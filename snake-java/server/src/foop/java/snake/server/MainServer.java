package foop.java.snake.server;

import foop.java.snake.server.connection.*;

class MainServer
{
    public static void main(String[] args)
    {
        if (args.length < 1) {
            usage();
        }

        int port = Integer.parseInt(args[0]);

        TCPServer server = new TCPServer(port);
        server.start();
    }

    protected static void usage()
    {
        System.out.println("Usage: java -jar snake-server.jar PORT");
        System.exit(0);
    }
}