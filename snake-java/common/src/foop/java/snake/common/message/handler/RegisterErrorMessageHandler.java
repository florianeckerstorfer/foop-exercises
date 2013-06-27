package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.RegisterErrorMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.Observable;

/**
 * Handles messages sent by the server to client to indicate an error in the registration process.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class RegisterErrorMessageHandler extends Observable implements MessageHandlerInterface
{
	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		if (message.getType() != RegisterErrorMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a RegisterErrorMessage.");
		}

		setChanged();
		notifyObservers(message);
	}
}
