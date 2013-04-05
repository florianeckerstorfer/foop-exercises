package foop.java.snake.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import foop.java.snake.common.player.*;

import java.util.*;

/**
 * PlayerPanel
 * Used to draw information about the Players (color, priority...)
 * Remark: Probably better to incorporate this functionality into BoardPanel as there are
 * shared data like color
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class PlayerPanel  extends JPanel{
	private List<Player> players;
    private int OFFSET = 20;

	// TODO: just copied from BoardPanel, no good ... maybe better to incorporate whole functionality into BoardPanel
	private Color[] colors = new Color[] {
            Color.blue,
            Color.cyan,
            Color.yellow,
            Color.red,
            Color.green,
            Color.orange,
            Color.magenta
       };
	
	public PlayerPanel() {
		this.setPreferredSize(new Dimension(120,100));	// To set some width
        TitledBorder b = new TitledBorder("Players");
        b.setTitleColor(Color.DARK_GRAY);
		this.setBorder(b);
	}
	
	public void setPlayers(List<Player> players) {
        this.players=players;
	}
	
    /**
     * renders the board
     * @param graphics  canvas
     */
    @Override
    public void paintComponent(Graphics graphics) {

        if (players == null) {
            graphics.setColor(Color.BLACK);
            graphics.drawString("Waiting for Players", 10, 50);
            return;
        }
        // clean the player-list
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        Iterator<Player> it = players.iterator();
        int line = 20 + OFFSET;
        graphics.setFont(new Font("Monospaced",Font.BOLD,12));
        while (it.hasNext()) {
        	Player p =it.next();
        	String name = p.getName();
        	int prio = p.getId();
        	graphics.setColor(colors[prio % colors.length]);
        	graphics.drawString(name, 10, line);
            line=line+OFFSET;
        }
    }
}
