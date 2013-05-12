package foop.java.snake.common.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;

/**
 * Manages TCP clients.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class TCPClientRegistry {
	protected HashMap<SocketAddress, TCPClient> clients = new HashMap<SocketAddress, TCPClient>();

	/**
	 * Returns a TCP client for the given address. Reuses an existing connection if one exists.
	 *
	 * @param address
	 * @return
	 */
	public TCPClient getClient(SocketAddress address)
		throws UnknownHostException, IOException {
		if (clients.containsKey(address)) {
			return clients.get(address);
		}

		TCPClient client = new TCPClient(address);
		clients.put(address, client);

		return client;
	}
}
