package foop.java.snake.common.message;

/**
 * UnregisterMessage.
 * 
 * Sent from Clients to server whenever a client wants to remove itself from the game
 * Shall we ACK this as well??? Or is it rather fire and forget?
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */

public class UnregisterMessage implements MessageInterface
{
	private static final long serialVersionUID = 4938929410818088927L;
	
	public static final int TYPE = 5;
	
	/**
	 * The player name.
	 */
	protected String playerName;

	/**
	 * Constructor.
	 * 
	 * @param playerName
	 */
	public UnregisterMessage(String playerName) 
	{
		this.playerName = playerName;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}

	/**
	 * Returns the name of the player.
	 * 
	 * @return The name of the player
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * Sets the name of the player.
	 * @param name The name of the player
	 */
	public void setPlayerName(String name)
	{
		playerName = name;
	}
}
