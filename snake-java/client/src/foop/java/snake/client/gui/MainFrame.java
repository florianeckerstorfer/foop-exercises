package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.player.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.util.*;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:33
 */
public class MainFrame extends JFrame implements Observer {
    //panel where the board is rendered
    private BoardPanel boardPanel;
    private PlayerPanel playerPanel;
    private int Id = 0;	//our Player-ID

    public MainFrame() {
        init();
        //set some frame properties
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setSize(600, 623);
        this.setVisible(true);
    }

    /**
     * initializes the frame
     */
    private void init() {
    	this.setLayout(new BorderLayout());
        boardPanel = new BoardPanel();
        playerPanel = new PlayerPanel();
        playerPanel.setVisible(true);
        boardPanel.setVisible(true);
        add(boardPanel, BorderLayout.CENTER);
        add(playerPanel, BorderLayout.EAST);
        
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
    		this.Id=((Integer)arg).intValue();    		
    	}
    }

    /**
     * takes a board and renders int on the board panel
     * @param board
     */
    public void renderBoard(Board board) {
        boardPanel.setBoard(board);
        boardPanel.repaint();
    }
    
    /**
     * takes a list of @see {Player}-objects and renders int on the playeröierarchy panel
     * @param List of Players
     */
    public void renderPlayers(List<Player> players) {
    	// TODO implement the method ;-)
    	playerPanel.setPlayers(players);
    	playerPanel.repaint();
    }

    /**
     * Sample method to show movement of the snake
     * @param args
     */
    public static void main(String[] args) {
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

        MainFrame mainFrame = new MainFrame();
        mainFrame.testPlayerPanel();
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
    /**
     * Testmethod just to test how the Player-List would be presented
     */
    private void testPlayerPanel() {
        
        // TODO Just a test...
        Player p1 = new Player("Player 1");
        p1.setID(1);
        Player p2 = new Player("Player 2");
        p2.setID(2);
        Player p3 = new Player("Player 3");
        p3.setID(3);
        Player p4 = new Player("Player 4");
        p4.setID(4);
        Player p5 = new Player("Player 5");
        p5.setID(5);
        List<Player> pl = new ArrayList<Player>();
        pl.add(p1);
        pl.add(p2);
        pl.add(p3);
        pl.add(p4);
        pl.add(p5);
        playerPanel.setPlayers(pl);
        playerPanel.repaint();
    	
    }
}
