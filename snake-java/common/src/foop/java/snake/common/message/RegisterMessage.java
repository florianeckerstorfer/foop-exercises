package foop.java.snake.common.message;

/**
 * Message sent by client to server to register a new player.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterMessage implements MessageInterface {
	public static final long serialVersionUID = 1;
	public static final int TYPE = 1;

	protected String playerName;
	protected int port;

	public RegisterMessage(String playerName, int port) {
		this.playerName = playerName;
		this.port = port;
	}

	/**
	 * Returns the name of the player.
	 *
	 * @return
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Returns the port.
	 *
	 * @return
	 */
	public int getPort() {
		return port;
	}

	public int getType() {
		return TYPE;
	}
}
