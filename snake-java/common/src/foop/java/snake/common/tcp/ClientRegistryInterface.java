package foop.java.snake.common.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * Interface for a registry of clients.
 *
 * @package   foop.java.snake.common.tcp
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 
 */
public interface ClientRegistryInterface
{
	/**
	 * Returns the client for the given address or creates a new client if it doesn't exist.
	 * 
	 * @param address
	 * 
	 * @return
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public ClientInterface getClient(SocketAddress address) throws UnknownHostException, IOException;
}
