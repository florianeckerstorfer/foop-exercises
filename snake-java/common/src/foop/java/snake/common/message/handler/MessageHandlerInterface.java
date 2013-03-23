package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.MessageInterface;

public interface MessageHandlerInterface
{
    public void handle(MessageInterface message) throws NoMessageHandlerFoundException;
}
