package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;

/**
 * Interface for message handlers.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public interface MessageHandlerInterface
{
	/**
	 * Handles the given message.
	 * 
	 * @param message The received message
	 * @param address The address the message came from
	 * 
	 * @throws NoMessageHandlerFoundException
	 */
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException;
}
