package foop.java.snake.client.gui;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.board.SnakeHeadDirection;

import javax.swing.*;
import java.awt.*;

/**
 * @package foop.java.snake.client.gui
 * @author  Alexander Duml
 * @author  Florian Eckerstorfer
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller  
 */
public class BoardPanel extends JPanel
{
	private static final long serialVersionUID = 5006542597815133815L;
	
	private MainFrame parent;
	private Board board;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 */
	public BoardPanel(MainFrame parent)
	{
		this.parent = parent;
	}

	/**
	 * Sets the board.
	 * 
	 * @param board
	 */
	public void setBoard(Board board)
	{
		this.board = board;
	}

	/**
	 * renders the board
	 *
	 * @param graphics canvas
	 */
	@Override
	public void paintComponent(Graphics graphics)
	{
		// If the server hasn't sent a board yet, print a waiting message.
		if (null == board) {
			graphics.setColor(Color.BLACK);
			graphics.drawString("Waiting for board", 100, 50);
			return;
		}

		cleanBoard(graphics);
		renderGrid(graphics);
		renderSnakes(graphics);
	}

	/**
	 * Iterate through all snakes and render them on the board.
	 * 
	 * @param graphics
	 */
	private void renderSnakes(Graphics graphics)
	{
		byte[][] field = board.getBoard();
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[i].length; j++) {
				renderSnakeCell(graphics, i, j);
			}
		}
	}

	/**
	 * Render the grid background of the board.
	 * 
	 * @param graphics
	 */
	private void renderGrid(Graphics graphics)
	{
		graphics.setColor(Color.GRAY);
		for (int i = 0; i <= board.getColumns(); i++) {
			int x = i * parent.getOffset();
			int max = board.getColumns() * parent.getOffset();
			graphics.drawLine(x, 0, x, max);
			for (int j = 0; j <= board.getRows(); j++) {
				int y = j * parent.getOffset();
				graphics.drawLine(0, y, max, y);
			}
		}
	}

	/**
	 * Cleans the board.
	 * 
	 * @param graphics
	 */
	private void cleanBoard(Graphics graphics)
	{
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, board.getRows() * parent.getOffset(), board.getColumns() * parent.getOffset());
	}
	
	/**
	 * Renders a snake cell in the given row.
	 *
	 * @param graphics canvas
	 * @param column   column
	 * @param row      row
	 */
	private void renderSnakeCell(Graphics graphics, int column, int row)
	{
		int playerId = board.getPlayerNumber(column, row);
		graphics.setColor(parent.getPlayerColor(playerId));

		if (board.isSnakeBody(column, row)) {
			renderSnakeBodyCell(graphics, column, row);
		} else if (board.isSnakeHead(column, row)) {
			renderSnakeHeadCell(graphics, column, row);
		}
	}

	/**
	 * Renders the head of a snake into the given cell.
	 * 
	 * @param graphics
	 * @param column
	 * @param row
	 */
	private void renderSnakeHeadCell(Graphics graphics, int column, int row)
	{
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

	/**
	 * Renders a single body cell of a snake.
	 * 
	 * @param graphics
	 * @param column
	 * @param row
	 */
	private void renderSnakeBodyCell(Graphics graphics, int column, int row)
	{
		graphics.fillOval(column * parent.getOffset(), row * parent.getOffset(), parent.getOffset(), parent.getOffset());
	}
}
