package foop.java.snake.common.message;
import java.util.List;

import foop.java.snake.common.player.Player;

/**
 * PrioChangeMessage
 * Sent from Server to Clients whenever the priorities of the player changes
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
//TODO recheck and finish it
public class PrioChangeMessage implements MessageInterface {
	private static final long serialVersionUID = 1;
    public static final int TYPE = 99;
    /*
     * Just a ordered list of player-IDs. First in the list has highest Prio
     */
    protected List<Player> playerPrio;

    public PrioChangeMessage(List<Player> list)
    {
        this.playerPrio = list;
    }
    
	@Override
	public int getType() {
		return TYPE;
	}

	/*
	 * @param prio "ordered prioritylist with unique names of players. First in list has highest priority"
	 */
	public void setPlayerPrio(List<Player> prio) {
		this.playerPrio=prio;
	}
	
	/*
	 * @return ordered prioritylist with unique names of players. First in list has highest priority
	 */
	public List<Player> getPlayerPrio() {
		return playerPrio;
	}	
}
