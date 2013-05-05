package foop.java.snake.client.gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:40
 */
public class BoardPanel extends JPanel{

    private MainFrame parent;
    private Board board;

    public BoardPanel(MainFrame parent) {
        this.parent = parent;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    /**
     * renders the board
     * @param graphics  canvas
     */
    @Override
    public void paintComponent(Graphics graphics) {
        if (board == null) {
            graphics.setColor(Color.BLACK);
            graphics.drawString("Waiting for board", 100, 50);
            return;
        }

        //clean board
        graphics.setColor(Color.LIGHT_GRAY);
        graphics.fillRect(0, 0, board.getRows() * parent.getOffset(), board.getColumns() * parent.getOffset());

        //render gird
        graphics.setColor(Color.GRAY);
        for (int i=0; i<= board.getColumns(); i++) {
            int x = i * parent.getOffset();
            int max = board.getColumns() * parent.getOffset();
            graphics.drawLine(x, 0, x, max);
            for (int j=0; j <= board.getRows(); j++) {
                int y = j * parent.getOffset();
                graphics.drawLine(0, y, max, y);
            }
        }

        //render snakes
        Byte[][] field = board.getBoard();
        for (int i=0; i<field.length; i++) {
            for (int j=0; j<field[i].length; j++) {
                drawSnake(graphics, i, j);
            }
        }
    }

    /**
     * draws the snake on the canvas
     * @param graphics  canvas
     * @param column    column
     * @param row       row
     */
    private void drawSnake(Graphics graphics, int column, int row) {
        int playerId = board.getPlayerNumber(column, row);
        graphics.setColor(parent.getPlayerColor(playerId));
        if (board.isSnakeBody(column, row)) {
            graphics.fillOval(column * parent.getOffset(), row * parent.getOffset(), parent.getOffset(), parent.getOffset());
        } else {
            if (board.isSnakeHead(column, row)) {
                byte snakeHead = board.getSnakeHeadDirection(column, row);
                if (snakeHead == SnakeHeadDirection.snakeHeadUp) {
                    graphics.fillArc(column * parent.getOffset(), row * parent.getOffset(), parent.getOffset(), parent.getOffset(), 120, 300);
                } else if (snakeHead == SnakeHeadDirection.snakeHeadDown) {
                    graphics.fillArc(column * parent.getOffset(), row * parent.getOffset(), parent.getOffset(), parent.getOffset(), 300, 300);
                } else if (snakeHead == SnakeHeadDirection.snakeHeadLeft) {
                    graphics.fillArc(column * parent.getOffset(), row * parent.getOffset(), parent.getOffset(), parent.getOffset(), 210, 300);
                } else if (snakeHead == SnakeHeadDirection.snakeHeadRight) {
                    graphics.fillArc(column * parent.getOffset(), row * parent.getOffset(), parent.getOffset(), parent.getOffset(), 30, 300);
                }
            }
        }
    }
}
