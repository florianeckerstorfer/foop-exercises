package foop.java.snake.common.snake;

import java.awt.*;
import java.util.List;

/**
 * Interface for a snake as we like them
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public interface ISnake
{
	/**
	 * Enum to define snake pody parts
	 * Possible values are HEAD, BODY, NONE
	 */
	public enum SnakePart {
		HEAD, BODY, NONE
	}

	/**
	 * Each snake has an Id, which shall be unique
	 *
	 * @return
	 */
	public int getId();

	/**
	 * Each snake has an Id, which shall be unique
	 *
	 * @return
	 */
	public void setId(int id);

	/**
	 * Returns the snake body. The first element can be interpreted as the head of the snake
	 *
	 * @return body of the snake
	 */
	public List<Point> getSnakeBody();

	/**
	 * Gets head of snake, if there's none return null.
	 * @return
	 */
	public Point getHead();

	/**
	 * Cuts the snake at a given position. Returns the remaining (dead) body parts
	 * The given position is neither part of the remaining, nor part of the dead body but lost in time and space,
	 * This snake is set all parts from head to cut-position -1
	 *
	 * @param position Where the snake is to be splitted
	 * @return List of dead (remaining) body parts. null if position is not part of the snake
	 */
	public List<Point> cut(Point position);

	/**
	 * Moves the snake in the given direction
	 * Sets the {@link Movement.Direction} flag.
	 *
	 * @param dir  Direction the snake is moved
	 */
	public void move(Movement.Direction dir);

	/**
	 * Moves he snake into current direction.
	 */
	public void move();

	/**
	 * Sets the board size
	 *
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
	 *
	 * @param size including head (size of smaller or equal to 1 means that only the head remains
	 * @param dir  Direction the head points to
	 */
	public void setSize(int size, Movement.Direction dir);

	/**
	 * Sets the head of the snake to the given position.
	 * Body will move accordingly if already present
	 *
	 * @param position
	 */
	public void setHeadPosition(Point position);

	/**
	 * Checks if the given position is occupied by the snake
	 *
	 * @param position
	 * @return what bodyPart of the snake is occupying the position?
	 */
	public SnakePart checkPosition(Point position);

	/**
	 * Returns the direction the snake is currently heading
	 *
	 * @return {@link Movement.Direction}
	 */
	public Movement.Direction getCurrentDirection();

	/**
	 * Appends the previously cut tail, if there's any.
	 */
	public void grow();

	/**
	 * Returns if snake is alive
	 * @return
	 */
	public boolean isAlive();
}
