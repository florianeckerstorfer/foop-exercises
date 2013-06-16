package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.RegisterAckMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.Observable;

/**
 * Handles messages sent by the client to the server to register new players.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class RegisterAckMessageHandler extends Observable implements MessageHandlerInterface
{
	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		if (message.getType() != RegisterAckMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a RegisterAckMessage.");
		}
		
		setChanged();
		notifyObservers(message);
	}
}
