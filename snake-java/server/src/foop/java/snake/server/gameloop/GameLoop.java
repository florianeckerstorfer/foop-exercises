package foop.java.snake.server.gameloop;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.snake.DeadSnake;
import foop.java.snake.common.snake.ISnake;
import foop.java.snake.common.snake.Snake;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPClientRegistry;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * GameLoop
 *
 * @author florian@eckerstorfer.co (Florian Eckerstorfer)
 */
public class GameLoop extends Thread implements Observer {
	/**
	 * The time that each loop takes
	 */
	final private int loopTime = 200;

	/**
	 * The number of iterations until the game starts
	 */
	final private int initialGameCountdown = 10;

	/**
	 * The number of iterations until the priority changes
	 */
	final private int priorityChangeInterval = 50;

	/**
	 * The minimum number of players
	 */
	final private int minPlayerCount = 10;

	/**
	 * Stores the player registry
	 */
	private PlayerRegistry playerRegistry;
	private TCPClientRegistry clientRegistry;

	/**
	 * Stores the current board
	 */
	private Board board;
	private Board nextBoard;
	private int boardColumns = 30;
	private int boardRows = 30;

	private List<Integer> currentPriorities;
	private List<Integer> nextPriorities;

	/**
	 * indicates if new prio shall be sent
	 */
	private boolean prioChanged = false;

	/**
	 * the snakes crawling on this planet
	 */
	private Map<Integer, ISnake> snakes = new HashMap<Integer, ISnake>();
	/**
	 * how long will they be initially?
	 */
	int initialMaxSnakeLength = 8;
	int initialMinSnakeLength = 3;

	private int DEAD_SNAKE_ID = 0;

	/**
	 * Constructor.
	 *
	 * @param playerRegistry
	 */
	public GameLoop(PlayerRegistry playerRegistry, TCPClientRegistry clientRegistry) {
		this.playerRegistry = playerRegistry;
		this.clientRegistry = clientRegistry;
		board = new Board(boardColumns, boardRows);
	}

	private void doTest() {
		board = new Board(17, 17);
		Snake s1 = new Snake(0, new Point(10, 18), 13, Snake.Direction.DOWN, 17, 17);
		Snake s2 = new Snake(10, new Point(11, 2), 5, Snake.Direction.LEFT, 17, 17);
		snakes.put(0, s1);
		snakes.put(10, s2);

		while (true) {
			s1.move();
			s2.move();
			drawSnakesOnBoard();
			sendMessages();
			try {
				Thread.sleep(300);
			} catch (Exception e) {
			}
			Point pos = s2.getSnakeBody().get(0);
			if (s1.checkPosition(pos) == Snake.SnakePart.BODY) {
				s1.cut(pos);
				s2.grow();
			}
		}
	}

	/**
	 * Threads that runs the game loop.
	 */
	public void run() {
		int loopCount = 0;
		int playerCount = 0;
		int gameCountdown = initialGameCountdown;
		this.board = new Board(this.boardColumns, this.boardRows);

		while (true) {
			System.out.println("Game Loop #" + loopCount);
			if (0 == playerCount && 0 == playerRegistry.getPlayerCount()) {
				// No registered players
				System.out.println("No registered players. But I will wait until the end of times");
			} else if (0 == playerCount && playerRegistry.getPlayerCount() > 0) {

				// TEST
				this.sendPlayerMessages();
				// doTest();
				// TEST-END

				// The first iteration that has registered players. Start the countdown
				playerCount = playerRegistry.getPlayerCount();
				System.out.println("Registered " + playerRegistry.getPlayerCount() + " players.");
			} else if (playerCount > 0 && 0 == playerRegistry.getPlayerCount()) {
				// In the previous iteration we had players in the loop, but there are all gone.
				// --> End (or reset) the game
				playerCount = 0;
				gameCountdown = initialGameCountdown;
				// TODO: End game
			} else if (gameCountdown > 0) {
				// We have registered players, but the countdown is still running down.

				System.out.println("Game starts in " + gameCountdown);
				gameCountdown--;
				if (gameCountdown == 0) {
					if (playerCount < this.minPlayerCount)
						this.addAISnakes();
					this.initSnakes();
					this.initPrios();
					this.sendPlayerMessages();
					this.sendMessages();
				}
			} else {
				// TODO: Implement "real" game logic, movement, collision detection, etc
				this.calculateSnakeMovement();
				this.sendMessages();
			}

			if (((loopCount % priorityChangeInterval) == 0) && gameCountdown == 0) {
				// TODO: Send priority change to the players
				this.changePrios();
			}

			try {
				Thread.sleep(loopTime);
			} catch (InterruptedException e) {
				System.out.println("Sleep interrupted! Game Loop #" + loopCount);
				e.printStackTrace();
			}
			loopCount++;
		}
	}

	private void sendPlayerMessages() {
		System.out.println("Sending initial player messages to players");
		List<Player> players = this.playerRegistry.getPlayers();
		for (Player player : players) {
			if (!player.isAI()) {
				try {
					TCPClient client = this.clientRegistry.getClient(player.getAddress());
					client.sendMessage(new PlayerInfoMessage(players));
				} catch (IOException e) {
					System.out.println("Error while sending to " + player.getName());
				}
			}
		}
	}

	private void addAISnakes() {
		int name = 1;
		int count = this.minPlayerCount - playerRegistry.getPlayerCount();
		System.out.println("Adding " + count + " AI snakes to meet minimum number of snakes..");
		while (count > 0) {
			Player ai = new Player("AI " + name, true);
			playerRegistry.addPlayer(ai);
			count--;
			name++;
		}
	}

	/**
	 * Initially sets the snakes for the players. First version: fixed y-value, evenly distributed x-value,
	 * all point to the same direction
	 */
	private void initSnakes() {
		List<Player> players = this.playerRegistry.getPlayers();
		int countOfPlayers = players.size();
		int yPos = boardRows / 2;
		int xPos = 0;
		int diffX = boardColumns / countOfPlayers;

		for (Player p : players) {
			xPos = xPos + diffX;
			int initialSnakeLength = (int) ((double) initialMaxSnakeLength * Math.random() + initialMinSnakeLength);
			this.snakes.put(p.getId(), new Snake(p.getId(), new Point(xPos, yPos), initialSnakeLength, Snake.Direction.DOWN, boardColumns, boardRows));
		}

		//add dead snake
		this.snakes.put(DEAD_SNAKE_ID, new DeadSnake());
		drawSnakesOnBoard();
	}

	/**
	 * copies the snakes onto the board
	 */
	private void drawSnakesOnBoard() {
		// nextBoard = new Board(this.board.getColumns(), this.board.getRows());
		board.clearBoard();
		for (ISnake snake : snakes.values()) {
			int id = snake.getId();
			List<Point> body = snake.getSnakeBody();
			Snake.Direction dir = snake.getCurrentDirection();
			for (int i = 0; i < body.size(); i++) {
				Point pos = body.get(i);
				byte snakeDirectionOnBoard;

				if (i == 0) {
					// we are drawing the head
					snakeDirectionOnBoard = convertDirection(dir);
				} else {
					snakeDirectionOnBoard = SnakeHeadDirection.snakeBody;
				}
				board.setField(pos.x, pos.y, (byte) (snakeDirectionOnBoard + id));
				// nextBoard.setField(pos.x, pos.y, (byte)(snakeDirectionOnBoard + id));
			}
		}
//        this.board = this.nextBoard;
	}

	/**
	 * Returns the id of the player with the higher id.
	 * @param p1	player 1 id
	 * @param p2	player 2 id
	 * @return 		player id
	 */
	private int comparePriorities(int p1, int p2) {
		int p1Index = this.currentPriorities.indexOf(p1);
		int p2Index = this.currentPriorities.indexOf(p2);

		return p1Index < p2Index ? p1 : p2;
	}

	/**
	 * Handles head collisions, snake with higher priority wins.
	 * @param s1	snake 1
	 * @param s2	snake 2
	 */
	private void handlePriorityCollision(ISnake s1, ISnake s2) {
		int id = comparePriorities(s1.getId(), s2.getId());
		if (s1.getId() == id) {
			handleCollision(s1, s2);
		} else if (s2.getId() == id) {
			handleCollision(s2, s1);
		}
	}

	/**
	 * Handle collision, the winning snake grows and the losing snake is cut at the collision point.
	 * @param s1	winning snake
	 * @param s2	losing snake
	 */
	private void handleCollision(ISnake s1, ISnake s2) {
		s1.grow();
		List<Point> cutBodyParts = s2.cut(s1.getHead());

		if(s2.getId() == DEAD_SNAKE_ID) {
			return;
		}

		if (cutBodyParts != null) {
			if (s2.getHead() == null) {
				//TODO remove dead snake
			}
			DeadSnake deadSnake = (DeadSnake) this.snakes.get(DEAD_SNAKE_ID);
			deadSnake.addBodyParts(cutBodyParts);
		}
	}

	/**
	 Detects and handles collisions.
	 */
	private void detectCollisions() {
		for (ISnake s1 : snakes.values()) {
			if (s1.getId() == DEAD_SNAKE_ID) {
				continue;
			}

			for (ISnake s2 : snakes.values()) {
				if (s1 == s2) {
					continue;
				}

				if (s2.checkPosition(s1.getHead()) == ISnake.SnakePart.HEAD) {
					System.out.println("case1: Snake head " + s1.getId() + " on snake head " + s2.getId());
					handlePriorityCollision(s1, s2);
				} else if (s2.checkPosition(s1.getHead()) == ISnake.SnakePart.BODY) {
					if (s1.checkPosition(s2.getHead()) == ISnake.SnakePart.BODY) {
						System.out.println("case2: Snake head " + s1.getId() + " on snake head " + s2.getId());
						handlePriorityCollision(s1, s2);
					} else {
						System.out.println("case3: Snake head " + s1.getId() + " on snake body " + s2.getId());
						handleCollision(s1, s2);
					}
				}
			}
		}
	}

	/**
	 * Converts from {@link Snake.Direction} to {@link SnakeHeadDirection}
	 *
	 * @param dir
	 * @return
	 */
	private byte convertDirection(Snake.Direction dir) {
		switch (dir) {
			case UP:
				return SnakeHeadDirection.snakeHeadUp;
			case DOWN:
				return SnakeHeadDirection.snakeHeadDown;
			case LEFT:
				return SnakeHeadDirection.snakeHeadLeft;
			case RIGHT:
				return SnakeHeadDirection.snakeHeadRight;
			case NONE:
			default:
				return SnakeHeadDirection.noSnake;
		}
	}

	/**
	 * Converts from {@link InputMessage.Keycode} to {@link Snake.Direction}
	 *
	 * @param key
	 * @return
	 */
	private Snake.Direction convertKeyCodeToDist(InputMessage.Keycode key) {
		switch (key) {
			case UP:
				return Snake.Direction.UP;
			case DOWN:
				return Snake.Direction.DOWN;
			case LEFT:
				return Snake.Direction.LEFT;
			case RIGHT:
				return Snake.Direction.RIGHT;
			case IGNORE:
			default:
				return Snake.Direction.NONE;
		}
	}
//	/**
//     * Randomly generate a snake consisting of head and body.
//     */
//    private void initSnakes() {
//        List<Map<String, Integer>> history = new ArrayList<Map<String, Integer>>();
//        System.out.println("init snakes " + playerRegistry.getPlayerCount());
//        int i = 0;
//        while(i < playerRegistry.getPlayerCount()) {
//            //random values for snake head field
//            Map<String, Integer> headField = new HashMap<String, Integer>();
//            headField.put("row", (int) (Math.random() * (board.getRows()-2)+1));
//            headField.put("column", (int) (Math.random() * (board.getColumns()-2)+1));
//            int id= playerRegistry.getPlayers().get(i).getId(),
//            int head =  2 + (int) (Math.random() * (5-2 +1));
//            byte headDirection = (byte)(head * 16 + id);
//            byte snakeHead = this.board.getHeadDirection(headDirection);
//
//            //random values for snake body field
//            int row = 0,
//                column = 0,
//                rand = -1 + (int) (Math.random() * 3);
//
//            if (snakeHead == SnakeHeadDirection.snakeHeadUp) {
//                column = rand;
//                if (column == 0) {
//                    row = 1;
//                }
//                System.out.println("up " + column + " " +row);
//            } else if (snakeHead == SnakeHeadDirection.snakeHeadDown) {
//                column = rand;
//                if (column == 0) {
//                    row = -1;
//                }
//            } else if (snakeHead == SnakeHeadDirection.snakeHeadLeft) {
//                row = rand;
//                if (row == 0) {
//                    column = 1;
//                }
//            } else if (snakeHead == SnakeHeadDirection.snakeHeadRight) {
//                row = rand;
//                if (row == 0) {
//                    column = -1;
//                }
//            }
//
//            Map<String, Integer> bodyField = new HashMap<String, Integer>();
//            bodyField.put("row", headField.get("row") + row);
//            bodyField.put("column", headField.get("column") + column);
//
//            if (isNotInHistory(history, headField.get("column"), headField.get("row")) &&
//                    isNotInHistory(history, bodyField.get("column"), bodyField.get("row"))) {
//                System.out.println("head: " + headField.get("column") + "/" + headField.get("row"));
//                this.board.setField(
//                        headField.get("column"),
//                        headField.get("row"),
//                        headDirection
//                );
//                System.out.println("body: " + bodyField.get("column") + "/" + bodyField.get("row"));
//                this.board.setField(
//                        bodyField.get("column"),
//                        bodyField.get("row"),
//                        (byte)(SnakeHeadDirection.snakeBody + id)
//                );
//
//                history.add(headField);
//                history.add(bodyField);
//                i++;
//            }
//        }
//    }

	/**
	 * Moves a snake part to a specific field
	 *
	 * @param column column
	 * @param row    row
	 * @param snake  snake part
	 */
	private void moveToField(int column, int row, byte snake) {
		column = (column < 0) ? this.board.getColumns() + column : column % this.board.getColumns();
		row = (row < 0) ? this.board.getRows() + row : row % this.board.getRows();

		this.nextBoard.setField(column, row, snake);
	}

	/**
	 * Logic to move a snake head in a specific direction
	 *
	 * @param column
	 * @param row
	 * @param player
	 */
	private void moveSnakeHead(int column, int row, Player player) {
		//System.out.println("moveSnakeHead from " + player.getName() + " " + column + "/" + row);

		byte snakeHead = this.board.getSnakeHeadDirection(column, row);
		InputMessage.Keycode key = player.getKeycode();

		if (snakeHead == SnakeHeadDirection.snakeHeadUp) {
			if (key == null || key == InputMessage.Keycode.IGNORE || key == InputMessage.Keycode.DOWN) {
				key = InputMessage.Keycode.UP;
			}
		} else if (snakeHead == SnakeHeadDirection.snakeHeadDown) {
			if (key == null || key == InputMessage.Keycode.IGNORE || key == InputMessage.Keycode.UP) {
				key = InputMessage.Keycode.DOWN;
			}
		} else if (snakeHead == SnakeHeadDirection.snakeHeadLeft) {
			if (key == null || key == InputMessage.Keycode.IGNORE || key == InputMessage.Keycode.RIGHT) {
				key = InputMessage.Keycode.LEFT;
			}
		} else if (snakeHead == SnakeHeadDirection.snakeHeadRight) {
			if (key == null || key == InputMessage.Keycode.IGNORE || key == InputMessage.Keycode.LEFT) {
				key = InputMessage.Keycode.RIGHT;
			}
		}

		if (key != null) {
			switch (key) {
				case LEFT:
					this.moveToField(column - 1, row,
						(byte) (SnakeHeadDirection.snakeHeadLeft + player.getId()));
					break;
				case RIGHT:
					this.moveToField(column + 1, row,
						(byte) (SnakeHeadDirection.snakeHeadRight + player.getId()));
					break;
				case UP:
					this.moveToField(column, row - 1,
						(byte) (SnakeHeadDirection.snakeHeadUp + player.getId()));
					break;
				case DOWN:
					this.moveToField(column, row + 1,
						(byte) (SnakeHeadDirection.snakeHeadDown + player.getId()));
					break;
				default:
					break;
			}
		}
	}

	/**
	 * Determinate if a specified field is not in a history.
	 *
	 * @param history
	 * @param column
	 * @param row
	 * @return boolean
	 */
	private boolean isNotInHistory(List<Map<String, Integer>> history, int column, int row) {
		for (Map<String, Integer> field : history) {
			if (field.get("column") == column && field.get("row") == row) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Moves the body of a snake.
	 *
	 * @param column
	 * @param row
	 * @param playerId
	 * @param history
	 */
	private void moveSnakeBody(int column, int row, int playerId, List<Map<String, Integer>> history) {
		//System.out.println("moveBody of PLayer " + playerId + " to " + column + "/" + row + " ");

		Map<String, Integer> field = new HashMap<String, Integer>();
		field.put("column", column);
		field.put("row", row);
		history.add(field);

		Byte body = (byte) (SnakeHeadDirection.snakeBody + playerId);
		int incCol = (column + 1) % this.board.getColumns(),
			incRow = (row + 1) % this.board.getRows(),
			decCol = (column - 1 < 0) ? this.board.getColumns() + column - 1 : column - 1,
			decRow = (row - 1 < 0) ? this.board.getRows() + row - 1 : row - 1;

		//check if there's another body part
		if (isNotInHistory(history, incCol, row) &&
			this.board.isSnake(incCol, row) &&
			this.board.getPlayerNumber(incCol, row) == playerId) {
			moveToField(column, row, body);
			moveSnakeBody(incCol, row, playerId, history);
		} else if (isNotInHistory(history, column, incRow) &&
			this.board.isSnake(column, incRow) &&
			this.board.getPlayerNumber(column, incRow) == playerId) {
			moveToField(column, row, body);
			moveSnakeBody(column, incRow, playerId, history);
		} else if (isNotInHistory(history, decCol, row) &&
			this.board.isSnake(decCol, row) &&
			this.board.getPlayerNumber(decCol, row) == playerId) {
			moveToField(column, row, body);
			moveSnakeBody(decCol, row, playerId, history);
		} else if (isNotInHistory(history, column, decRow) &&
			this.board.isSnake(column, decRow) &&
			this.board.getPlayerNumber(column, decRow) == playerId) {
			moveToField(column, row, body);
			moveSnakeBody(column, decRow, playerId, history);
		}
	}


	private void calculateSnakeMovement() {
		// TODO implement this!!!!!
		// loop over all players and move their snakes accordingly
		// Snake killed? what to do? I assume, that the player is no longer in the list then...
		// In this first iteration of the final solution I just move the snake and draw it on the board
		for (Player player : playerRegistry.getPlayers()) {
			ISnake snake = snakes.get(player.getId());
			if (snake == null) {
				continue;
			}

			if (player.isAI()) {
				player.setKeycode(aiDecision(snake.getCurrentDirection()));
			}

			InputMessage.Keycode key = player.getKeycode();
			if (key != null) {
				snake.move(convertKeyCodeToDist(key));
			} else {
				// keep the snake moving to current direction
				snake.move();
			}
		}
		this.detectCollisions();
		this.drawSnakesOnBoard();
	}

//    /**
//     * Calculates and generates the next board.
//     */
//    private void calculateBoard() {
//        System.out.println("calculateBoard");
//        this.nextBoard = new Board(this.board.getColumns(), this.board.getRows());
//        for (int i=0; i<this.board.getColumns(); i++) {
//            for (int j=0; j<this.board.getRows(); j++) {
//                //find snake head
//                if (this.board.isSnakeHead(i, j) &&
//                        (this.board.getSnakeHeadDirection(i,j)!=SnakeHeadDirection.snakeBody)) {
//                    Player player = playerRegistry.getPlayerById(this.board.getPlayerNumber(i, j));
//                    if (player != null) {
//                    	if(player.isAI()) {
//                    		aiDecision(i,j);
//                    	}
//                    	this.moveSnakeHead(i, j, player);
//	                    this.moveSnakeBody(i, j, player.getId(), new ArrayList<Map<String, Integer>>());
//                    } else {
//                        System.out.println("Error there should be a player");
//                    }
//                }
//            }
//        }
//        this.board = this.nextBoard;
//    }

	private Keycode aiDecision(Snake.Direction currentDirection) {
		Double decision = Math.random();
		switch (currentDirection) {
			case UP:
				if (decision >= 0.9)
					return Keycode.RIGHT;
				else if (decision <= 0.1)
					return Keycode.LEFT;
				break;
			case DOWN:
				if (decision >= 0.9)
					return Keycode.RIGHT;
				else if (decision <= 0.1)
					return Keycode.LEFT;
				break;
			case RIGHT:
				if (decision >= 0.9)
					return Keycode.UP;
				else if (decision <= 0.1)
					return Keycode.DOWN;
				break;
			case LEFT:
				if (decision >= 0.9)
					return Keycode.UP;
				else if (decision <= 0.1)
					return Keycode.DOWN;
				break;
			default:
				return Keycode.IGNORE;
		}
		return Keycode.IGNORE;
	}

	private void aiDecision(int column, int row) {
		Player player = this.playerRegistry.getPlayerById(this.board.getPlayerNumber(column, row));
		byte direction = this.board.getSnakeHeadDirection(column, row);
		Double decision = Math.random();
		switch (direction) {
			case SnakeHeadDirection.snakeHeadUp:
				if (decision >= 0.9)
					player.setKeycode(Keycode.RIGHT);
				else if (decision <= 0.1)
					player.setKeycode(Keycode.LEFT);
				break;
			case SnakeHeadDirection.snakeHeadDown:
				if (decision >= 0.9)
					player.setKeycode(Keycode.RIGHT);
				else if (decision <= 0.1)
					player.setKeycode(Keycode.LEFT);
				break;
			case SnakeHeadDirection.snakeHeadRight:
				if (decision >= 0.9)
					player.setKeycode(Keycode.UP);
				else if (decision <= 0.1)
					player.setKeycode(Keycode.DOWN);
				break;
			case SnakeHeadDirection.snakeHeadLeft:
				if (decision >= 0.9)
					player.setKeycode(Keycode.UP);
				else if (decision <= 0.1)
					player.setKeycode(Keycode.DOWN);
				break;
			default:
				player.setKeycode(Keycode.IGNORE);
		}
	}

	private void sendMessages() {
		System.out.println("Sending messages to players");
		List<Player> players = this.playerRegistry.getPlayers();
		for (Player player : players) {
			if (!player.isAI()) {
				try {
					TCPClient client = this.clientRegistry.getClient(player.getAddress());
					client.sendMessage(new BoardMessage(this.board));
					if (prioChanged) {
						System.out.println("before sending: currPrios/nextPrios: " + this.currentPriorities.size() + "/" + this.nextPriorities.size());
						client.sendMessage(new PrioChangeMessage(this.currentPriorities, this.nextPriorities));
					}
				} catch (IOException e) {
					System.out.println("Error while sending to " + player.getName());
				}
			}
		}
		if (prioChanged)
			prioChanged = false;
	}

	/**
	 * Changes / sets the players priorities. Initially random and later on just
	 * "rolling through". So strictly spoken the next prio... is not necessary....
	 */
	private void changePrios() {
		System.out.println("Change Priorities");
		if (this.nextPriorities == null)
			System.out.println("Next prios == null");
		if (this.currentPriorities == null)
			System.out.println("prios == null");

		System.out.println("before change currPrios/nextPrios: " + this.currentPriorities.size() + "/" + this.nextPriorities.size());

		List<Integer> nextPrios = this.currentPriorities;
		Collections.shuffle(nextPrios);
		this.currentPriorities = this.nextPriorities;
		this.nextPriorities = nextPrios;

		System.out.println("after change currPrios/nextPrios: " + this.currentPriorities.size() + "/" + this.nextPriorities.size());


		prioChanged = true; // send them during next senMessage()
	}

	private void initPrios() {
		System.out.println("init Priorities for " + playerRegistry.getPlayerCount() + " player");

		List<Player> players = playerRegistry.getPlayers();
		this.currentPriorities = new ArrayList<Integer>();
		this.nextPriorities = new ArrayList<Integer>();

		for (Player player : players) {
			currentPriorities.add(player.getId());
		}

		Collections.shuffle(currentPriorities);
		List<Integer> nextPrios = new ArrayList<Integer>(currentPriorities);
		Collections.shuffle(nextPrios);

		this.nextPriorities = nextPrios;

		prioChanged = true; // send them during next sendMessage()
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof InputMessage) {
			InputMessage tmp = (InputMessage) arg;
			Player player = playerRegistry.getPlayerById(tmp.getPlayerID());
			if (player != null) {
				player.setKeycode(tmp.getInput());
			}
		}
	}
}
