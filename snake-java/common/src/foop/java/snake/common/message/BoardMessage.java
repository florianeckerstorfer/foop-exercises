package foop.java.snake.common.message;

import foop.java.snake.common.board.Board;

/**
 * BoardMessage.
 * 
 * Sent from server to Clients to indicate chenges at the board. will be sent in regular time intervalls aka "ticks"
 * Content is the board to be displayed.
 *
 * @package   foop.java.snake.common.message
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class BoardMessage implements MessageInterface
{
	private static final long serialVersionUID = 1;
	
	public static final int TYPE = 100;
	
	/**
	 * The board.
	 */
	protected Board board;

	/**
	 * Constructor.
	 * 
	 * @param b
	 */
	public BoardMessage(Board b)
	{
		this.setBoard(b);
	}

	@Override
	public int getType()
	{
		return TYPE;
	}

	/**
	 * @return The current board
	 */
	public Board getBoard()
	{
		return board;
	}

	/**
	 * @param board The board.
	 */
	public void setBoard(Board board)
	{
		this.board = board;
	}
}
