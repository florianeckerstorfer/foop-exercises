package foop.java.snake.server.connection;

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

    public TCPServer(int port)
    {
        this.port = port;
    }

    public void start()
        throws IOException, ClassNotFoundException
    {
        System.out.println("Start TCPServer");
        socket = new ServerSocket(port);

        MessageHandlerRegistry messageHandlerRegistry = new MessageHandlerRegistry();
        messageHandlerRegistry.registerHandler(RegisterMessage.TYPE, new RegisterMessageHandler());

        while (true) {
            Socket connectionSocket = socket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());

            MessageInterface message = (MessageInterface)inputStream.readObject();
            try {
                messageHandlerRegistry.handle(message);
            } catch (NoMessageHandlerFoundException ex) {
                System.out.println("Couldn\'t find message handler:\n" + ex.getMessage());
            }
        }
    }

    public void shutdown()
        throws IOException
    {
        socket.close();
    }
}