package foop.java.snake.client.connection;

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

    public TCPClient(String server, int serverPort)
        throws UnknownHostException, IOException
    {
        socket = new Socket(server, serverPort);
    }

    /**
     * Sends a message to the server.
     *
     * @param message
     */
    public void sendMessage(MessageInterface message)
        throws IOException
    {
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(message);
        outputStream.close();
    }

    public void close()
        throws IOException
    {
        socket.close();
    }
}
