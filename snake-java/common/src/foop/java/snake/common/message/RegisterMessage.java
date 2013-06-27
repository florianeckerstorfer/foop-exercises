package foop.java.snake.common.message;

/**
 * Message sent by client to server to register a new player.
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class RegisterMessage implements MessageInterface
{
	private static final long serialVersionUID = 1083636312265690990L;

	public static final int TYPE = 1;

	/**
	 * The player name.
	 */
	protected String playerName;
	
	/**
	 * The port number.
	 */
	protected int port;

	/**
	 * Constructor.
	 * 
	 * @param playerName
	 * @param port
	 */
	public RegisterMessage(String playerName, int port)
	{
		this.playerName = playerName;
		this.port = port;
	}

	/**
	 * Returns the name of the player.
	 *
	 * @return The player name
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * Returns the port.
	 *
	 * @return The port
	 */
	public int getPort()
	{
		return port;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}
}
