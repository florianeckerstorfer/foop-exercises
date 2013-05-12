package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.RegisterErrorMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;

/**
 * Handles messages sent by the server to client to indicate an error in the registration process.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterErrorMessageHandler implements MessageHandlerInterface {
	public RegisterErrorMessageHandler() {
	}

	/**
	 * Handles a message sent by the client to the server to register a new player.
	 *
	 * @param rawMessage
	 * @param address
	 * @throws NoMessageHandlerFoundException when no handler can be found
	 */
	public void handle(MessageInterface rawMessage, SocketAddress address)
		throws NoMessageHandlerFoundException {
		if (rawMessage.getType() != RegisterErrorMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a RegisterErrorMessage.");
		}
		RegisterErrorMessage message = (RegisterErrorMessage) rawMessage;

		System.out.println("RegisterErrorMessageHandler: Registration on server failed with the following error:\n");
		System.out.println(message.getMessage());
	}
}
