package foop.java.snake.common.message.handler;

import java.net.SocketAddress;
import java.util.Observable;

import foop.java.snake.common.message.GameOverMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

/**
 * GameOverMessageHandler
 * 
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 *
 */
public class GameOverMessageHandler extends Observable implements
		MessageHandlerInterface 
{
	@Override
	public void handle(MessageInterface message, SocketAddress address)
			throws NoMessageHandlerFoundException
	{
		if (message.getType() != GameOverMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a GameOver-Message.");
		}

		setChanged();
		notifyObservers((GameOverMessage) message);
	}
}



