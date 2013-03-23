package foop.java.snake.common.message.handler;

import java.util.HashMap;
import java.net.SocketAddress;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.MessageInterface;

/**
 * Handles messages.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class MessageHandlerRegistry implements MessageHandlerInterface
{
    protected HashMap<Integer, MessageHandlerInterface> handlers = new HashMap<Integer, MessageHandlerInterface>();

    /**
     * Registers a new message handler.
     *
     * @param type
     * @param handler
     */
    public void registerHandler(int type, MessageHandlerInterface handler)
    {
        handlers.put(type, handler);
    }

    /**
     * Sends the given message to the correct message handler.
     *
     * @param  message
     * @param  address
     * @throws NoMessageHandlerFoundException when no suitable handler is registered
     */
    public void handle(MessageInterface message, SocketAddress address)
        throws NoMessageHandlerFoundException
    {
        int type = message.getType();
        if (handlers.containsKey(type)) {
            handlers.get(type).handle(message, address);
        } else {
            throw new NoMessageHandlerFoundException();
        }
    }
}
