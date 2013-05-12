package foop.java.snake.client.gui;

import foop.java.snake.common.player.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PlayerPanel
 * Used to draw information about the Players (color, priority...)
 * Remark: Probably better to incorporate this functionality into BoardPanel as there are
 * shared data like color
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class PlayerPanel extends JPanel {
	private MainFrame parent;
	private List<Player> players;
	private List<Integer> currentPriorities;
	private List<Integer> nextPriorities;
	private int myID;


	public PlayerPanel(MainFrame parent) {
		this.parent = parent;
		this.setPreferredSize(new Dimension(120, 100));    // To set some width

		TitledBorder b = new TitledBorder("Players");
		b.setTitleColor(Color.DARK_GRAY);
		this.setBorder(b);
	}

	public void setMyID(int ID) {
		this.myID = ID;
	}

	public int getMyID() {
		return this.myID;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * renders the player list
	 *
	 * @param graphics canvas
	 */
	@Override
	public void paintComponent(Graphics graphics) {
		// clean the player-list
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

		//check if there are any players
		if (players == null || (players != null && players.isEmpty())) {
			graphics.setColor(Color.BLACK);
			graphics.drawString("Waiting for Players:", 10, 50);
			return;
		}

		//Player List
		graphics.setColor(Color.BLACK);
		int line = 2 * parent.getOffset();
		graphics.drawString("Players", 10, line);
		line += parent.getOffset();
		Iterator<Player> it = players.iterator();
		while (it.hasNext()) {
			Player p = it.next();
			graphics.setColor(parent.getPlayerColor(p.getId()));
			if (p.getId() == this.myID)
				graphics.drawString(String.format("%s (me)", p.getName()), 10, line);
			else
				graphics.drawString(String.format("%s", p.getName()), 10, line);
			line += parent.getOffset();
		}

		//Priorities
		graphics.setColor(Color.BLACK);
		line += 2 * parent.getOffset();
		graphics.drawString("Priorities", 10, line);
		line += parent.getOffset();
		System.out.println(this.currentPriorities.size() + " current priorities: " + this.currentPriorities.toString());
		String prioNames[] = new String[this.currentPriorities.size()];
		Color prioColors[] = new Color[this.currentPriorities.size()];

		for (Player p : players) {
			int prioIndex = this.currentPriorities.indexOf(p.getId());
			System.out.println("Priority " + p.getId() + " of player #" + p.getId() + " has index " + prioIndex);
			prioNames[prioIndex] = p.getName();
			prioColors[prioIndex] = parent.getPlayerColor(p.getId());
		}

		System.out.println("Player names for prio list set: " + prioNames.length);

		for (int i = 0; i < prioNames.length; i++) {
			graphics.setColor(prioColors[i]);
			graphics.drawString(prioNames[i], 10, line);
			line += parent.getOffset();
		}

		//Upcoming Priorities
		graphics.setColor(Color.BLACK);
		line += 2 * parent.getOffset();
		graphics.drawString("Upcoming Priorities", 10, line);
		line += parent.getOffset();
		System.out.println(this.nextPriorities.size() + " next priorities: " + this.nextPriorities.toString());
		prioNames = new String[this.nextPriorities.size()];
		prioColors = new Color[this.currentPriorities.size()];

		for (Player p : players) {
			int prioIndex = this.nextPriorities.indexOf(p.getId());
			System.out.println("Priority " + p.getId() + " of player #" + p.getId() + " has index " + prioIndex);
			if (prioNames[prioIndex] == null) {
				prioNames[prioIndex] = p.getName();
				prioColors[prioIndex] = parent.getPlayerColor(p.getId());
			}
		}

		System.out.println("Player names for prio list set: " + prioNames.length);

		for (int i = 0; i < prioNames.length; i++) {
			graphics.setColor(prioColors[i]);
			graphics.drawString(prioNames[i], 10, line);
			line += parent.getOffset();
		}
	}

	public void setNextPrio(List<Integer> nextPlayerPrios) {
		this.nextPriorities = new ArrayList<Integer>(nextPlayerPrios);
	}

	public void setCurrentPrio(List<Integer> playerPrios) {
		this.currentPriorities = new ArrayList<Integer>(playerPrios);
	}
}
