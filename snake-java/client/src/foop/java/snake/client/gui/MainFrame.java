package foop.java.snake.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.player.Player;

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
    private Color[] colors = new Color[] {
            Color.blue,
            Color.cyan,
            Color.yellow,
            Color.red,
            Color.green,
            Color.orange,
            Color.magenta
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
     * @param board board
     */
    public void renderBoard(Board board) {
        boardPanel.setBoard(board);
        boardPanel.repaint();
    }

    public void setMyID(int id) {
    	playerPanel.setMyID(id);
    }
    
    /**
     * takes a list of @see {Player}-objects and renders int on the playeröierarchy panel
     * @param id 
     * @param players List of Players
     */
    public void renderPlayers(List<Player> players) {
    	playerPanel.setPlayers(players);
    }

    /**
     * Sample method to show movement of the snakes
     * @param args
     */
    public static void main(String[] args) {
    	/*
        MainFrame mainFrame = new MainFrame();

        List<Player> pl = new ArrayList<Player>();
        mainFrame.renderPlayers(0, pl);

        Board board = new Board(30, 30);
        Byte[][] b = new Byte[][]{
                {0, 0, (byte)0x20, (byte)0x10, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x51, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0}
        };
        board.setBoard(b);
        mainFrame.renderBoard(board);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        b = new Byte[][]{
                {0, (byte)0x20, (byte)0x10, (byte)0x10, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, (byte)0x41},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        board.setBoard(b);
        mainFrame.renderBoard(board);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        b = new Byte[][]{
                {0, (byte)0x10, (byte)0x10, (byte)0x10, (byte)0x10, 0, 0, 0, 0},
                {0, (byte)0x30, 0, 0, (byte)0x10, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, (byte)0x11},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, (byte)0x31},
                {0, 0, 0, 0, 0, 0, 0, (byte)0x11, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        board.setBoard(b);
        mainFrame.renderBoard(board);
        */
    }

	public void renderPrios(List<Integer> playerPrios,
			List<Integer> nextPlayerPrios) {
		playerPanel.setCurrentPrio(playerPrios);
		playerPanel.setNextPrio(nextPlayerPrios);
		playerPanel.repaint();
	}
}
