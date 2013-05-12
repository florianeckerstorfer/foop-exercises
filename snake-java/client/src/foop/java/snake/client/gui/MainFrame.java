package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.player.Player;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:33
 */
public class MainFrame extends JFrame {
	//panel where the board is rendered
	private BoardPanel boardPanel;
	private PlayerPanel playerPanel;

	private int offset = 20;
	private Color[] colors = new Color[]{
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

	public MainFrame() {
		init();
		//set some frame properties
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("Snake");
		this.setSize(722, 623);
		this.setVisible(true);
	}

	/**
	 * initializes the frame
	 */
	private void init() {
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setLayout(new BorderLayout());
		boardPanel = new BoardPanel(this);
		playerPanel = new PlayerPanel(this);
		playerPanel.setVisible(true);
		boardPanel.setVisible(true);
		add(boardPanel, BorderLayout.CENTER);
		add(playerPanel, BorderLayout.EAST);

	}

	public Color getPlayerColor(int playerId) {
		return colors[playerId % colors.length];
	}

	public int getOffset() {
		return offset;
	}

	/**
	 * takes a board and renders int on the board panel
	 *
	 * @param board board
	 */
	public void renderBoard(Board board) {
		boardPanel.setBoard(board);
		boardPanel.repaint();
	}

	public void setMyID(int id) {
		playerPanel.setMyID(id);
	}

	public int getMyID() {
		return playerPanel.getMyID();
	}

	/**
	 * takes a list of @see {Player}-objects and renders int on the playerHierarchy panel
	 *
	 * @param players List of Players
	 */
	public void renderPlayers(List<Player> players) {
		playerPanel.setPlayers(players);
	}

	public void renderPrios(List<Integer> playerPrios, List<Integer> nextPlayerPrios) {
		playerPanel.setCurrentPrio(playerPrios);
		playerPanel.setNextPrio(nextPlayerPrios);
		playerPanel.repaint();
	}
}
