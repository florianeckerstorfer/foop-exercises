package foop.java.snake.common.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Manages TCP clients.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class TCPClientRegistry implements ClientRegistryInterface
{
	/**
	 * List of registered clients.
	 */
	protected HashMap<SocketAddress, TCPClient> clients = new HashMap<SocketAddress, TCPClient>();

	/**
	 * Returns a TCP client for the given address. Reuses an existing connection if one exists.
	 *
	 * @param address
	 * @return
	 */
	public ClientInterface getClient(SocketAddress address)
		throws UnknownHostException, IOException
	{
		if (clients.containsKey(address)) {
			return clients.get(address);
		}

		TCPClient client = new TCPClient(address);
		clients.put(address, client);

		return client;
	}
}
