package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
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

    public void setBoard(Board board) {
        this.board = board;
    }

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
            int x = i * OFFSET;
            for (int j=0; j<field[i].length; j++) {
                int y = j * OFFSET;
                Byte value = field[i][j];
                if (value != 0) {
                    drawSnake(graphics, value, x, y);
                }
            }
        }
    }

    private void drawSnake(Graphics graphics, Byte value, int x, int y) {
        graphics.setColor(getColor(value));
        if (isHead(value)) {
            graphics.fillRoundRect(x, y, OFFSET, OFFSET, OFFSET / 2, OFFSET / 2);
        } else {
            graphics.fillOval(x, y, OFFSET, OFFSET);
        }
    }

    private boolean isHead(Byte value) {
        if (value == 1) {
            return true;
        }
        return false;
    }

    private Color getColor(Byte value) {
        return Color.BLUE;
    }

}
