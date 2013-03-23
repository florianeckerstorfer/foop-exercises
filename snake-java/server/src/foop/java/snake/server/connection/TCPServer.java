package foop.java.snake.server.connection;

import java.io.*;
import java.net.*;

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

        while (true) {
            Socket connectionSocket = socket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(connectionSocket.getInputStream());

            Object obj = inputStream.readObject();
            System.out.println("Received object: " + obj.getClass().getName() + '\n');
        }
    }

    public void shutdown()
        throws IOException
    {
        socket.close();
    }
}