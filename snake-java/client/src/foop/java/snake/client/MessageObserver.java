package foop.java.snake.client;

import foop.java.snake.client.gui.MainFrame;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.GameOverMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.message.RegisterAckMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * MessageObserver.
 * 
 * @package   foop.java.snake.client
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class MessageObserver implements Observer
{
	MainFrame frame;

	/**
	 * Constructor.
	 * 
	 * @param frame
	 */
	public MessageObserver(MainFrame frame)
	{
		this.frame = frame;
	}

	/**
	 * Observes incoming messages and updates the GUI.
	 * 
	 * @param o
	 * @param arg 
	 */
	@Override
	public void update(Observable o, Object arg)
	{
		MessageInterface message;
		
		if (arg instanceof MessageInterface) {
			message = (MessageInterface) arg;
		} else {
			return;
		}

		// Handle messages based on the type.
		switch (message.getType()) {
			case (BoardMessage.TYPE):
				frame.renderBoard(((BoardMessage) message).getBoard());
				break;
			case (RegisterAckMessage.TYPE):
				frame.setPlayerId(((RegisterAckMessage) message).getPlayerID());
				break;
			case (PrioChangeMessage.TYPE):
				frame.renderPriorities(
					((PrioChangeMessage) message).getPlayerPrios(),
					((PrioChangeMessage) message).getNextPlayerPrios()
				);
				break;
			case (PlayerInfoMessage.TYPE):
				frame.renderPlayers(((PlayerInfoMessage) message).getPlayers());
				break;
			case (GameOverMessage.TYPE):
				// TODO what to do on game over?
				frame.gameOver(((GameOverMessage)message).getMessage());
				break;
			default:
				System.err.println("Received Message with unknown type: " + message.getType());
				return;
		}
	}
}

