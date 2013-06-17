package foop.java.snake.common.tcp;

import java.io.IOException;
import java.net.UnknownHostException;

import foop.java.snake.common.message.MessageInterface;

/**
 * Interface for clients that can send messages to a server.
 * 
 * @package   foop.java.snake.common.tcp
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 
 */
public interface ClientInterface
{
	/**
	 * Sends a message to the server.
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(MessageInterface message) throws IOException;
	
	/**
	 * Opens a connection to the server.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void open() throws UnknownHostException, IOException;
	
	/**
	 * Closes the connection to the server.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException;
}
