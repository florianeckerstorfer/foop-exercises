package foop.java.snake.common.board;
import java.io.Serializable;
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
	
	/*
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
}
