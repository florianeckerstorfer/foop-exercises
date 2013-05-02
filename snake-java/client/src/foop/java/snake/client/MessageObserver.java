package foop.java.snake.client;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import foop.java.snake.client.gui.MainFrame;
import foop.java.snake.common.board.Board;
import foop.java.snake.common.player.Player;

public class MessageObserver implements Observer {

	MainFrame frame;
    private int id = 0; //our Player-ID

	public MessageObserver (MainFrame frame) {
		this.frame = frame;
	}
	/**
     * we have been updated by the Observable --> do something
     * unfortunately Observer is not generic. We have to check and cast
     * Not so very nice but I do not really want to create gerneri Observer/Observable for this
     */
    @Override
    public void update(Observable o, Object arg) {
        // Now, this is the problem here. We still have to distinguish from which Observable this is comming...
        if (arg instanceof List<?>) {
            frame.renderPlayers(id, (List<Player>)arg);
        }
        if (arg instanceof Board) {
            frame.renderBoard((Board)arg);
        }
        if (arg instanceof Integer) {
            this.id=((Integer)arg).intValue();
        }
    }
}
