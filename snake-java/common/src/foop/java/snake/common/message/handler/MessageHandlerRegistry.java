package foop.java.snake.common.message.handler;

import java.util.HashMap;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.MessageInterface;

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
     * @throws NoMessageHandlerFoundException when no suitable handler is registered
     */
    public void handle(MessageInterface message)
        throws NoMessageHandlerFoundException
    {
        int type = message.getType();
        if (handlers.containsKey(type)) {
            handlers.get(type).handle(message);
        } else {
            throw new NoMessageHandlerFoundException();
        }
    }
}
