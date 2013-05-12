package foop.java.snake.common.snake;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Living snake. Cannot be dead by itself that is a dead snake is one which is no longer present as an object
 * Be aware, that it is necssary to first set the Board size the snake will live on before actually setting anything on the snake
 *
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class Snake implements ISnake {
	private LinkedList<Point> snakeBody = new LinkedList<Point>(); // this will be the snake...
	private Direction currentDirection = Direction.NONE;
	private int boardX = 0;    // the world, the snake is living on
	private int boardY = 0;
	private int id = 0;
	private Point removedTail = null;

	/**
	 * creates a snake with the head at a given position, a given length and the body pointing to the given position
	 *
	 * @param position position of the head of the snake
	 * @param size     including head (size of smaller or equal to 1 means that only the head remains
	 * @param dir      direction the head points ( see {@link Direction})
	 */
	public Snake(int id, Point position, int size, Direction dir, int boardWidth, int boardHeight) {

		setId(id);
		setBoardSize(boardWidth, boardHeight);
		currentDirection = dir;
		snakeBody = new LinkedList<Point>();
		snakeBody.addFirst(new Point(this.getPosOnBoard(position)));
		// the head is already created ;-)
		this.setSize(size - 1, dir);
	}

	public Snake(int id, List<Point> bodyParts) {
		setId(id);
		snakeBody = new LinkedList<Point>(bodyParts);    // does this create Body-copies? Or only references?
		// TODO: check if body parts are all on board...
		// as we assume the board size is not changing I did not do this
	}

	/**
	 * Default constructor. Empty snake...
	 */
	private Snake() {
		// TODO...?
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public List<Point> getSnakeBody() {
		return snakeBody;
	}

	@Override
	public Point getHead() {
		return snakeBody.peekFirst() != null && this.checkPosition(snakeBody.getFirst()) == SnakePart.HEAD ?
			snakeBody.getFirst() : null;
	}

	@Override
	public List<Point> cut(Point position) {
		// if not in body or if head...
		if (!isInSnake(position))
			return null;

		if (position.equals(snakeBody.get(0))) {
			snakeBody.removeFirst();
			return snakeBody;
		}

		int cutIndex = snakeBody.indexOf(position);

		// create new body-part
		List<Point> toRet = new LinkedList<Point>();
		for (int i = cutIndex + 1; i <= snakeBody.size() - 1; i++) {
			toRet.add(snakeBody.get(i));
		}
		int countToRemove = snakeBody.size() - cutIndex;
		for (int i = 0; i < countToRemove; i++) {
			snakeBody.removeLast();
		}
		return toRet;
	}

	@Override
	public void move() {
		move(getCurrentDirection());
	}

	@Override
	public void move(Direction dir) {

		if (snakeBody.size() == 0)
			return;

		// compute new head position
		int x = snakeBody.peekFirst().x;
		int y = snakeBody.peekFirst().y;

		// to avoid useless movings...
		if (dir == Direction.NONE)
			dir = currentDirection;
		// the ifs are necessary to avoid self-"eating"
		switch (dir) {
			case UP:
				if (currentDirection != Direction.DOWN) {
					y--;
					currentDirection = dir;
				} else {
					y++;
				}
				break;
			case DOWN:
				if (currentDirection != Direction.UP) {
					y++;
					currentDirection = dir;
				} else {
					y--;
				}
				break;
			case LEFT:
				if (currentDirection != Direction.RIGHT) {
					x--;
					currentDirection = dir;
				} else {
					x++;
				}
				break;
			case RIGHT:
				if (currentDirection != Direction.LEFT) {
					x++;
					currentDirection = dir;
				} else {
					x--;
				}
				break;

			default:
		}
		Point newHeadPos = getPosOnBoard(x, y);

		snakeBody.addFirst(newHeadPos);
		// save removed tail
		removedTail = snakeBody.removeLast();
	}

	@Override
	public void setBoardSize(int sizeX, int sizeY) {
		this.boardX = sizeX;
		this.boardY = sizeY;
	}

	@Override
	public void setSize(int size, Direction dir) {
		// TODO untested
		int currentSize = snakeBody.size();
		if (size < currentSize) {
			this.cut(snakeBody.get(size)); // the remaining body we will not use anymore...
			return;
		}
		int dx = 0;
		int dy = 0;
		int tailX = snakeBody.peekLast().x;
		int tailY = snakeBody.peekLast().y;

		if (currentSize == 1) {
			switch (dir) {
				case UP:
					dy = 1;
					break;
				case DOWN:
					dy = -1;
					break;
				case LEFT:
					dx = 1;
					break;
				case RIGHT:
					dx = -1;
					break;
				default:
			}
		} else {
			// find out the dx and dy via the last two elements
			Point sacrum = snakeBody.get(currentSize - 2);
			dx = tailX - sacrum.x;
			dy = tailY - sacrum.y;
		}

		while (currentSize <= size--) {
			tailX = tailX + dx;
			tailY = tailY + dy;
			snakeBody.addLast(getPosOnBoard(new Point(tailX, tailY)));
		}

	}

	@Override
	public void setHeadposition(Point position) {
		if (snakeBody.size() == 0)
			return;
		Point currentHead = snakeBody.peekFirst();
		int diffX = position.x - currentHead.x;
		int diffY = position.y - currentHead.y;

		for (Point p : snakeBody) {
			p.translate(diffX, diffY);
			p.setLocation(getPosOnBoard(p));
		}
	}

	@Override
	public SnakePart checkPosition(Point position) {
		// TODO untested
		if (isInSnake(position)) {
			if (snakeBody.peekFirst().equals(position))
				return SnakePart.HEAD;
			else
				return SnakePart.BODY;
		}
		return SnakePart.NONE;
	}

	@Override
	public Direction getCurrentDirection() {
		return currentDirection;
	}

	/**
	 * True if point p is part of the snake (head or body)
	 *
	 * @param p
	 * @return
	 */
	protected boolean isInSnake(Point p) {
		return snakeBody.contains(p);
	}

	/**
	 * Computes the real position of the given coordinate on the board
	 * Acts if the board would be some kind of twofolded cylinder
	 *
	 * @param x x-coord to be checked
	 * @param y y-coord to be checked
	 * @return truncated position
	 */
	protected Point getPosOnBoard(int x, int y) {
		x = (x < 0) ? boardX + x : x % boardX;
		y = (y < 0) ? boardY + y : y % boardY;

		return new Point(x, y);
	}

	/**
	 * Computes the real position of the given coordinate on the board
	 * Acts if the board would be some kind of twofolded cylinder
	 *
	 * @param p Point to be checked
	 * @return
	 */
	protected Point getPosOnBoard(Point p) {
		return getPosOnBoard(p.x, p.y);
	}

	@Override
	public void grow() {
		if (removedTail != null) {
			snakeBody.add(removedTail);
		}
		removedTail = null;
	}
}
