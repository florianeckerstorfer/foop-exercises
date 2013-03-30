package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;

import javax.swing.*;
import java.awt.*;

/**
 * User: Alexander Duml
 * Date: 26.03.13
 * Time: 13:40
 */
public class BoardPanel extends JPanel{

    private Board board;
    private int OFFSET = 20;
    private Color[] colors = new Color[] {
        Color.blue,
        Color.cyan,
        Color.yellow,
        Color.red,
        Color.green,
        Color.orange,
        Color.magenta
    };

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
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, board.getRows() * OFFSET, board.getColumns() * OFFSET);

        //render gird
        graphics.setColor(Color.GRAY);
        for (int i=0; i<= board.getColumns(); i++) {
            int x = i * OFFSET;
            int max = board.getColumns() * OFFSET;
            graphics.drawLine(x, 0, x, max);
            for (int j=0; j <= board.getRows(); j++) {
                int y = j * OFFSET;
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
        int player = board.getPlayerNumber(column, row);
        graphics.setColor(colors[player % colors.length]);
        if (board.isSnakeBody(column, row)) {
            graphics.fillOval(column * OFFSET, row * OFFSET, OFFSET, OFFSET);
        } else {
            if (board.isSnakeHead(column, row)) {
                byte snakeHead = board.getSnakeHeadDirection(column, row);
                if (snakeHead == SnakeHeadDirection.snakeHeadUp) {
                    graphics.fillArc(column * OFFSET, row * OFFSET, OFFSET, OFFSET, 120, 300);
                } else if (snakeHead == SnakeHeadDirection.snakeHeadDown) {
                    graphics.fillArc(column * OFFSET, row * OFFSET, OFFSET, OFFSET, 300, 300);
                } else if (snakeHead == SnakeHeadDirection.snakeHeadLeft) {
                    graphics.fillArc(column * OFFSET, row * OFFSET, OFFSET, OFFSET, 210, 300);
                } else if (snakeHead == SnakeHeadDirection.snakeHeadRight) {
                    graphics.fillArc(column * OFFSET, row * OFFSET, OFFSET, OFFSET, 30, 300);
                }
            }
        }
    }
}
