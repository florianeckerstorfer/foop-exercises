package foop.java.snake.client.gui;

import foop.java.snake.common.player.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PlayerPanel
 * Used to draw information about the Players (color, priority...)
 * Remark: Probably better to incorporate this functionality into BoardPanel as there are
 * shared data like color
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
@SuppressWarnings("serial")
public class PlayerPanel extends JPanel {
	private MainFrame parent;
	private List<Player> players;
	private List<Integer> currentPriorities;
	private List<Integer> nextPriorities;
	private int myID;


	public PlayerPanel(MainFrame parent) {
		this.parent = parent;
		this.setPreferredSize(new Dimension(240, 100));    // To set some width

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
		Font font = new Font ("Sans Serif", Font.BOLD , 11);
		graphics.setFont(font);
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

		//Priorities
		int xPos = 10;
		graphics.setColor(Color.BLACK);
		line += 2 * parent.getOffset();
		graphics.drawString("Priorities", xPos, line);
		line += parent.getOffset();

		String prioNames[] = new String[this.currentPriorities.size()];
		Color prioColors[] = new Color[this.currentPriorities.size()];

		for (Player p : players) {
			int prioIndex = this.currentPriorities.indexOf(p.getId());

			prioNames[prioIndex] = p.getName();
			prioColors[prioIndex] = parent.getPlayerColor(p.getId());
		}

		for (int i = 0; i < prioNames.length; i++) {
			graphics.setColor(prioColors[i]);
			graphics.drawString(prioNames[i], xPos, line);
			line += parent.getOffset();
		}

		//Upcoming Priorities
		line = 2 * parent.getOffset();
		line += 2 * parent.getOffset();
		xPos = 120;
		graphics.setColor(Color.BLACK);
		graphics.drawString("Upcoming Priorities", xPos, line);
		line += parent.getOffset();

		prioNames = new String[this.nextPriorities.size()];
		prioColors = new Color[this.currentPriorities.size()];

		for (Player p : players) {
			int prioIndex = this.nextPriorities.indexOf(p.getId());
			if (prioNames[prioIndex] == null) {
				prioNames[prioIndex] = p.getName();
				prioColors[prioIndex] = parent.getPlayerColor(p.getId());
			}
		}

		for (int i = 0; i < prioNames.length; i++) {
			graphics.setColor(prioColors[i]);
			graphics.drawString(prioNames[i], xPos, line);
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
