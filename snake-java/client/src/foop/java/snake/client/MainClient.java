package foop.java.snake.client;

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
        if (args.length < 2) {
            usage();
        }

        String server = args[0];
        int serverPort = Integer.parseInt(args[1]);

        try {
            TCPClient client = new TCPClient(server, serverPort);
            client.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static void usage()
    {
        System.out.println("Usage: java -jar snake-client.jar SERVER SERVER_PORT");
        System.exit(0);
    }
}
