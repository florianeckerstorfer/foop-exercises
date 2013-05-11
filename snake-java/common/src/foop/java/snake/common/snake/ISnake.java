package foop.java.snake.common.snake;

import java.util.List;
import java.awt.*;

/**
 * Interface for a snake as we like them
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public interface ISnake {
// 
/**
	 * Enum to define movement-directions.
	 * Possible values are UP, DOWN, LEFT, RIGHT and NONE
	 *
	 */
	public enum Direction {
		UP, DOWN, LEFT, RIGHT, NONE
	}
	/**
	 * Enum to define snake pody parts
	 * Possible values are HEAD, BODY, NONE
	 *
	 */
	public enum SnakePart {
		HEAD, BODY, NONE
	}
	/**
	 * Each snake has an Id, which shall be unique
	 * @return
	 */
	public int getId();

	/**
	 * Each snake has an Id, which shall be unique
	 * @return
	 */
	public void setId(int id);
	
	/**
	 * Returns the snake body. The first element can be interpreted as the head of the snake
	 * @return body of the snake
	 */
	public List<Point> getSnakeBody();
	
	/**
	 * Cuts the snake at a given position. Returns the remaining (dead) body parts
	 * The given position is neither part of the remaining, nor part of the dead body but lost in time and space, 
	 * This snake is set all parts from head to cut-position -1
	 * @param position Where the snake is to be splitted
	 * @return List of dead (remaining) body parts. null if position is not part of the snake
	 */
	public List<Point> cut(Point position);
	
	/**
	 * Moves the snake in the given direction and if it shall grow...
	 * Sets the isGrowing flag.
	 * Sets the {@link Direction} flag.
	 * @param dir Direction the snake is moved
	 * @param grow If true the tail will not be cut of, that is, the snake will grow
	 */
	public void move(Direction dir, boolean grow);

	/**
	 * Moves the snake in the given direction
	 * Sets the {@link Direction} flag.
	 * It will or will not grow depending on the flag
	 * which is set by {@link setIsGrowning}	 
	 * @param dir Direction the snake is moved
	 * @param grow If true the tail will not be cut of, that is, the snake will grow
	 */
	public void move(Direction dir);
	/**
	 * Moves the snake towards the current direction and if it shall grow...
	 * Sets the isGrowing flag.
	 * @param dir Direction the snake is moved
	 * @param grow If true the tail will not be cut of, that is, the snake will grow
	 */
	public void move(boolean grow);

	/**
	 * Moves he snake into current direction. It will or will not grow depending on the flag
	 * which is set by {@link setIsGrowning}
	 */
	public void move();
	
	/**
	 * Sets the board size
	 * @param sizeX
	 * @param sizeY
	 */
	public void setBoardSize(int sizeX, int sizeY);
	/**
	 * Sets the size of the snake. 
	 * If the new size is smaller then the current one, the remaining elements are deleted. 
	 * If the size is larger than the current one, then elements are added in the direction the last
	 * two body parts are directing. 
	 * If only the head is there the direction parameter is used.
	 * If the size is  <=0 only the head is created as there are no ghost-snake ;-)
	 * @param size including head (size of smaller or equal to 1 means that only the head remains
	 * @param dir Direction the head points to
	 */
	public void setSize(int size, Direction dir);
	
	/**
	 * Sets the head of the snake to the given position.
	 * Body will move accordingly if already present
	 * @param position
	 */
	public void setHeadposition(Point position);
	
	/**
	 * Checks if the given position is occupied by the snake
	 * @param position
	 * @return what bodypart of the snake is occupying the position?
	 */
	public SnakePart checkPosition(Point position);
	
	/**
	 * Returns the direction the snake is currently heading
	 * @return {@link Direction}
	 */
	public Direction getCurrentDirection();
	
	/**
	 * Sets if the snake will grow during coming moves ({@link move})
	 * @param isGrowning
	 */
	public void setIsGrowning(boolean isGrowning);
	
	/**
	 * Is the snake currently growing?
	 * @return
	 */
	public boolean getIsGrowing();
	
}
