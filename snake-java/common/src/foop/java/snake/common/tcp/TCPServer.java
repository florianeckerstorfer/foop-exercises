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
public class TCPServer implements Runnable
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
    public void run()
    {
        System.out.println("Start TCPServer at port " + port + ".");

        try {
            socket = new ServerSocket(port);

            while (true) {
                Socket connectionSocket = socket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());

                MessageInterface message = (MessageInterface)inputStream.readObject();
                try {
                    messageHandlerRegistry.handle(message, connectionSocket.getRemoteSocketAddress());
                } catch (NoMessageHandlerFoundException ex) {
                    System.out.println("Couldn\'t find message handler:\n> " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            exitWithError(ex);
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

    /**
     * Exits the client if the given hostname is not valid.
     *
     * @param ex
     */
    protected void exitWithError(UnknownHostException ex)
    {
        System.out.println("Unknown host:\n> "+ex.getMessage());
        System.exit(0);
    }

    /**
     * Exists the client if there were problems with the connection.
     *
     * @param ex
     */
    protected void exitWithError(IOException ex)
    {
        System.out.println("There was something wrong with the connection to the server:");
        System.out.println("> "+ex.getMessage());
        System.exit(0);
    }

    /**
     * Exists the client if there was another problem.
     *
     * @param ex
     */
    protected void exitWithError(Exception ex)
    {
        System.out.println("Ouch! Something went wrong:\n> " + ex.getMessage());
        System.exit(0);
    }
}