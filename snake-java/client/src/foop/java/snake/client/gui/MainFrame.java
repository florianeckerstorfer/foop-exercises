package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @package   foop.java.snake.client.gui
 * @author    Alexander Duml
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	/**
	 * The panel where the board is rendered
	 */
	private BoardPanel boardPanel;
	
	private PlayerPanel playerPanel;

	private int offset = 20;
	
	private Color[] colors = new Color[] {
		Color.gray,
		Color.black,
		Color.blue,
		Color.cyan,
		Color.yellow,
		Color.red,
		Color.green,
		Color.orange,
		Color.magenta,
		new Color(51, 102, 0),
		new Color(180, 17, 220),
		new Color(244, 104, 51),
		new Color(244, 190, 84),
		new Color(189, 229, 19)
	};

	/**
	 * Constructor.
	 * 
	 */
	public MainFrame()
	{
		init();
		//set some frame properties
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Snake");
		this.setSize(842, 633);
		this.setVisible(true);
	}

	/**
	 * Initializes the frame
	 */
	private void init()
	{
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout());
		boardPanel = new BoardPanel(this);
		playerPanel = new PlayerPanel(this);
		playerPanel.setVisible(true);
		boardPanel.setVisible(true);
		add(boardPanel, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.EAST);

	}

	/**
	 * Returns the color of the player with the given ID.
	 * 
	 * @param playerId
	 * @return
	 */
	public Color getPlayerColor(int playerId)
	{
		return colors[playerId % colors.length];
	}

	/**
	 * Returns the offset.
	 * 
	 * @return The offset
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Takes a board and renders it on the board panel.
	 *
	 * @param board board
	 */
	public void renderBoard(Board board)
	{
		boardPanel.setBoard(board);
		boardPanel.repaint();
	}

	/**
	 * Sets the player ID.
	 * 
	 * @param id
	 */
	public void setPlayerId(int id)
	{
		playerPanel.setPlayerId(id);
	}

	/**
	 * Returns the player ID.
	 * @return
	 */
	public int getPlayerId()
	{
		return playerPanel.getPlayerId();
	}

	/**
	 * Takes a list of Player-objects and renders them on the playerHierarchy panel
	 *
	 * @param players List of Players
	 */
	public void renderPlayers(List<Player> players)
	{
		playerPanel.setPlayers(players);
	}

	/**
	 * Renders the priorities.
	 * 
	 * @param playerPrios
	 * @param nextPlayerPrios
	 */
	public void renderPriorities(List<Integer> playerPrios, List<Integer> nextPlayerPrios)
	{
		playerPanel.setCurrentPrio(playerPrios);
		playerPanel.setUpcomingPriorities(nextPlayerPrios);
		playerPanel.repaint();
	}
	
	/**
	 * Display the game over message on the screen.
	 * 
	 * @param message
	 */
	public void gameOver(String message)
	{
		JOptionPane.showMessageDialog(this, message, "You are dead!", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Displays an error message on the screen.
	 * 
	 * @param message
	 */
	public void errorMessage(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
	}
}
