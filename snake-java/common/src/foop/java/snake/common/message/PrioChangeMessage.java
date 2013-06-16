package foop.java.snake.common.message;

import java.util.List;

/**
 * PrioChangeMessage.
 * Sent from Server to Clients whenever the priorities of the player changes
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
//TODO recheck and finish it
public class PrioChangeMessage implements MessageInterface
{
	private static final long serialVersionUID = 1;
	public static final int TYPE = 99;

	protected List<Integer> priorities;
	protected List<Integer> upcomingPriorities;

	public PrioChangeMessage(List<Integer> currPrios, List<Integer> nextPrios)
	{
		this.priorities = currPrios;
		this.upcomingPriorities = nextPrios;
	}

	@Override
	public int getType()
	{
		return TYPE;
	}

	/**
	 * Sets the priorities.
	 * 
	 * @param priorities The ordered list of priorities, highest priority first.
	 */
	public void setPriorities(List<Integer> priorities)
	{
		this.priorities = priorities;
	}

	/**
	 * Returns the priorities.
	 * 
	 * @return The ordered list of priorities
	 */
	public List<Integer> getPriorities()
	{
		return priorities;
	}

	/**
	 * Sets the upcoming priorities.
	 * 
	 * @param upcomingPriorities The ordered list of upcoming priorities, highest priority first.
	 */
	public void setUpcomingPriorities(List<Integer> upcomingPriorities)
	{
		this.upcomingPriorities = upcomingPriorities;
	}

	/**
	 * Returns the upcoming player priorities.
	 * 
	 * @return The orderd list of upcoming priorities.
	 */
	public List<Integer> getUpcomingPriorities()
	{
		return this.upcomingPriorities;
	}
}
