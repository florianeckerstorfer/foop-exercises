package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.RegisterMessage;
import foop.java.snake.common.message.MessageInterface;

public class RegisterMessageHandler implements MessageHandlerInterface
{
    public void handle(MessageInterface message)
        throws NoMessageHandlerFoundException
    {
        if (!(message instanceof RegisterMessage)) {
            throw new NoMessageHandlerFoundException("This is not a RegisterMessage.");
        }
        RegisterMessage m = (RegisterMessage)message;
        System.out.println("Registered new player " + m.getPlayerName());
    }
}