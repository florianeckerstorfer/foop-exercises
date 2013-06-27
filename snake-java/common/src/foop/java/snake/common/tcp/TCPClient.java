package foop.java.snake.common.tcp;

import foop.java.snake.common.message.MessageInterface;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

/**
 * TCPClient
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class TCPClient implements ClientInterface
{
	/**
	 * The socket
	 */
	protected Socket socket;
	
	/**
	 * The output stream
	 */
	protected ObjectOutputStream outputStream;
	
	/**
	 * The address
	 */
	protected SocketAddress address;

	/**
	 * Constructor.
	 * 
	 * @param address
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public TCPClient(SocketAddress address)
		throws UnknownHostException, IOException
	{
		this.address = address;
		this.open();
	}

	/**
	 * Sends a message to the server.
	 * 
	 * KaRo: to fullfill the accept() of the TCPServer there must be a reconnect
	 * after each message is sent. senMessage automatically tries to open() and close()
	 * the socket.
	 *
	 * @param message
	 */
	public void sendMessage(MessageInterface message) throws IOException
	{
		if (socket.isClosed()) {
			open();
		}

		outputStream.writeObject(message);
		close();
	}

	/**
	 * Opens a new socket.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void open() throws UnknownHostException, IOException
	{
		//System.out.println("TCPClient: Opening new socket to "+((InetSocketAddress)address).getAddress()+":"+((InetSocketAddress)address).getPort());
		socket = new Socket();
		socket.connect(address);
		outputStream = new ObjectOutputStream(socket.getOutputStream());
	}

	/**
	 * Closes an open socket.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		//System.out.println("TCPClient: Closing socket to "+((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress()+":"+socket.getPort());
		outputStream.close();
		socket.close();
	}
}
