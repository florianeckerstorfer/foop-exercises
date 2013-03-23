package foop.java.snake.common.tcp;

import java.io.*;
import java.net.*;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.*;
import foop.java.snake.common.message.handler.*;

/**
 * TCPServer
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class TCPServer
{
    protected int port;
    protected ServerSocket socket;
    protected MessageHandlerRegistry messageHandlerRegistry;

    public TCPServer(int port, MessageHandlerRegistry messageHandlerRegistry)
    {
        this.port = port;
        this.messageHandlerRegistry = messageHandlerRegistry;
    }

    /**
     * Starts the TCP server.
     *
     * @throws IOException            when an IO errors occurs
     * @throws ClassNotFoundException when a message can't be converted into an object
     */
    public void start()
        throws IOException, ClassNotFoundException
    {
        System.out.println("Start TCPServer");
        socket = new ServerSocket(port);

        while (true) {
            Socket connectionSocket = socket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());

            MessageInterface message = (MessageInterface)inputStream.readObject();
            try {
                messageHandlerRegistry.handle(message, connectionSocket.getRemoteSocketAddress());
            } catch (NoMessageHandlerFoundException ex) {
                System.out.println("Couldn\'t find message handler:\n" + ex.getMessage());
            }
        }
    }

    /**
     * Shuts the TCP server down.
     *
     * @throws IOException when an IO error occurs.
     */
    public void shutdown()
        throws IOException
    {
        socket.close();
    }
}