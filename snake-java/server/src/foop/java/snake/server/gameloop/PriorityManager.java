package foop.java.snake.server.gameloop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;

/**
 * PriorityManager.
 * 
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class PriorityManager
{
	/**
	 * List of current priorities.
	 */
	private List<Integer> priorities;
	
	/**
	 * List of upcoming priorities.
	 */
	private List<Integer> upcomingPriorities;
	
	/**
	 * Initializes the priority lists.
	 * 
	 * @param playerRegistry
	 * 
	 * @return true
	 */
	public Boolean init(PlayerRegistry playerRegistry)
	{
		System.out.println("init Priorities for " + playerRegistry.getPlayerCount() + " player");

		List<Player> players = playerRegistry.getPlayers();
		priorities = new ArrayList<Integer>();
		upcomingPriorities = new ArrayList<Integer>();

		for (Player player : players) {
			priorities.add(player.getId());
		}

		Collections.shuffle(priorities);
		List<Integer> nextPrios = new ArrayList<Integer>(priorities);
		Collections.shuffle(nextPrios);

		this.upcomingPriorities = nextPrios;

		return true; // send them during next sendMessage()
	}
	
	/**
	 * Changes / sets the players priorities. Initially random and later on just
	 * "rolling through". So strictly spoken the next prio... is not necessary....
	 * 
	 * @return true
	 */
	public void update(MessageHandler messageHandler)
	{
		List<Integer> nextPrios = priorities;
		Collections.shuffle(nextPrios);
		priorities = upcomingPriorities;
		upcomingPriorities = nextPrios;

		// Send message to clients
		messageHandler.sendPrioChangeMessage(this);
	}
	
	/**
	 * Returns the id of the player with the higher priority.
	 * 
	 * @param p1 The ID of player 1
	 * @param p2 The ID of player 2
	 * 
	 * @return player id
	 */
	public int compare(int p1, int p2)
	{
		return priorities.indexOf(p1) < priorities.indexOf(p2) ? p1 : p2;
	}
	
	public List<Integer> getPriorities()
	{
		return priorities;
	}
	
	public List<Integer> getUpcomingPriorities()
	{
		return upcomingPriorities;
	}
}
