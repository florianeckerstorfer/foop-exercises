package foop.java.snake.common.message;

/**
 * Message sent by server to the client when an error occured during the registration.
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class RegisterErrorMessage implements MessageInterface
{
	private static final long serialVersionUID = -2236475586173055370L;

	public static final int TYPE = 2;

	/**
	 * The error message.
	 */
	protected String message;

	/**
	 * Constructor.
	 * 
	 * @param message The error message.
	 */
	public RegisterErrorMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Returns the error message.
	 * 
	 * @return The error message
	 */
	public String getMessage()
	{
		return message;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}
}
