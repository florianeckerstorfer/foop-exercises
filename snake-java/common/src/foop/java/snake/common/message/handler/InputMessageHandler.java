package foop.java.snake.common.message.handler;

import java.net.SocketAddress;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

public class InputMessageHandler implements MessageHandlerInterface {

	@Override
	public void handle(MessageInterface message, SocketAddress address)
			throws NoMessageHandlerFoundException {
		
		if (message.getType() != InputMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a InputMessage.");
        }

		// TODO Message received - what now ;-)
		this.printInput((InputMessage)message);
	}
	/**
	 * Dummy-method to test and print input
	 * @param m
	 */
	private void printInput(InputMessage m) {
		System.out.println("Got new Input-Message.");
		System.out.print("Following Key has been pressed: " );
		Keycode key = m.getInput();

		switch(key) {
			case LEFT:
				System.out.println("LEFT" );
			case RIGHT:
				System.out.println("RIGHT" );
			case UP:
				System.out.println("UP" );
			case DOWN:
				System.out.println("DOWN" );
			case IGNORE:
				System.out.println("IGNORE" );
		}
		
	}

}
