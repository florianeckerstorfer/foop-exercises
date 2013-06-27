package foop.java.snake.common.message;

import foop.java.snake.common.player.Player;

import java.util.List;

/**
 * PlayerInfoMessage.
 * 
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class PlayerInfoMessage implements MessageInterface
{
	private static final long serialVersionUID = 1L;
	
	public static final int TYPE = 127;

	/**
	 * Lst of players.
	 */
	private List<Player> players;

	/**
	 * Constructor.
	 * 
	 * @param players
	 */
	public PlayerInfoMessage(List<Player> players)
	{
		this.players = players;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}

	/**
	 * Returns the list of players.
	 * 
	 * @return
	 */
	public List<Player> getPlayers()
	{
		return this.players;
	}

}
