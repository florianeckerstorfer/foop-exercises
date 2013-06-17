package foop.java.snake.common.snake;

import foop.java.snake.common.board.SnakeHeadDirection;
import foop.java.snake.common.message.InputMessage;

public class Movement
{
	/**
	 * Enum to define movement-directions.
	 * Possible values are UP, DOWN, LEFT, RIGHT and NONE
	 */
	public enum Direction
	{
		UP, DOWN, LEFT, RIGHT, NONE
	}
	
	/**
	 * Converts from {@link Direction} to {@link SnakeHeadDirection}
	 *
	 * @param direction
	 * @return
	 */
	public static byte convertDirection(Direction direction)
	{
		switch (direction) {
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
	public static Direction convertKeyCodeToDirection(InputMessage.Keycode key)
	{
		switch (key) {
			case UP:
				return Direction.UP;
			case DOWN:
				return Direction.DOWN;
			case LEFT:
				return Direction.LEFT;
			case RIGHT:
				return Direction.RIGHT;
			case IGNORE:
			default:
				return Direction.NONE;
		}
	}
}
