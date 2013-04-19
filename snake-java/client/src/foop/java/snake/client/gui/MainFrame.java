package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.player.*;

import javax.swing.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:33
 */
public class MainFrame extends JFrame implements Observer {
    //panel where the board is rendered
    private BoardPanel boardPanel;
    private PlayerPanel playerPanel;

    private int id = 0; //our Player-ID
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
     * we have been updated by the Observable --> do something
     * unfortunately Observer is not generic. We have to check and cast
     * Not so very nice but I do not really want to create gerneri Observer/Observable for this
     */
    @Override
    public void update(Observable o, Object arg) {
        // Now, this is the problem here. We still have to distinguish from which Observable this is comming...
        if (arg instanceof List<?>) {
            this.renderPlayers((List<Player>)arg);
        }
        if (arg instanceof Board) {
            this.renderBoard((Board)arg);
        }
        if (arg instanceof Integer) {
            this.id=((Integer)arg).intValue();
        }
    }

    /**
     * takes a board and renders int on the board panel
     * @param board board
     */
    public void renderBoard(Board board) {
        boardPanel.setBoard(board);
        boardPanel.repaint();
    }

    /**
     * takes a list of @see {Player}-objects and renders int on the playeröierarchy panel
     * @param players List of Players
     */
    public void renderPlayers(List<Player> players) {
    	playerPanel.setPlayers(players);
    	playerPanel.repaint();
    }

    /**
     * Sample method to show movement of the snakes
     * @param args
     */
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();

        List<Player> pl = new ArrayList<Player>();
        pl.add(new Player("Player 1", 0, 5, 7));
        pl.add(new Player("Player 2", 1, 1, 5));
        pl.add(new Player("Player 3", 2, 3, 1));
        pl.add(new Player("Player 4", 3, 6, 2));
        pl.add(new Player("Player 5", 4, 2, 3));
        mainFrame.renderPlayers(pl);

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
    }
}
