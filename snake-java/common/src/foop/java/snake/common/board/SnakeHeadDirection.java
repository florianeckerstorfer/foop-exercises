package foop.java.snake.common.board;

/**
 * SnakeHeadDirection
 * Statics for the direction of the snake head.
 * Per definition the upper four bits of a board-field contain the information if a snake head, a snake body or nothing at all is at 
 * this position.   
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public final class SnakeHeadDirection {
	public static byte noSnake = 		(byte)0x00;
	public static byte snakeBody = 		(byte)0x10;
	public static byte snakeHeadUp = 	(byte)0x20;
	public static byte snakeHeadRight =	(byte)0x30;
	public static byte snakeHeadDown = 	(byte)0x40;
	public static byte snakeHeadLeft = 	(byte)0x50;
}

