package foop.java.snake.client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import foop.java.snake.common.player.Player;

/**
 * PlayerPanel
 * Used to draw information about the Players (color, priority...)
 * Remark: Probably better to incorporate this functionality into BoardPanel as there are
 * shared data like color
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class PlayerPanel  extends JPanel{
    private MainFrame parent;
	private List<Player> players;

	public PlayerPanel(MainFrame parent) {
        this.parent = parent;
        this.setPreferredSize(new Dimension(120, 100));	// To set some width

        TitledBorder b = new TitledBorder("Players");
        b.setTitleColor(Color.DARK_GRAY);
        this.setBorder(b);
	}

	public void setPlayers(List<Player> players) {
        this.players = players;
	}

    /**
     * renders the player list
     * @param graphics  canvas
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

        //current priorities
        Collections.sort(players, new Comparator<Player>(){
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.getPriority() == p2.getPriority())
                    return 0;
                return p1.getPriority() > p2.getPriority() ? -1 : 1;
            }
        });

        graphics.setColor(Color.BLACK);
        int line = 2 * parent.getOffset();
        graphics.drawString("Priorities", 10, line);
        line += parent.getOffset();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            graphics.setColor(parent.getPlayerColor(p.getId()));
            graphics.drawString(String.format("%s (%s)", p.getName(), p.getPriority()) , 10, line);
            line += parent.getOffset();
        }

        //next priorities
        Collections.sort(players, new Comparator<Player>(){
            @Override
            public int compare(Player p1, Player p2) {
                if(p1.getPriority() == p2.getPriority())
                    return 0;
                return p1.getNextPriority() > p2.getNextPriority() ? -1 : 1;
            }
        });

        graphics.setColor(Color.BLACK);
        line += parent.getOffset();
        graphics.drawString("Next priorities:", 10, line);
        line += parent.getOffset();
        it = players.iterator();
        while (it.hasNext()) {
            Player p = it.next();
            graphics.setColor(parent.getPlayerColor(p.getId()));
            graphics.drawString(String.format("%s (%s)", p.getName(), p.getNextPriority()) , 10, line);
            line += parent.getOffset();
        }
    }
}
