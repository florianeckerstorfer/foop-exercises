package foop.java.snake.common.tcp;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import foop.java.snake.common.message.MessageInterface;

/**
 * TCPClient
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class TCPClient
{
    protected Socket socket;
    protected ObjectOutputStream outputStream;
    protected SocketAddress address;

    public TCPClient(SocketAddress address)
        throws UnknownHostException, IOException
    {
    	this.address=address;
    	this.open();
    }

    /**
     * Sends a message to the server.
     * KaRo: to fullfill the accept() of the TCPServer there must be a reconnect
     * after each message is sent. senMessage automatically tries to open() and close()
     * the socket.
     *
     * @param message
     */
    public void sendMessage(MessageInterface message)
        throws IOException
    {
    	if (socket.isClosed()) {
    		open();
        }

        outputStream.writeObject(message);
        close();
    }

    public void open()
            throws UnknownHostException, IOException
    {
        System.out.println("TCPClient: Opening new socket to "+((InetSocketAddress)address).getAddress()+":"+((InetSocketAddress)address).getPort());
    	socket = new Socket();
        socket.connect(address);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public void close()
        throws IOException
    {
        System.out.println("TCPClient: Closing socket to "+((InetSocketAddress)socket.getRemoteSocketAddress()).getAddress()+":"+socket.getPort());
        outputStream.close();
        socket.close();
    }
}
