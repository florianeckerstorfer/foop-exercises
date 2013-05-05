package foop.java.snake.common.message;
import foop.java.snake.common.board.Board;
/**
 * BoardMessage
 * Sent from server to Clients to indicate chenges at the board. will be sent in regular time intervalls aka "ticks"
 * Content is the board to be displayed.
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class BoardMessage implements MessageInterface {
	private static final long serialVersionUID = 1;
    public static final int TYPE = 100;
    protected Board board;
    
    public BoardMessage (Board b) {
    	this.setBoard(b);
    }
    
    @Override
	public int getType() {
		return TYPE;
	}
    
    /**
     * @return the current board
     */
	public Board getBoard() {
		return board;
	}

    /**
     * @name board the current board to be sent
     */
	public void setBoard(Board board) {
		this.board=board;
	}
}
