package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.Observable;

public class InputMessageHandler extends Observable implements MessageHandlerInterface {

	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException {

		if (message.getType() != InputMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a InputMessage.");
		}

		// TODO Message received - what now ;-)
		this.printInput((InputMessage) message);

		// Implementation of the observer-pattern
		setChanged();
		notifyObservers((InputMessage) message);
	}

	/**
	 * Dummy-method to test and print input
	 *
	 * @param m
	 */
	private void printInput(InputMessage m) {
		System.out.println("InputMessageHandler: Got new Input-Message.");
		System.out.print("InputMessageHandler: Following Key has been pressed by player " + m.getPlayerID() + ": ");
		Keycode key = m.getInput();

		switch (key) {
			case LEFT:
				System.out.println("LEFT");
				break;
			case RIGHT:
				System.out.println("RIGHT");
				break;
			case UP:
				System.out.println("UP");
				break;
			case DOWN:
				System.out.println("DOWN");
				break;
			case QUIT:
				System.out.println("QUIT");
				break;
			case IGNORE:
				System.out.println("IGNORE");
		}

		// TODO: store input in player object

	}

}
