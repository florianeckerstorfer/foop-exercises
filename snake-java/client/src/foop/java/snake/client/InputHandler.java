package foop.java.snake.client;

import foop.java.snake.client.gui.MainFrame;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.tcp.TCPClient;

import java.io.IOException;

public class InputHandler {

	private TCPClient client;
	private MainFrame frame;

	public InputHandler(TCPClient client, MainFrame frame) {
		this.client = client;
		this.frame = frame;
	}


	public void handleInput(Keycode input) {
		try {
			client.sendMessage(new InputMessage(frame.getMyID(), input));
		} catch (IOException e) {
			System.err.println("InputHandler: Couldn't send keycode to server: " + e.getMessage());
		}
	}
}
