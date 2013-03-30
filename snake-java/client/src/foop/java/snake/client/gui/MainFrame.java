package foop.java.snake.client.gui;

import foop.java.snake.client.InputListener;
import foop.java.snake.common.board.Board;
import javax.swing.*;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:33
 */
public class MainFrame extends JFrame {
    //panel where the board is rendered
    private BoardPanel boardPanel;

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
        boardPanel = new BoardPanel();
        boardPanel.setVisible(true);
        this.add(boardPanel);
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
        mainFrame.addKeyListener(new InputListener());
    }
}
