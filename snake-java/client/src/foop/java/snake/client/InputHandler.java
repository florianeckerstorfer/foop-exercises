package foop.java.snake.client;

import foop.java.snake.client.gui.MainFrame;
import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;
import foop.java.snake.common.tcp.TCPClient;

import java.io.IOException;

/**
 * Input handler.
 * 
 * @package   foop.java.snake.client
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapelle
 */
public class InputHandler
{
	/**
	 * The TCP client.
	 */
	private TCPClient client;
	
	/**
	 * The main frame.
	 */
	private MainFrame frame;

	/**
	 * Constructor.
	 * @param client
	 * @param frame
	 */
	public InputHandler(TCPClient client, MainFrame frame)
	{
		this.client = client;
		this.frame = frame;
	}

	/**
	 * Handle the given input character.
	 * 
	 * @param input
	 */
	public void handleInput(Keycode input)
	{
		try {
			client.sendMessage(new InputMessage(frame.getPlayerId(), input));
		} catch (IOException e) {
			System.err.println("InputHandler: Couldn't send keycode to server: " + e.getMessage());
		}
	}
}
