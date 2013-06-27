package foop.java.snake.server.gameloop;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;

import java.util.Observable;
import java.util.Observer;

/**
 * GameLoop
 *
 * @package   foop.java.snake.server.gameloop
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class GameLoop extends Thread implements Observer
{
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

	/**
	 * Stores the current board
	 */
	private Board board = new Board(30, 30);
	
	/**
	 * The priority manager
	 */
	private PriorityManager priorityManager;

	/**
	 * The message handler.
	 */
	private MessageHandler messageHandler;

	/**
	 * The snake manager.
	 */
	private SnakeManager snakeManager;

	/**
	 * Constructor.
	 *
	 * @param playerRegistry
	 */
	public GameLoop(PlayerRegistry playerRegistry, PriorityManager priorityManager,
			MessageHandler messageHandler, SnakeManager snakeManager)
	{
		this.playerRegistry = playerRegistry;
		this.priorityManager = priorityManager;
		this.messageHandler = messageHandler;
		this.snakeManager = snakeManager;
	}

	/**
	 * Threads that runs the game loop.
	 */
	public void run()
	{
		int loopCount = 0;
		int playerCount = 0;
		int gameCountdown = initialGameCountdown;
		this.board = new Board(board.getColumns(), board.getRows());

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
				
				// The countdown is finished, initialize the game
				if (gameCountdown == 0) {
					if (playerCount < minPlayerCount) {
						snakeManager.initAiSnakes(minPlayerCount);
					}
					snakeManager.initSnakes(board);
					priorityManager.init(playerRegistry);
					messageHandler.sendPlayerInfoMessage();
					messageHandler.sendBoardMessage(board);
					messageHandler.sendPrioChangeMessage(priorityManager);
				}
			} else {
				// Normal game loop iteration. Move snakes and send messages
				if (snakeManager.calculateMovement(board)) {
					messageHandler.sendPlayerInfoMessage();
					messageHandler.sendPrioChangeMessage(priorityManager);
				}
				messageHandler.sendBoardMessage(board);
			}

			// Check if we need to change the priorities
			if ((0 == (loopCount % priorityChangeInterval)) && 0 == gameCountdown) {
				priorityManager.update(messageHandler);
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

	@Override
	public void update(Observable o, Object arg)
	{
		if (arg instanceof InputMessage) {
			InputMessage tmp = (InputMessage) arg;
			Player player = playerRegistry.getPlayerById(tmp.getPlayerId());
			if (player != null) {
				player.setKeycode(tmp.getInput());
			}
		}
	}
}
