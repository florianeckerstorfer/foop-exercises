package foop.java.snake.common.snake;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a dead snake.
 * 
 * Many of the base-methods are to be overrided as now there is no differentiation between head and body
 * Strictly spoken a dead snake has no head but only body parts...
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class DeadSnake implements ISnake
{
	private LinkedList<Point> snakeBody;

	public DeadSnake()
	{
		snakeBody = new LinkedList<Point>();
	}

	public void addBodyParts(List<Point> bodyParts)
	{
		for (Point part : bodyParts) {
			if (!snakeBody.contains(part)) {
				snakeBody.add(part);
			}
		}
	}

	@Override
	public int getId()
	{
		return 0;
	}

	@Override
	public List<Point> getSnakeBody()
	{
		return snakeBody;
	}

	@Override
	public List<Point> cut(Point position)
	{
		int cutIndex = snakeBody.indexOf(position);
		if (cutIndex == -1)
			return null;

		snakeBody.remove(cutIndex);
		return snakeBody;
	}

	@Override
	public SnakePart checkPosition(Point position) {
		if (snakeBody.contains(position))
			return SnakePart.BODY;
		return SnakePart.NONE;
	}

	@Override
	public Movement.Direction getCurrentDirection() {
		return Movement.Direction.NONE;
	}

	@Override
	public Point getHead() {
		return null;
	}

	@Override
	public void setId(int id) {}

	@Override
	public void move(Movement.Direction dir) {}

	@Override
	public void move() {}

	@Override
	public void setBoardSize(int sizeX, int sizeY) {}

	@Override
	public void setSize(int size, Movement.Direction dir) {}

	@Override
	public void setHeadPosition(Point position) {}

	@Override
	public void grow() {}

	@Override
	public boolean isAlive() {
		return false;
	}
}
