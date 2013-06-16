package foop.java.snake.client.gui;

import foop.java.snake.common.player.Player;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PlayerPanel
 * 
 * Used to draw information about the Players (color, priority...)
 * Remark: Probably better to incorporate this functionality into BoardPanel as there are
 * shared data like color
 *
 * @package   foop.java.snape.client.gui
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class PlayerPanel extends JPanel
{
	private static final long serialVersionUID = 7957053122451535883L;
	
	/**
	 * The main game frame.
	 */
	private MainFrame parent;
	
	/**
	 * The list of players.
	 */
	private List<Player> players;
	
	/**
	 * The list of current priorities.
	 */
	private List<Integer> currentPriorities;
	
	/**
	 * The list of upcoming priorities.
	 */
	private List<Integer> upcomingPriorities;
	
	/**
	 * The ID of the this player.
	 */
	private int playerId;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 */
	public PlayerPanel(MainFrame parent)
	{
		this.parent = parent;
		this.setPreferredSize(new Dimension(240, 100));    // To set some width

		TitledBorder b = new TitledBorder("Players");
		b.setTitleColor(Color.DARK_GRAY);
		this.setBorder(b);
	}

	/**
	 * Sets the ID of the player.
	 * 
	 * @param playerId
	 */
	public void setPlayerId(int playerId)
	{
		this.playerId = playerId;
	}

	/**
	 * Returns the ID of the player.
	 * 
	 * @return
	 */
	public int getPlayerId()
	{
		return this.playerId;
	}

	/**
	 * Sets the players.
	 * 
	 * @param players
	 */
	public void setPlayers(List<Player> players)
	{
		this.players = players;
	}

	/**
	 * Renders the player list
	 *
	 * @param graphics canvas
	 */
	@Override
	public void paintComponent(Graphics graphics)
	{
		configureFont(graphics);
		cleanPlayerList(graphics);

		// Check if there are any players
		if (players == null || (players != null && players.isEmpty())) {
			graphics.setColor(Color.BLACK);
			graphics.drawString("Waiting for Players:", 10, 50);
			return;
		}

		// Player List
		graphics.setColor(Color.BLACK);

		// Render Priorities
		graphics.setColor(Color.BLACK);
		renderPriorities(graphics, 10, this.currentPriorities, "Priorities");
		renderPriorities(graphics, 120, this.upcomingPriorities, "Upcoming");
	}

	/**
	 * Renders the list of priorities.
	 * 
	 * @param graphics
	 * @param line
	 */
	private void renderPriorities(Graphics graphics, int xPos, List<Integer> priorities, String label)
	{
		int line = 4 * parent.getOffset();
		
		// Draw label
		graphics.drawString(label, xPos, line);
		line += parent.getOffset();

		String names[] = new String[priorities.size()];
		Color colors[] = new Color[priorities.size()];

		// Get player names and colors
		for (Player p : players) {
			int index = priorities.indexOf(p.getId());

			names[index] = p.getName();
			colors[index] = parent.getPlayerColor(p.getId());
		}

		// Render priorities
		for (int i = 0; i < names.length; i++) {
			graphics.setColor(colors[i]);
			graphics.drawString(names[i], xPos, line);
			line += parent.getOffset();
		}
	}

	/**
	 * Cleans the player list.
	 * 
	 * @param graphics
	 */
	private void cleanPlayerList(Graphics graphics)
	{
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	/**
	 * Configures the font used in the player panel.
	 * 
	 * @param graphics
	 */
	private void configureFont(Graphics graphics)
	{
		graphics.setFont(new Font ("Sans Serif", Font.BOLD , 11));
		graphics.setColor(Color.LIGHT_GRAY);
	}

	/**
	 * Sets the upcoming priorities.
	 * 
	 * @param upcomingPlayerPriorities
	 */
	public void setUpcomingPriorities(List<Integer> upcomingPlayerPriorities)
	{
		this.upcomingPriorities = new ArrayList<Integer>(upcomingPlayerPriorities);
	}

	/**
	 * Sets the current priorities.
	 * 
	 * @param playerPrios
	 */
	public void setCurrentPrio(List<Integer> playerPrios)
	{
		this.currentPriorities = new ArrayList<Integer>(playerPrios);
	}
}
