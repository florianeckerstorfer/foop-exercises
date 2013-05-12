package foop.java.snake.common.snake;

import java.awt.*;
import java.util.List;

/**
 * Representing a dead body part
 * Many of the base-methods are to be overrided as now there is no differentiation between head and body
 * Strictly spoken a dead snake has no head but only body parts...
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class DeadSnake extends Snake {

	public DeadSnake(int id, List<Point> bodyParts) {
		super(id, bodyParts);
	}

	/**
	 * dead snakes do not move ;-)
	 */
	public void move(Direction dir, boolean grow) {
		// do nothing...
		return;
	}


}
