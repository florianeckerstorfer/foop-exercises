package foop.java.snake.server.gameloop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPClientRegistry;

/**
 * GameLoop
 *
 * @author florian@eckerstorfer.co (Florian Eckerstorfer)
 */
public class GameLoop extends Thread implements Observer
{
	/**
	 * The time that each loop takes
	 */
	final private int loopTime = 100;

	/**
	 * The number of iterations until the game starts
	 */
	final private int initialGameCountdown = 10;

	/**
	 * The number of iterations until the priority changes
	 */
	final private int priorityChangeInterval = 20;
	
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
	 * Constructor.
	 *
	 * @param playerRegistry
	 */
	public GameLoop(PlayerRegistry playerRegistry, TCPClientRegistry clientRegistry)
	{
		this.playerRegistry = playerRegistry;
        this.clientRegistry = clientRegistry;
	}

    /**
	 * Threads that runs the game loop.
	 */
	public void run()
	{
		int loopCount = 0;
		int playerCount = 0;
		int gameCountdown = initialGameCountdown;
        this.board = new Board(this.boardRows, this.boardColumns);

		while (true) {
			System.out.println("Game Loop #" + loopCount);
			if (0 == playerCount && 0 == playerRegistry.getPlayerCount()) {
				// No registered players
				System.out.println("No registered players. But I will wait until the end of times");
			} else if (0 == playerCount && playerRegistry.getPlayerCount() > 0) {
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
                	if(playerCount<this.minPlayerCount)
                		this.addAISnakes();
                    this.initSnakes();
                    this.initPrios();
                    this.sendPlayerMessages();
                    this.sendMessages();
                }
			} else {
				// TODO: Implement "real" game logic, movement, collision detection, etc
                this.calculateBoard();
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
        for(Player player : players) {
        	if(!player.isAI()) {
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
    	int count = this.minPlayerCount-playerRegistry.getPlayerCount();
    	System.out.println("Adding "+count+" AI snakes to meet minimum number of snakes..");
    	while(count>0) {
    		Player ai = new Player("AI "+name,true);
    		playerRegistry.addPlayer(ai);
    		count--;
    		name++;
    	}
	}

	/**
     * Randomly generate a snake consisting of head and body.
     */
    private void initSnakes() {
        List<Map<String, Integer>> history = new ArrayList<Map<String, Integer>>();
        System.out.println("init snakes " + playerRegistry.getPlayerCount());
        int i = 0;
        while(i < playerRegistry.getPlayerCount()) {
            //random values for snake head field
            Map<String, Integer> headField = new HashMap<String, Integer>();
            headField.put("row", (int) (Math.random() * (board.getRows()-2)+1));
            headField.put("column", (int) (Math.random() * (board.getColumns()-2)+1));
            int id= playerRegistry.getPlayers().get(i).getId(),
                head =  2 + (int) (Math.random() * (5-2 +1));
            byte headDirection = (byte)(head * 16 + id);
            byte snakeHead = this.board.getHeadDirection(headDirection);

            //random values for snake body field
            int row = 0,
                column = 0,
                rand = -1 + (int) (Math.random() * 3);

            if (snakeHead == SnakeHeadDirection.snakeHeadUp) {
                column = rand;
                if (column == 0) {
                    row = 1;
                }
                System.out.println("up " + column + " " +row);
            } else if (snakeHead == SnakeHeadDirection.snakeHeadDown) {
                column = rand;
                if (column == 0) {
                    row = -1;
                }
            } else if (snakeHead == SnakeHeadDirection.snakeHeadLeft) {
                row = rand;
                if (row == 0) {
                    column = 1;
                }
            } else if (snakeHead == SnakeHeadDirection.snakeHeadRight) {
                row = rand;
                if (row == 0) {
                    column = -1;
                }
            }

            Map<String, Integer> bodyField = new HashMap<String, Integer>();
            bodyField.put("row", headField.get("row") + row);
            bodyField.put("column", headField.get("column") + column);

            if (isNotInHistory(history, headField.get("column"), headField.get("row")) &&
                    isNotInHistory(history, bodyField.get("column"), bodyField.get("row"))) {
                System.out.println("head: " + headField.get("column") + "/" + headField.get("row"));
                this.board.setField(
                        headField.get("column"),
                        headField.get("row"),
                        headDirection
                );
                System.out.println("body: " + bodyField.get("column") + "/" + bodyField.get("row"));
                this.board.setField(
                        bodyField.get("column"),
                        bodyField.get("row"),
                        (byte)(SnakeHeadDirection.snakeBody + id)
                );

                history.add(headField);
                history.add(bodyField);
                i++;
            }
        }
    }

    /**
     * Moves a snake part to a specific field
     * @param column    column
     * @param row       row
     * @param snake     snake part
     */
    private void moveToField(int column, int row, byte snake) {
        column = (column < 0) ? this.board.getColumns() + column : column % this.board.getColumns();
        row = (row < 0) ? this.board.getRows() + row : row % this.board.getRows();

        this.nextBoard.setField(column, row, snake);
    }

    /**
     * Logic to move a snake head in a specific direction
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
            switch(key) {
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
     * @param history
     * @param column
     * @param row
     * @return  boolean
     */
    private boolean isNotInHistory(List<Map<String, Integer>> history, int column, int row) {
        for (Map<String, Integer> field: history) {
            if (field.get("column") == column && field.get("row") == row) {
                return false;
            }
        }
        return true;
    }

    /**
     * Moves the body of a snake.
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

    /**
     * Calculates and generates the next board.
     */
    private void calculateBoard() {
        System.out.println("calculateBoard");
        this.nextBoard = new Board(this.board.getColumns(), this.board.getRows());
        for (int i=0; i<this.board.getColumns(); i++) {
            for (int j=0; j<this.board.getRows(); j++) {
                //find snake head
                if (this.board.isSnakeHead(i, j) &&
                        (this.board.getSnakeHeadDirection(i,j)!=SnakeHeadDirection.snakeBody)) {
                    Player player = playerRegistry.getPlayerById(this.board.getPlayerNumber(i, j));
                    if (player != null) {
                    	if(player.isAI()) {
                    		aiDecision(i,j);
                    	} 
                    	this.moveSnakeHead(i, j, player);
	                    this.moveSnakeBody(i, j, player.getId(), new ArrayList<Map<String, Integer>>());
                    } else {
                        System.out.println("Error there should be a player");
                    }
                }
            }
        }
        this.board = this.nextBoard;
    }

    private void aiDecision(int column, int row) {
    	Player player = this.playerRegistry.getPlayerById(this.board.getPlayerNumber(column,row));
    	byte direction = this.board.getSnakeHeadDirection(column, row);
		Double decision=Math.random();
		switch (direction) {
			case SnakeHeadDirection.snakeHeadUp:
				if(decision>=0.9)
					player.setKeycode(Keycode.RIGHT);
				else if(decision<=0.1)
					player.setKeycode(Keycode.LEFT);
				break;
			case SnakeHeadDirection.snakeHeadDown:
				if(decision>=0.9)
					player.setKeycode(Keycode.RIGHT);
				else if(decision<=0.1)
					player.setKeycode(Keycode.LEFT);
				break;
			case SnakeHeadDirection.snakeHeadRight:
				if(decision>=0.9)
					player.setKeycode(Keycode.UP);
				else if(decision<=0.1)
					player.setKeycode(Keycode.DOWN);
				break;
			case SnakeHeadDirection.snakeHeadLeft:
				if(decision>=0.9)
					player.setKeycode(Keycode.UP);
				else if(decision<=0.1)
					player.setKeycode(Keycode.DOWN);
				break;
			default:
				player.setKeycode(Keycode.IGNORE);
		}
	}

	private void sendMessages() {
        System.out.println("Sending messages to players");
        List<Player> players = this.playerRegistry.getPlayers();
        for(Player player : players) {
        	if(!player.isAI()) {
	            try {
	                TCPClient client = this.clientRegistry.getClient(player.getAddress());
	                client.sendMessage(new BoardMessage(this.board));
	                if (prioChanged) {
	                    System.out.println("before sending: currPrios/nextPrios: "+this.currentPriorities.size()+"/"+this.nextPriorities.size());
		                client.sendMessage(new PrioChangeMessage(this.currentPriorities,this.nextPriorities));
	                }
	            } catch (IOException e) {
	                System.out.println("Error while sending to " + player.getName());
	            }
        	}
        }
        if (prioChanged)
        	prioChanged=false;
    }
    /**
     * Changes / sets the players priorities. Initially random and later on just
     * "rolling through". So strictly spoken the next prio... is not necessary....
     */
    private void changePrios() {
        System.out.println("Change Priorities");
        if(this.nextPriorities==null)
        	System.out.println("Next prios == null");
        if(this.currentPriorities==null)
        	System.out.println("prios == null");
        
        System.out.println("before change currPrios/nextPrios: "+this.currentPriorities.size()+"/"+this.nextPriorities.size());
        
        List<Integer> nextPrios = this.currentPriorities;
        Collections.shuffle(nextPrios);
        this.currentPriorities=this.nextPriorities;
        this.nextPriorities=nextPrios;
        
        System.out.println("after change currPrios/nextPrios: "+this.currentPriorities.size()+"/"+this.nextPriorities.size());

        
    	prioChanged=true; // send them during next senMessage()
    }

    private void initPrios() {
        System.out.println("init Priorities for " + playerRegistry.getPlayerCount() + " player");

		List<Player> players = playerRegistry.getPlayers();
		this.currentPriorities = new ArrayList<Integer>();
		this.nextPriorities = new ArrayList<Integer>();
		
		for(Player player: players) {
			currentPriorities.add(player.getId());
		}
		
		Collections.shuffle(currentPriorities);
		List<Integer> nextPrios = new ArrayList<Integer>(currentPriorities);
		Collections.shuffle(nextPrios);
		
		this.nextPriorities=nextPrios;
		
		prioChanged=true; // send them during next sendMessage()
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
