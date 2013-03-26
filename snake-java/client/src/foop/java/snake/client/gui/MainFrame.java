package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import javax.swing.*;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:33
 */
public class MainFrame extends JFrame {

    private BoardPanel boardPanel;

    public MainFrame() {
        init();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Snake");
        this.setSize(600, 623);
        this.setVisible(true);
    }

    private void init() {
        boardPanel = new BoardPanel();
        boardPanel.setVisible(true);
        this.add(boardPanel);
    }


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
        Byte[][] b = {
                {0, 0, 1, 2, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        board.setBoard(b);

        MainFrame mainFrame = new MainFrame();
        mainFrame.renderBoard(board);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Byte[][] b2 = {
                {0, 1, 2, 2, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        board.setBoard(b2);
        mainFrame.renderBoard(board);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        Byte[][] b3 = {
                {0, 2, 2, 2, 2, 0, 0, 0, 0},
                {0, 1, 0, 0, 2, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        board.setBoard(b3);
        mainFrame.renderBoard(board);
    }
}
