package foop.java.snake.server.gameloop;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Manages snakes and snake movement.
 * 
 * @package   foop.java.snake.server.gameloop
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 
 */
public class SnakeManager
{
	/**
	 * The snakes crawling on this planet
	 */
	private Map<Integer, ISnake> snakes = new HashMap<Integer, ISnake>();
	
	/**
	 * how long will they be initially?
	 */
	int initialMaxSnakeLength = 8;
	int initialMinSnakeLength = 3;
	
	/**
	 * The player registry.
	 */
	private PlayerRegistry playerRegistry;

	/**
	 * The AI direction strategy.
	 */
	private AiDirectionStrategyInterface aiDirectionStrategy;

	/**
	 * The collision detection strategy.
	 */
	private CollisionDetectionStrategyInterface collisionDetection;

	/**
	 * The priority manager.
	 */
	private PriorityManager priorityManager;

	public SnakeManager(PlayerRegistry playerRegistry, PriorityManager priorityManager,
			AiDirectionStrategyInterface aiDirectionStrategy, CollisionDetectionStrategyInterface collisionDetection)
	{
		this.playerRegistry = playerRegistry;
		this.priorityManager = priorityManager;
		this.aiDirectionStrategy = aiDirectionStrategy;
		this.collisionDetection = collisionDetection;
	}
	
	/**
	 * Returns the list of snakes.
	 * 
	 * @return The list of snakes
	 */
	public Map<Integer, ISnake> getSnakes()
	{
		return snakes;
	}
	
	/**
	 * Initially sets the snakes for the players. First version: fixed y-value, evenly distributed x-value,
	 * all point to the same direction
	 */
	public void initSnakes(Board board)
	{
		List<Player> players = playerRegistry.getPlayers();
		int countOfPlayers = players.size();
		int yPos = board.getRows() / 2;
		int xPos = 0;
		int diffX = board.getColumns() / countOfPlayers;

		for (Player p : players) {
			xPos = xPos + diffX;
			int initialSnakeLength = (int) ((double) initialMaxSnakeLength * Math.random() + initialMinSnakeLength);
			this.snakes.put(p.getId(), new Snake(p.getId(), new Point(xPos, yPos), initialSnakeLength, Movement.Direction.DOWN, board.getColumns(), board.getRows()));
		}

		// Initialize dead snake
		snakes.put(CollisionDetectionStrategyInterface.DEAD_SNAKE_ID, new DeadSnake());
		
		drawSnakes(board);
	}
	
	/**
	 * copies the snakes onto the board
	 */
	public void drawSnakes(Board board)
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
	}
	
	public Boolean calculateMovement(Board board)
	{
		// loop over all players and move their snakes accordingly
		// Snake killed? what to do? I assume, that the player is no longer in the list then...
		// In this first iteration of the final solution I just move the snake and draw it on the board
		
		List<Integer> idsToRemove = new ArrayList<Integer>();
		
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
			collisionDetection.detectCollision(snakes, idsToRemove, snakes.get(player.getId()));
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
			return true;
		}
		
		drawSnakes(board);
		
		return false;
	}
	
	/**
	 * Adds computer snakes to the game.
	 * 
	 */
	public void initAiSnakes(int minSnakeCount)
	{
		int name = 1;
		int count = minSnakeCount - playerRegistry.getPlayerCount();
		System.out.println("Adding " + count + " AI snakes to meet minimum number of snakes..");
		
		while (count > 0) {
			Player ai = new Player("Computer " + name, true);
			playerRegistry.addPlayer(ai);
			count--;
			name++;
		}
	}
}
