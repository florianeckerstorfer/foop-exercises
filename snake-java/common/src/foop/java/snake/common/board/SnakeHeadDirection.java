package foop.java.snake.common.board;

/**
 * SnakeHeadDirection
 * Statics for the direction of the snake head.
 * Per definition the upper four bits of a board-field contain the information if a snake head, a snake body or nothing at all is at
 * this position.
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public final class SnakeHeadDirection {
	public final static byte noSnake = (byte) 0x00;
	public final static byte snakeBody = (byte) 0x10;
	public final static byte snakeHeadUp = (byte) 0x20;
	public final static byte snakeHeadRight = (byte) 0x30;
	public final static byte snakeHeadDown = (byte) 0x40;
	public final static byte snakeHeadLeft = (byte) 0x50;
}

