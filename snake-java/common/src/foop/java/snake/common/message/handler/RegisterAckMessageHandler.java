package foop.java.snake.common.message.handler;

import java.net.SocketAddress;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.*;

/**
 * Handles messages sent by the client to the server to register new players.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterAckMessageHandler implements MessageHandlerInterface
{
    public RegisterAckMessageHandler()
    {
    }

    /**
     * Handles a message sent by the client to the server to register a new player.
     *
     * @param  rawMessage
     * @param  address
     * @throws NoMessageHandlerFoundException when no handler can be found
     */
    public void handle(MessageInterface rawMessage, SocketAddress address)
        throws NoMessageHandlerFoundException
    {
        if (!(rawMessage instanceof RegisterAckMessage)) {
            throw new NoMessageHandlerFoundException("This is not a RegisterAckMessage.");
        }
        RegisterAckMessage message = (RegisterAckMessage)rawMessage;

        System.out.println("Registration successful.\n");
    }
}
