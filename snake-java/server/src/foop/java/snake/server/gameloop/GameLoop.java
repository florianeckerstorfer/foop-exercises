package foop.java.snake.server.gameloop;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPClientRegistry;

import java.io.IOException;
import java.util.*;

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
	final private int loopTime = 500;

	/**
	 * The number of iterations until the game starts
	 */
	final private int initialGameCountdown = 1;

	/**
	 * The number of iterations until the priority changes
	 */
	final private int priorityChangeInterval = 5;

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

    private List<InputMessage> inputMessages;

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
                    this.setSnakes();
                    this.sendMessage();
                }
			} else {
				// TODO: Implement "real" game logic, movement, collision detection, etc
                this.calculateBoard();
                this.sendMessage();
			}

			if ((loopCount % priorityChangeInterval) == 0) {
				// TODO: Send priority change to the players
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

    private void setSnakes() {
        List<Map<String, Integer>> fields = new ArrayList<Map<String, Integer>>();
        System.out.println("setsnakes " + playerRegistry.getPlayerCount());
        int i = 0;
        while(i < playerRegistry.getPlayerCount()) {
            Map<String, Integer> field = new HashMap<String, Integer>();
            field.put("id", playerRegistry.getPlayers().get(i).getId());
            field.put("row", (int) (Math.random() * board.getRows()));
            field.put("column", (int) (Math.random() * board.getColumns()));
            field.put("direction", 2 + (int) (Math.random() * (5-2 +1)));

            boolean isInList = false;
            for(Map<String, Integer> tmp : fields) {
                if (tmp.get("row") == field.get("row") && tmp.get("column") == field.get("column")) {
                    isInList = true;
                }
            }
            if (!isInList) {
                this.board.setField(
                        field.get("row"),
                        field.get("column"),
                        (byte)(field.get("direction") * 16 + field.get("id"))
                );
                fields.add(field);
                i++;
            }
        }
    }


    private void moveToField(int column, int row, byte snakeHeadDirection) {
        column = (column < 0) ? this.board.getColumns() + column : column % this.board.getColumns();
        row = (row < 0) ? this.board.getRows() + row : row % this.board.getRows();

        this.nextBoard.setField(column, row, snakeHeadDirection);
    }

    private void chooseDirection(int column, int row, InputMessage.Keycode key, int playerId) {
        switch(key) {
            case LEFT:
                this.moveToField(column - 1, row, (byte) (SnakeHeadDirection.snakeHeadLeft + playerId));
                break;
            case RIGHT:
                this.moveToField(column + 1, row, (byte) (SnakeHeadDirection.snakeHeadRight + playerId));
                break;
            case UP:
                this.moveToField(column, row - 1, (byte) (SnakeHeadDirection.snakeHeadUp + playerId));
                break;
            case DOWN:
                this.moveToField(column, row + 1, (byte) (SnakeHeadDirection.snakeHeadDown + playerId));
                break;
        }
    }

    private void moveSnakeHead(int column, int row, Player player) {
        System.out.println("moveSnakeHead from " + player.getName() + " [" + column + "/" + row + "] ");

        byte snakeHead = this.board.getSnakeHeadDirection(column, row);
        InputMessage.Keycode key = player.getKeycode();

        this.nextBoard.setField(column, row, (byte)0);

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
            this.chooseDirection(column, row, key, player.getId());
        }
    }

    private void calculateBoard() {
        System.out.println("calculateBoard");
        this.nextBoard = new Board(this.board.getColumns(), this.board.getRows());
        for (int i=0; i<this.board.getColumns(); i++) {
            for (int j=0; j<this.board.getRows(); j++) {
                if (this.board.isSnakeHead(i, j)) {
                    Player player = playerRegistry.getPlayerById(this.board.getPlayerNumber(i, j));
                    if (player != null) {
                        this.moveSnakeHead(i, j, player);
                    } else {
                        System.out.println("Error there should be a player");
                    }
                }
            }
        }
        this.board = this.nextBoard;
    }

    private void sendMessage() {
        System.out.println("Send board");
        for(Player player : this.playerRegistry.getPlayers()) {
            try {
                TCPClient client = this.clientRegistry.getClient(player.getAddress());
                client.sendMessage(new BoardMessage(this.board));
                client.sendMessage(new PrioChangeMessage(playerRegistry.getPlayers()));
            } catch (IOException e) {
                System.out.println("Error while sending to " + player.getName());
            }
        }
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
