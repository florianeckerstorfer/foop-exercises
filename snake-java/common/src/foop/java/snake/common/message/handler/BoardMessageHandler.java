package foop.java.snake.common.message.handler;

import java.net.SocketAddress;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

/**
 * Handles messages sent by the server to the client to present new board-status.
 * 
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class BoardMessageHandler implements MessageHandlerInterface {

	@Override
	public void handle(MessageInterface message, SocketAddress address)
			throws NoMessageHandlerFoundException {
		if (message.getType() != BoardMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a BoardMessage.");
        }
		BoardMessage boardMessage = (BoardMessage)message;

		// TODO Message received - what now ;-)
        System.out.println("New Board State received.\n");

	}

}