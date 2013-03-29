package foop.java.snake.common.board;
import java.io.Serializable;
import foop.java.snake.common.board.SnakeHeadDirection;
/**
 * Board
 * Holds the current board, that is, which field is occupied by which player
 * A board consists of columns x rows fields. Each field codes a player-number and (what else, direction?)  
 * 
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class Board implements Serializable{

	private static final long serialVersionUID = 1;

	private Integer rows;
	private Integer columns;
	private Byte[][] board;
	
	/**
	 * Initializes a board with given dimensions
	 * @param rows number of rows
	 * @param comumns number of columns
	 */
	public Board(Integer rows, Integer columns) {
		this.setBoard(new Byte[columns][rows]);
		this.setColumns(columns);
		this.setRows(rows);
	}
	
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	public Integer getColumns() {
		return columns;
	}
	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	public Byte[][] getBoard() {
		return board;
	}
	public void setBoard(Byte[][] board) {
		this.board = board;
	}
	
	/**
	 * Returns the direction of the snake head at the given position. If no snake head is there it
	 * returns SnakeHeadDirection.noSnakeHead
	 * @param column column to be checked
	 * @param row row to be checked
	 * @return a byte value, which can be checked by class SnakeHeadDirection 
	 * @see SnakeHeadDirection
	 */	
	public byte getSnakeHeadDirection(Integer column, Integer row) {
		return (byte)(board[column][row]&(byte)0xF0);
	}
	
	/**
	 * Returns the player ID at a given position on the board
	 * @param column column to be checked
	 * @param row row to be checked
	 * @return true if on the given position is a snake-head, false otherwise
	 */
	public boolean isSnakeHead (Integer column, Integer row) {
		if ((board[column][row]&(byte)0xF0) == SnakeHeadDirection.noSnake)
			return false;
		return true;
	}

	/**
	 * Returns the player ID at a given position on the board
	 * @param column column to be checked
	 * @param row row to be checked
	 * @return true if on the given position is a snake-head, false otherwise
	 */
	public boolean isSnakeBody (Integer column, Integer row) {
		if((board[column][row]&(byte)0xF0) == SnakeHeadDirection.snakeBody)
			return true;
		return false;
	}
	
	/**
	 * Returns the player ID at a given position on the board
	 * @param column column to be checked
	 * @param row row to be checked
	 * @return Player number. "0" if no snake-part at this position
	 */
	public int getPlayerNumber(Integer column, Integer row) {
		return board[column][row]&(byte)0x0F;
	}
}