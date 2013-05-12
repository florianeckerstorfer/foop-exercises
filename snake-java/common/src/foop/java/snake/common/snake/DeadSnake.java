package foop.java.snake.common.snake;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Representing a dead body part
 * Many of the base-methods are to be overrided as now there is no differentiation between head and body
 * Strictly spoken a dead snake has no head but only body parts...
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class DeadSnake implements ISnake {

	private LinkedList<Point> snakeBody;

	public DeadSnake() {
		snakeBody = new LinkedList<Point>();
	}

	public void addBodyParts(List<Point> bodyParts) {
		for (Point part : bodyParts) {
			if (!snakeBody.contains(part)) {
				snakeBody.add(part);
			}
		}
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public List<Point> getSnakeBody() {
		return snakeBody;
	}

	@Override
	public List<Point> cut(Point position) {
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
	public Direction getCurrentDirection() {
		return Direction.NONE;
	}

	@Override
	public Point getHead() {
		return null;
	}

	@Override
	public void setId(int id) {}

	@Override
	public void move(Direction dir) {}

	@Override
	public void move() {}

	@Override
	public void setBoardSize(int sizeX, int sizeY) {}

	@Override
	public void setSize(int size, Direction dir) {}

	@Override
	public void setHeadPosition(Point position) {}

	@Override
	public void grow() {}

	@Override
	public boolean isAlive() {
		return false;
	}
}
