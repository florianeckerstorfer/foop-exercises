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
        System.out.println("TCPServer: Start TCPServer at port " + port + ".");

        try {
            socket = new ServerSocket(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                Socket connectionSocket = socket.accept();
                System.out.println("TCPServer (port="+port+"): Accepted message");
                ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());

                MessageInterface message = (MessageInterface)inputStream.readObject();
                try {
                    System.out.println("TCPServer: got message of type " + message.getType());
                    messageHandlerRegistry.handle(message, connectionSocket.getRemoteSocketAddress());
                } catch (NoMessageHandlerFoundException ex) {
                    System.out.println("TCPServer: Couldn\'t find message handler:\n> " + ex.getMessage());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                exitWithError(ex);
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

    /**
     * Exits the client if the given hostname is not valid.
     *
     * @param ex
     */
    protected void exitWithError(UnknownHostException ex)
    {
        System.out.println("TCPServer: Unknown host:\n> "+ex.getMessage());
        System.exit(0);
    }

    /**
     * Exists the client if there were problems with the connection.
     *
     * @param ex
     */
    protected void exitWithError(IOException ex)
    {
        System.out.println("TCPServer: There was something wrong with the connection to the server:");
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
        System.out.println("TCPServer: Ouch! Something went wrong:\n> " + ex.getMessage());
        System.exit(0);
    }
}