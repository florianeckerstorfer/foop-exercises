package foop.java.snake.server.gameloop.ai;

import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.snake.Movement;

/**
 * Interface for classes that implement a strategy to make decisions on how a computer controlled snake
 * should change directions.
 * 
 * @package   foop.java.snake.common.snake
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public interface AiDirectionStrategyInterface
{
	/**
	 * Returns a new direction based on the current direction.
	 * 
	 * @param currentDirection
	 * @return
	 */
	public Keycode newDirection(Movement.Direction currentDirection);
}
