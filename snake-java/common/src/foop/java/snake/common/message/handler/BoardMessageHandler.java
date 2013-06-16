package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.Observable;

/**
 * Handles messages sent by the server to the client to present new board-status.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class BoardMessageHandler extends Observable implements MessageHandlerInterface
{
	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException 
	{
		if (message.getType() != BoardMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a BoardMessage.");
		}
		BoardMessage boardMessage = (BoardMessage) message;

		System.out.println("BoardMessageHandler: Got Board-Message.");

		setChanged();
		notifyObservers(boardMessage);
	}
}
