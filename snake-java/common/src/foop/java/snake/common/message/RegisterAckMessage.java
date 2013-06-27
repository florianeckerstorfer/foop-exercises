package foop.java.snake.common.message;

/**
 * Message sent by server to the client when the registration was successful.
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class RegisterAckMessage implements MessageInterface
{
	private static final long serialVersionUID = -3990327690216417999L;
	
	public static final int TYPE = 3;
	
	/**
	 * The player ID.
	 */
	protected int playerID;

	/**
	 * Constructor.
	 * 
	 * @param Id
	 */
	public RegisterAckMessage(int Id)
	{
		setPlayerID(Id);
	}

	@Override
	public int getType()
	{
		return TYPE;
	}

	/**
	 * Returns the player ID.
	 * 
	 * @return The player ID
	 */
	public int getPlayerID()
	{
		return playerID;
	}

	/**
	 * Sets the player ID.
	 * 
	 * @param playerID The player ID
	 */
	public void setPlayerID(int playerID)
	{
		this.playerID = playerID;
	}

}
