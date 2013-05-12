package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;

/**
 * Interface for message handlers.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public interface MessageHandlerInterface {
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException;
}
