package foop.java.snake.client.connection;

import java.io.*;
import java.net.*;

public class TCPClient
{
    protected Socket socket;

    public TCPClient(String server, int serverPort) throws Exception
    {
        socket = new Socket(server, serverPort);
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        BufferedReader inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputStream.writeBytes("Hello Server! How do you do?\n");
        System.out.println(inputStream.readLine());
    }

    public void close() throws Exception
    {
        socket.close();
    }
}
