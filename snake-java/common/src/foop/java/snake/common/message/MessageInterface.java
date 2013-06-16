package foop.java.snake.common.message;

import java.io.Serializable;

/**
 * MessageInterface
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public interface MessageInterface extends Serializable
{
	/**
	 * Returns the type of the message.
	 * 
	 * @return The message type
	 */
	public int getType();
}
