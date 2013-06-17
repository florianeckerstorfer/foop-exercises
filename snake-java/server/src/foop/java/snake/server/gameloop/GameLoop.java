package foop.java.snake.server.gameloop;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.snake.DeadSnake;
import foop.java.snake.common.snake.ISnake;
import foop.java.snake.common.snake.Movement;
import foop.java.snake.common.snake.Snake;
import foop.java.snake.server.gameloop.ai.AiDirectionStrategyInterface;
import foop.java.snake.server.gameloop.collisiondetection.CollisionDetectionStrategyInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	
	private List<Integer> idsToRemove = new ArrayList<Integer>();
	/**
	 * indicates if new prio shall be sent
	 */
	private boolean prioChanged = false;

	/**
	 * The snakes crawling on this planet
	 */
	private Map<Integer, ISnake> snakes = new HashMap<Integer, ISnake>();
	
	/**
	 * how long will they be initially?
	 */
	int initialMaxSnakeLength = 8;
	int initialMinSnakeLength = 3;

	private AiDirectionStrategyInterface aiDirectionStrategy;
	
	/**
	 * The priority manager
	 */
	private PriorityManager priorityManager;

	/**
	 * The collision detection strategy
	 */
	private CollisionDetectionStrategyInterface collisionDetectionStrategy;

	/**
	 * The message handler.
	 */
	private MessageHandler messageHandler;

	/**
	 * Constructor.
	 *
	 * @param playerRegistry
	 */
	public GameLoop(PlayerRegistry playerRegistry,
			AiDirectionStrategyInterface aiDirectionStrategy, PriorityManager priorityManager,
			CollisionDetectionStrategyInterface collisionDetectionStrategy, MessageHandler messageHandler)
	{
		this.playerRegistry = playerRegistry;
		this.aiDirectionStrategy = aiDirectionStrategy;
		this.priorityManager = priorityManager;
		this.collisionDetectionStrategy = collisionDetectionStrategy;
		this.messageHandler = messageHandler;
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
				// The countdown is finished.
				// Initialize the game
				if (gameCountdown == 0) {
					if (playerCount < this.minPlayerCount) {
						this.addAISnakes();
					}
					this.initSnakes();
					prioChanged = priorityManager.init(playerRegistry);
					messageHandler.sendPlayerInfoMessage();
					messageHandler.sendBoardMessage(board);
					if (prioChanged) {
						messageHandler.sendPrioChangeMessage(priorityManager);
					}
				}
			} else {
				// Normal game loop iteration. Move snakes and send messages
				this.calculateSnakeMovement();
				messageHandler.sendBoardMessage(board);
				if (prioChanged) {
					messageHandler.sendPrioChangeMessage(priorityManager);
				}
			}

			// Check if we need to change the priorities
			if (((loopCount % priorityChangeInterval) == 0) && gameCountdown == 0) {
				prioChanged = priorityManager.update();
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

	/**
	 * Adds computer snakes to the game.
	 * 
	 */
	private void addAISnakes()
	{
		int name = 1;
		int count = this.minPlayerCount - playerRegistry.getPlayerCount();
		System.out.println("Adding " + count + " AI snakes to meet minimum number of snakes..");
		
		while (count > 0) {
			Player ai = new Player("Computer " + name, true);
			playerRegistry.addPlayer(ai);
			count--;
			name++;
		}
	}

	/**
	 * Initially sets the snakes for the players. First version: fixed y-value, evenly distributed x-value,
	 * all point to the same direction
	 */
	private void initSnakes()
	{
		List<Player> players = this.playerRegistry.getPlayers();
		int countOfPlayers = players.size();
		int yPos = board.getRows() / 2;
		int xPos = 0;
		int diffX = board.getColumns() / countOfPlayers;

		for (Player p : players) {
			xPos = xPos + diffX;
			int initialSnakeLength = (int) ((double) initialMaxSnakeLength * Math.random() + initialMinSnakeLength);
			this.snakes.put(p.getId(), new Snake(p.getId(), new Point(xPos, yPos), initialSnakeLength, Movement.Direction.DOWN, board.getColumns(), board.getRows()));
		}

		// Add dead snake
		snakes.put(CollisionDetectionStrategyInterface.DEAD_SNAKE_ID, new DeadSnake());
		drawSnakesOnBoard();
	}

	/**
	 * copies the snakes onto the board
	 */
	private void drawSnakesOnBoard()
	{
		// Clear the board
		board.clearBoard();
		
		// Iterate through all snakes
		for (ISnake snake : snakes.values()) {
			int id = snake.getId();
			List<Point> body = snake.getSnakeBody();
			Movement.Direction dir = snake.getCurrentDirection();
			for (int i = 0; i < body.size(); i++) {
				Point pos = body.get(i);
				byte snakeDirectionOnBoard;

				if (i == 0) {
					// We are drawing the head
					snakeDirectionOnBoard = Movement.convertDirection(dir);
				} else {
					snakeDirectionOnBoard = SnakeHeadDirection.snakeBody;
				}
				board.setField(pos.x, pos.y, (byte) (snakeDirectionOnBoard + id));
			}
		}
//        this.board = this.nextBoard;
	}
	
	private void calculateSnakeMovement()
	{
		// loop over all players and move their snakes accordingly
		// Snake killed? what to do? I assume, that the player is no longer in the list then...
		// In this first iteration of the final solution I just move the snake and draw it on the board
		
		idsToRemove.clear();
		
		for (Player player : playerRegistry.getPlayers()) {
			ISnake snake = snakes.get(player.getId());
			if (snake == null) {
				continue;
			}

			if (player.isAi()) {
				player.setKeycode(aiDirectionStrategy.newDirection(snake.getCurrentDirection()));
			}

			InputMessage.Keycode key = player.getKeycode();
			if (key != null) {
				snake.move(Movement.convertKeyCodeToDirection(key));
			} else {
				// keep the snake moving to current direction
				snake.move();
			}
			collisionDetectionStrategy.detectCollision(snakes, idsToRemove, snakes.get(player.getId()));
		}
		// remove all data of "eaten" snakes
		for (int i: idsToRemove) {
			snakes.remove(new Integer(i));
			priorityManager.getPriorities().remove(new Integer(i));
			priorityManager.getUpcomingPriorities().remove(new Integer(i));
			playerRegistry.removesPlayer(playerRegistry.getPlayerById(i));
		}
		// send new list of participating players
		if (idsToRemove.size() > 0) {
			messageHandler.sendPlayerInfoMessage();
			prioChanged=true;
		}
		
		this.drawSnakesOnBoard();
		
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
