package foop.java.snake.client;

import java.io.IOException;

import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.tcp.TCPClient;

public class InputHandler {

	private TCPClient client;

	public InputHandler(TCPClient client) {
		this.client=client;
	}


	public void handleInput(Keycode input) {
		try {
			client.sendMessage(new InputMessage(0, input));
		} catch (IOException e) {
			System.err.println("InputHandler: Couldn't send keycode to server: "+e.getMessage());
		}
	}
}
