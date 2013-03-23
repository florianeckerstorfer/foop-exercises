package foop.java.snake.common.message.handler;

import java.net.SocketAddress;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.MessageInterface;

/**
 * Interface for message handlers.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public interface MessageHandlerInterface
{
    public void handle(MessageInterface message, SocketAddress address)
        throws NoMessageHandlerFoundException;
}
