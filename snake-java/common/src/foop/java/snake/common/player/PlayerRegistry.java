package foop.java.snake.common.player;

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

    /**
     * Adds a player to the registry.
     *
     * @param  player
     * @return
     */
    public PlayerRegistry addPlayer(Player player)
    {
        players.add(player);
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
}
