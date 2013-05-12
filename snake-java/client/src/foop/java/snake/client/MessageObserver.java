package foop.java.snake.client;

import foop.java.snake.client.gui.MainFrame;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.message.RegisterAckMessage;

import java.util.Observable;
import java.util.Observer;

public class MessageObserver implements Observer {

	MainFrame frame;

	public MessageObserver(MainFrame frame) {
		this.frame = frame;
	}

	/**
	 * we have been updated by the Observable --> do something
	 * unfortunately Observer is not generic. We have to check and cast
	 * Not so very nice but I do not really want to create gerneri Observer/Observable for this
	 */
	@Override
	public void update(Observable o, Object arg) {
		MessageInterface message;
		if (arg instanceof MessageInterface)
			message = (MessageInterface) arg;
		else
			return;

		switch (message.getType()) {
			case (BoardMessage.TYPE):
				frame.renderBoard(((BoardMessage) message).getBoard());
				break;
			case (RegisterAckMessage.TYPE):
				frame.setMyID(((RegisterAckMessage) message).getPlayerID());
				break;
			case (PrioChangeMessage.TYPE):
				frame.renderPrios(((PrioChangeMessage) message).getPlayerPrios(), ((PrioChangeMessage) message).getNextPlayerPrios());
				break;
			case (PlayerInfoMessage.TYPE):
				frame.renderPlayers(((PlayerInfoMessage) message).getPlayers());
				break;
			default:
				System.err.println("Received Message with unknown type: " + message.getType());
				return;
		}
	}
}
