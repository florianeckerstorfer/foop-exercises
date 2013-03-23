package foop.java.snake.client;

import java.net.UnknownHostException;
import java.io.IOException;

import foop.java.snake.common.message.*;

import foop.java.snake.client.connection.*;

/**
 * MainClient
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
class MainClient
{
    /**
     * Client main method.
     *
     * @param  args
     * @return
     */
    public static void main(String[] args)
    {
        System.out.println("Hello Client!");
        if (args.length < 3) {
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
        System.out.println("Usage: java -jar snake-client.jar PLAYER_NAME SERVER SERVER_PORT");
        System.exit(0);
    }

    protected String playerName;
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
        server = args[1];
        serverPort = Integer.parseInt(args[2]);
    }

    /**
     * Runs the client.
     */
    public void run()
    {
        try {
            TCPClient client = new TCPClient(server, serverPort);
            client.sendMessage(new RegisterMessage(playerName));
            client.close();
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

    protected void exitWithError(Exception ex)
    {
        System.out.println("Ouch! Something went wrong:\n" + ex.getMessage());
        System.exit(0);
    }
}
