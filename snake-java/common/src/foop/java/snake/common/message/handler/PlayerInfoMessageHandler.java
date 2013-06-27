package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.Observable;

/**
 * PlayerInfoMessageHandler.
 * 
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class PlayerInfoMessageHandler extends Observable implements MessageHandlerInterface
{
	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		if (message.getType() != PlayerInfoMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a PlayerInfoMessage.");
		}
		
		setChanged();
		notifyObservers(message);
	}
}
