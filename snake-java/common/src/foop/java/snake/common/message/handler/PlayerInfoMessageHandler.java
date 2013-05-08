package foop.java.snake.common.message.handler;

import java.net.SocketAddress;
import java.util.Observable;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

public class PlayerInfoMessageHandler extends Observable implements MessageHandlerInterface {

	@Override
	public void handle(MessageInterface message, SocketAddress address)
			throws NoMessageHandlerFoundException {
		if (message.getType() != PlayerInfoMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a PlayerInfoMessage.");
        }
        setChanged();
        notifyObservers(message);
	}


}
