package foop.java.snake.server.gameloop.collisiondetection;

import java.awt.Point;
import java.util.List;
import java.util.Map;

import foop.java.snake.common.snake.DeadSnake;
import foop.java.snake.common.snake.ISnake;
import foop.java.snake.server.gameloop.MessageHandler;
import foop.java.snake.server.gameloop.PriorityManager;

/**
 * Simple collision detection interface.
 * 
 * @package   foop.java.snake.server.gameloop.collisiondetection
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class SimpleCollisionDetectionStrategy implements
		CollisionDetectionStrategyInterface
{
	/**
	 * The priority manager.
	 */
	private PriorityManager priorityManager;

	/**
	 * The message handler.
	 */
	private MessageHandler messageHandler;

	/**
	 * Constructor.
	 * 
	 * @param priorityManager
	 */
	public SimpleCollisionDetectionStrategy(PriorityManager priorityManager, MessageHandler messageHandler)
	{
		this.priorityManager = priorityManager;
		this.messageHandler = messageHandler;
	}
	
	/**
	 * Detects if the given snake collides with another snake.
	 * 
	 * @param snakes
	 * @param idsToRemove
	 * @param snake
	 */
	public void detectCollision(Map<Integer, ISnake> snakes, List<Integer> idsToRemove, ISnake snake)
	{
		for (ISnake s2 : snakes.values()) {
			if (snake == s2) {
				continue;
			}

			if (s2.checkPosition(snake.getHead()) == ISnake.SnakePart.HEAD) {
				System.out.println("case1: Snake head " + snake.getId() + " on snake head " + s2.getId());
				handleHeadCollision(snakes, idsToRemove, snake, s2);
			} else if (s2.checkPosition(snake.getHead()) == ISnake.SnakePart.BODY) {
				if (snake.checkPosition(s2.getHead()) == ISnake.SnakePart.BODY) {
					System.out.println("case2: Snake head " + snake.getId() + " on snake head " + s2.getId());
					handleHeadCollision(snakes, idsToRemove, snake, s2);
				} else {
					System.out.println("case3: Snake head " + snake.getId() + " on snake body " + s2.getId());
					handleCollision(snakes, idsToRemove, snake, s2);
				}
			}
		}
	}
	
	/**
	 * Handles head collisions, snake with higher priority wins.
	 * 
	 * @param snakes The list of snakes
	 * @param snake1 The first snake
	 * @param snake2 The second snake
	 */
	private void handleHeadCollision(Map<Integer, ISnake> snakes, List<Integer> idsToRemove, ISnake snake1, ISnake snake2)
	{
		int winner = priorityManager.compare(snake1.getId(), snake2.getId());
		
		if (snake1.getId() == winner) {
			handleCollision(snakes, idsToRemove, snake1, snake2);
		} else if (snake2.getId() == winner) {
			handleCollision(snakes, idsToRemove, snake2, snake1);
		}
	}

	/**
	 * Handle collision, the winning snake grows and the losing snake is cut at the collision point.
	 * 
	 * @param snakes The list of snakes
	 * @param winner The winning snake
	 * @param loser  The losing snake
	 * 
	 * @return TRUE when the loser is dead, FALSE when otherwise.
	 */
	private void handleCollision(Map<Integer, ISnake> snakes, List<Integer> idsToRemove, ISnake winner, ISnake loser)
	{
		winner.grow();
		List<Point> cutBodyParts = loser.cut(winner.getHead());

		if(loser.getId() == DEAD_SNAKE_ID) {
			return;
		}

		if (cutBodyParts != null) {
			if (loser.getHead() == null) {
				messageHandler.sendGameOverMessage(loser.getId(), winner.getId());
				idsToRemove.add(loser.getId());
			}
			
			DeadSnake deadSnake = (DeadSnake) snakes.get(DEAD_SNAKE_ID);
			deadSnake.addBodyParts(cutBodyParts);
		}
	}
}
