package foop.java.snake.common.tcp;

import java.io.*;
import java.net.*;

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

    public TCPClient(SocketAddress address)
        throws UnknownHostException, IOException
    {
        socket = new Socket();
        socket.connect(address);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Sends a message to the server.
     *
     * @param message
     */
    public void sendMessage(MessageInterface message)
        throws IOException
    {
        outputStream.writeObject(message);
    }

    public void close()
        throws IOException
    {
        System.out.println("Closing output stream to client.");
        outputStream.close();
        System.out.println("Closing socket to client!");
        socket.close();
    }
}
