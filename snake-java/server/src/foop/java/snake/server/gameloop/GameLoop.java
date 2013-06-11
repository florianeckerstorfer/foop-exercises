package foop.java.snake.server.gameloop;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.GameOverMessage;
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
	private List<Integer> iDsToRemove = new ArrayList<Integer>();
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

	/** 
	 * Test-Method. Normally not used. To be removed
	 */
	private void doTest() {
		this.initPrios();
		this.currentPriorities.add(1);
		this.currentPriorities.add(2);
		board = new Board(17, 17);
		Snake s1 = new Snake(0, new Point(6, 8), 2, Snake.Direction.RIGHT, 17, 17);
		Snake s2 = new Snake(1, new Point(10, 8), 2, Snake.Direction.LEFT, 17, 17);
		snakes.put(0, s1);
		snakes.put(10, s2);

		while (true) {
			s1.move();
			detectCollForSnake(s1);
			s2.move();
			detectCollForSnake(s2);
			drawSnakesOnBoard();
//			this.calculateSnakeMovement();
			sendMessages();
			try {
				Thread.sleep(300);
			} catch (Exception e) {
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
				//	doTest();
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
				this.calculateSnakeMovement();
				this.sendMessages();
			}

			if (((loopCount % priorityChangeInterval) == 0) && gameCountdown == 0) {
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
				// TODO remove dead snake
				sendGameOverMessage(s2.getId(), s1.getId());
				iDsToRemove.add(s2.getId());
			}
			DeadSnake deadSnake = (DeadSnake) this.snakes.get(DEAD_SNAKE_ID);
			deadSnake.addBodyParts(cutBodyParts);
		}
	}
	/**
	 * Sends a gameOver-Message to the given player
	 * @param playerId
	 */
	private void sendGameOverMessage(int lostId, int winningId) {
		System.out.println("Sending messages to players");
		Player lostPlayer = playerRegistry.getPlayerById(lostId);
		if (lostPlayer.isAI()) {
			return;
		}
		try {
			String message = playerRegistry.getPlayerById(winningId).getName();;
			TCPClient client = this.clientRegistry.getClient(lostPlayer.getAddress());
			client.sendMessage(new GameOverMessage("You lost!\n" + message + " killed you!"));
		} catch (IOException e) {
			System.out.println("Error while sending to " + lostPlayer.getName());
		}
	}

	private void detectCollForSnake(ISnake snake) {
		for (ISnake s2 : snakes.values()) {
			if (snake == s2) {
				continue;
			}

			if (s2.checkPosition(snake.getHead()) == ISnake.SnakePart.HEAD) {
				System.out.println("case1: Snake head " + snake.getId() + " on snake head " + s2.getId());
				handlePriorityCollision(snake, s2);
			} else if (s2.checkPosition(snake.getHead()) == ISnake.SnakePart.BODY) {
				if (snake.checkPosition(s2.getHead()) == ISnake.SnakePart.BODY) {
					System.out.println("case2: Snake head " + snake.getId() + " on snake head " + s2.getId());
					handlePriorityCollision(snake, s2);
				} else {
					System.out.println("case3: Snake head " + snake.getId() + " on snake body " + s2.getId());
					handleCollision(snake, s2);
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

	private void calculateSnakeMovement() {
		// loop over all players and move their snakes accordingly
		// Snake killed? what to do? I assume, that the player is no longer in the list then...
		// In this first iteration of the final solution I just move the snake and draw it on the board
		
		iDsToRemove.clear();
		
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
			detectCollForSnake(snakes.get(player.getId()));
		}
		// remove all data of "eaten" snakes
		for (int i: iDsToRemove) {
			snakes.remove(new Integer(i));
			currentPriorities.remove(new Integer(i));
			nextPriorities.remove(new Integer(i));
			playerRegistry.removesPlayer(playerRegistry.getPlayerById(i));
		}
		// send new list of participating players
		if (iDsToRemove.size() > 0) {
			sendPlayerMessages();
			prioChanged=true;
		}
		
		this.drawSnakesOnBoard();
		
	}

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
