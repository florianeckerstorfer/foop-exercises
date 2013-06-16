package foop.java.snake.server.gameloop.ai;

import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.snake.ISnake.Direction;

/**
 * Simple, random-based strategy for computer-controlled snakes to change their direction.
 * 
 * @package   foop.java.snake.common.snake
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian GrŸnbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class SimpleAiDirectionStrategy implements AiDirectionStrategyInterface
{
	@Override
	public Keycode newDirection(Direction currentDirection)
	{
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
}
