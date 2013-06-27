package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.Observable;

/**
 * Handles messages sent by the server to the client to indicate a change in player priorities.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class PrioChangeMessageHandler extends Observable implements MessageHandlerInterface
{
	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		if (message.getType() != PrioChangeMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a PrioChangedMessage.");
		}
		
		PrioChangeMessage prioMessage = (PrioChangeMessage) message;
		
		setChanged();
		notifyObservers(prioMessage);
	}
}
