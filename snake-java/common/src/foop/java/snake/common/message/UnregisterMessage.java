package foop.java.snake.common.message;

/**
 * UnregisterMessage
 * Sent from Clients to server whenever a client wants to remove itself from the game
 * Shall we ACK this as well??? Or is it rather fire and forget?
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */

public class UnregisterMessage implements MessageInterface {
	private static final long serialVersionUID = 1;
	public static final int TYPE = 5;
	protected String playerName;

	public UnregisterMessage(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public int getType() {
		return TYPE;
	}

	/**
	 * @return name of the player
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * @param name name of the player
	 */
	public void setPlayerName(String name) {
		playerName = name;
	}
}
