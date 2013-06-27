package foop.java.snake.common.message;

/**
 * Message sent to the server upon keyboard input.
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Fabian Grünbichler
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class InputMessage implements MessageInterface
{
	private static final long serialVersionUID = 1;
	
	public static final int TYPE = 4;

	/**
	 * Possible key codes.
	 */
	public enum Keycode {
		IGNORE, UP, DOWN, LEFT, RIGHT, QUIT
	}

	/**
	 * The user input.
	 */
	private Keycode input;
	
	/**
	 * The ID of the player.
	 */
	private int playerId;

	/**
	 * Constructor.
	 * 
	 * @param playerId
	 * @param input
	 */
	public InputMessage(int playerId, Keycode input)
	{
		this.input = input;
		this.playerId = playerId;
	}

	/**
	 * Returns the user input.
	 * 
	 * @return
	 */
	public Keycode getInput()
	{
		return input;
	}

	/**
	 * Returns the player ID.
	 * 
	 * @return
	 */
	public int getPlayerId()
	{
		return playerId;
	}

	@Override
	public int getType()
	{
		return InputMessage.TYPE;
	}
}
