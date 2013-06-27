package foop.java.snake.common.message;

/**
 * Indicates, that the player has been killed and the game is over.
 * 
 * @package   foop.java.snake.common.message
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class GameOverMessage implements MessageInterface
{
	private static final long serialVersionUID = 1;
	
	public static final int TYPE = 128;
	
	private String message;

	/**
	 * Constructor.
	 * 
	 * @param message
	 */
	public GameOverMessage(String message) 
	{
		this.setMessage(message);
	}
	
	@Override
	public int getType()
	{
		return TYPE;
	}
	
	/**
	 * Sets the message.,
	 * 
	 * @param message
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}

	/**
	 * Returns the message.
	 * 
	 * @return
	 */
	public String getMessage()
	{
		return message;
	}
}
