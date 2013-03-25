package foop.java.snake.client;

import java.net.UnknownHostException;
import java.net.InetSocketAddress;
import java.io.IOException;

import foop.java.snake.common.message.*;
import foop.java.snake.common.message.handler.*;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPServer;

/**
 * MainClient
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
class MainClient
{
	
	private TCPClient client;
	
    /**
     * Client main method.
     *
     * @param  args
     * @return
     */
    public static void main(String[] args)
    {
        System.out.println("Hello Client!");
        if (args.length < 4) {
            usage();
        }

        MainClient client = new MainClient(args);
        client.run();
    }

    /**
     * Prints usage instructions to a CLI.
     *
     * @return
     */
    protected static void usage()
    {
        System.out.println("Usage: java -jar snake-client.jar PLAYER_NAME PORT SERVER SERVER_PORT");
        System.exit(0);
    }

    protected String playerName;
    protected int port;
    protected String server;
    protected int serverPort;

    /**
     * Initializes the MainClient.
     *
     * @param  args
     * @return
     */
    public MainClient(String[] args)
    {
        playerName = args[0];
        port = Integer.parseInt(args[1]);
        server = args[2];
        serverPort = Integer.parseInt(args[3]);
    }

    /**
     * Runs the client.
     */
    public void run()
    {
        try {
            MessageHandlerRegistry messageHandlerRegistry = new MessageHandlerRegistry();
            messageHandlerRegistry.registerHandler(
                RegisterErrorMessage.TYPE, new RegisterErrorMessageHandler()
            );
            messageHandlerRegistry.registerHandler(
                RegisterAckMessage.TYPE, new RegisterAckMessageHandler()
            );

            TCPServer server = new TCPServer(port, messageHandlerRegistry);
            (new Thread(server)).start();
        } catch (Exception ex) {
            exitWithError(ex);
        }

        try {
            System.out.println("listen to port " + port);
            client = new TCPClient(new InetSocketAddress(server, serverPort));
            client.sendMessage(new RegisterMessage(playerName, port));
        } catch (Exception ex) {
            exitWithError(ex);
        }
    }

    /**
     * Exits the client if the given hostname is not valid.
     *
     * @param ex
     */
    protected void exitWithError(UnknownHostException ex)
    {
        System.out.println("Could not find \"" + server + ":" + serverPort + "\".");
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
        System.out.println(ex.getMessage());
        System.exit(0);
    }

    /**
     * Exists the client if there was another problem.
     *
     * @param ex
     */
    protected void exitWithError(Exception ex)
    {
        System.out.println("Ouch! Something went wrong:\n" + ex.getMessage());
        System.exit(0);
    }
}
