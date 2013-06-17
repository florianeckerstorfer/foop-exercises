package foop.java.snake.server.gameloop.collisiondetection;

import java.util.List;
import java.util.Map;

import foop.java.snake.common.snake.ISnake;

/**
 * Interface for collision detection strategies.
 * 
 * @package   foop.java.snake.server.gameloop.collisiondetection
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public interface CollisionDetectionStrategyInterface
{
	Integer DEAD_SNAKE_ID = 0;
	
	public void detectCollision(Map<Integer, ISnake> snakes, List<Integer> idsToRemove, ISnake snake);
}
