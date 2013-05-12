package foop.java.snake.common.message;

import java.util.List;

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

	protected List<Integer> currPrios;
	protected List<Integer> nextPrios;

	public PrioChangeMessage(List<Integer> currPrios, List<Integer> nextPrios) {
		this.currPrios = currPrios;
		this.nextPrios = nextPrios;
	}

	@Override
	public int getType() {
		return TYPE;
	}

	/*
	 * @param prio "ordered prioritylist with unique names of players. First in list has highest priority"
	 */
	public void setPlayerPrios(List<Integer> currPrios) {
		this.currPrios = currPrios;
	}

	/*
	 * @return ordered prioritylist with unique names of players. First in list has highest priority
	 */
	public List<Integer> getPlayerPrios() {
		return currPrios;
	}

	public void setNextPlayerPrios(List<Integer> nextPrios) {
		this.nextPrios = nextPrios;
	}

	public List<Integer> getNextPlayerPrios() {
		return this.nextPrios;
	}
}
