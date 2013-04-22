package foop.java.snake.common.player;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.ArrayList;

/**
 * PlayerRegistry
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class PlayerRegistry
{
    protected List<Player> players = new ArrayList<Player>();
    protected static Deque<Integer> playerIDs = new ArrayDeque<Integer>();
    private static final int maxPlayerID=15;
    private static final int noPlayerID=-1;

    /**
     * Static block to initialize the Stack for player-IDs once
     * IDs are between 1 and maxPlayerID
     */
    static {
    	for (int i=maxPlayerID; i > 0; i--)
    		playerIDs.addFirst(new Integer(i));
    }

    /**
     * Adds a player to the registry.
     *
     * @param  player
     * @return
     */
    public PlayerRegistry addPlayer(Player player)
    {
        players.add(player);
        player.setID(this.getNextPlayerID());

        return this;
    }

    /**
     * Removes a player from the registry.
     *
     * @param  player
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
     * @param  name
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
     * @param  name
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
     * Returns the list of players.
     *
     * @return
     */
    public List<Player> getPlayers()
    {
        return players;
    }

    /**
     * Retrieves next Player-ID from Stack.
     *
     * @return player-ID
     */
    private int getNextPlayerID() {
	    try {
	    	return playerIDs.removeFirst().intValue();
	    } catch (Exception e) {
	    	System.out.println("PlayerRegistry: No ID for player left...");
	    	// TODO: What shall we do if we run out of IDs?
	    }
	    return noPlayerID;
    }

}
