package foop.java.snake.common.message.handler;

import java.net.SocketAddress;
import java.util.Observable;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.RegisterAckMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

/**
 * Handles messages sent by the client to the server to register new players.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterAckMessageHandler  extends Observable implements MessageHandlerInterface
{
	int myID;

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
        if (rawMessage.getType() != RegisterAckMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a RegisterAckMessage.");
        }
        RegisterAckMessage message = (RegisterAckMessage)rawMessage;

        System.out.println("RegisterAckMessageHandler: Registration successful.");
        System.out.println("RegisterAckMessageHandler: We got the ID " + message.getPlayerID());
        this.myID=message.getPlayerID();

        // Implementation of the observer-pattern
        setChanged();
        notifyObservers(new Integer(message.getPlayerID()));
    }
}
