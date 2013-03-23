package foop.java.snake.server.connection;

import java.io.*;
import java.net.*;

public class TCPServer
{
    protected int port;
    protected ServerSocket socket;

    public TCPServer(int port)
    {
        this.port = port;
    }

    public void start() throws Exception
    {
        System.out.println("Start TCPServer");
        socket = new ServerSocket(port);

        while (true) {
            Socket connectionSocket = socket.accept();
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outputStream = new DataOutputStream(connectionSocket.getOutputStream());
            outputStream.writeBytes(inputStream.readLine() + '\n');
        }
    }

    public void shutdown() throws Exception
    {
        socket.close();
    }
}