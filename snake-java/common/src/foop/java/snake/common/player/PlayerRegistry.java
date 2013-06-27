package foop.java.snake.common.player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * PlayerRegistry
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class PlayerRegistry
{
	/**
	 * List of players.
	 */
	protected List<Player> players = new ArrayList<Player>();
	
	/**
	 * Queue of available player IDs.
	 */
	protected static Deque<Integer> playerIDs = new ArrayDeque<Integer>();
	
	/**
	 * Maximum player ID.
	 */
	private static final int maxPlayerID = 15;
	
	/**
	 * Pseudo ID when no more IDs are available.
	 */
	private static final int noPlayerID = -1;

	/**
	 * Static block to initialize the Stack for player-IDs once
	 * IDs are between 1 and maxPlayerID
	 */
	static 
	{
		for (int i = maxPlayerID; i > 0; i--)
			playerIDs.addFirst(new Integer(i));
	}

	/**
	 * Adds a player to the registry.
	 *
	 * @param player
	 * @return
	 */
	public PlayerRegistry addPlayer(Player player)
	{
		players.add(player);
		player.setId(this.getNextPlayerID());
		System.out.println(player.getId());
		return this;
	}

	/**
	 * Removes a player from the registry.
	 *
	 * @param player
	 * @return
	 */
	public PlayerRegistry removesPlayer(Player player)
	{
		players.remove(player);
		playerIDs.addFirst(new Integer(player.getId())); //put back ID
		return this;
	}

	/**
	 * Returns if a player with the given name exists.
	 *
	 * @param name
	 * @return TRUE if a player with the given name exists, FALSE if not.
	 */
	public boolean hasPlayerName(String name)
	{
		Player player = getPlayerByName(name);
		if (null == player) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns the player with the given name.
	 *
	 * @param name
	 * @return
	 */
	public Player getPlayerByName(String name)
	{
		for (Player player : players) {
			if (name.equals(player.getName())) {
				return player;
			}
		}

		return null;
	}

	/**
	 * Returns the player by id.
	 *
	 * @param id player id
	 * @return Player
	 */
	public Player getPlayerById(int id)
	{
		for (Player player : players) {
			if (id == player.getId()) {
				return player;
			}
		}

		return null;
	}

	/**
	 * Updates the given player.
	 * 
	 * @param player
	 */
	public void updatePlayer(Player player)
	{
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getId() == player.getId()) {
				players.set(i, player);
				return;
			}
		}
	}

	/**
	 * Returns the list of players.
	 *
	 * @return
	 */
	public List<Player> getPlayers()
	{
		return players;
	}

	/**
	 * Returns the number of currently registered players.
	 *
	 * @return The number of players
	 */
	public int getPlayerCount()
	{
		return players.size();
	}

	/**
	 * Retrieves next Player-ID from Stack.
	 *
	 * @return player-ID
	 */
	private int getNextPlayerID()
	{
		try {
			return playerIDs.removeFirst().intValue();
		} catch (Exception e) {
			System.out.println("PlayerRegistry: No ID for player left...");
		}
		
		return noPlayerID;
	}
}
