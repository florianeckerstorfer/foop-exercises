package foop.java.snake.common.message;

import foop.java.snake.common.board.Board;
/**
 * Indicates, that the player has been killed and the game is over.
 * @author  Robert Kapeller <rkapeller@gmail.com>
 *
 */
public class GameOverMessage implements MessageInterface {
	private static final long serialVersionUID = 1;
	public static final int TYPE = 128;
	private String message;

	public GameOverMessage(String message) {
		this.setMessage(message);
	}
	@Override
	public int getType() {
		return TYPE;
	}
	public void setMessage(String message) {
		this.message=message;
	}
	public String getMessage() {
		return message;
	}

}
