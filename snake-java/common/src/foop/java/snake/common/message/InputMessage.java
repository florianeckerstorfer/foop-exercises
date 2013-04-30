package foop.java.snake.common.message;

/**
 * Message sent to the server upon keyboard input
 * @author Fabian Grünbichler
 *
 */
public class InputMessage implements MessageInterface {

	private static final long serialVersionUID = 1;
    public static final int TYPE = 4;

	public enum Keycode {
		IGNORE, UP, DOWN, LEFT, RIGHT, QUIT 
	}
	
	private Keycode input;
	private int playerID;
	
	public InputMessage(int playerID, Keycode input) {
		this.input = input;
		this.playerID = playerID;
	}

	public Keycode getInput() {
		return input;
	}

	public int getPlayerID() {
		return playerID;
	}
	
	@Override
	public int getType() {
		return InputMessage.TYPE;
	}

}
