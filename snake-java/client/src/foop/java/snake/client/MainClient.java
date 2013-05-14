package foop.java.snake.client;

import foop.java.snake.client.gui.InputListener;
import foop.java.snake.client.gui.MainFrame;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.GameOverMessage;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.message.RegisterAckMessage;
import foop.java.snake.common.message.RegisterErrorMessage;
import foop.java.snake.common.message.RegisterMessage;
import foop.java.snake.common.message.UnregisterMessage;
import foop.java.snake.common.message.handler.BoardMessageHandler;
import foop.java.snake.common.message.handler.GameOverMessageHandler;
import foop.java.snake.common.message.handler.MessageHandlerRegistry;
import foop.java.snake.common.message.handler.PlayerInfoMessageHandler;
import foop.java.snake.common.message.handler.PrioChangeMessageHandler;
import foop.java.snake.common.message.handler.RegisterAckMessageHandler;
import foop.java.snake.common.message.handler.RegisterErrorMessageHandler;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPServer;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * MainClient
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
class MainClient {
	/**
	 * Client main method.
	 *
	 * @param args
	 * @return
	 */
	public static void main(String[] args) {
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
	protected static void usage() {
		System.out.println("Usage: java -jar snake-client.jar PLAYER_NAME PORT SERVER SERVER_PORT");
		System.exit(0);
	}

	private TCPClient client;
	private InputHandler inputHandler;

	private MainFrame mainFrame;
	private MessageObserver messageObserver;

	protected String playerName;
	protected int port;
	protected String server;
	protected int serverPort;

	/**
	 * Initializes the MainClient.
	 *
	 * @param args
	 * @return
	 */
	public MainClient(String[] args) {
		playerName = args[0];
		port = Integer.parseInt(args[1]);
		server = args[2];
		serverPort = Integer.parseInt(args[3]);

		mainFrame = new MainFrame();
		mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		messageObserver = new MessageObserver(mainFrame);
	}

	/**
	 * Exists the client if there was another problem.
	 *
	 * @param ex
	 */
	protected void exitWithError(Exception ex) {
		System.out.println("MainClient: Ouch! Something went wrong:\n" + ex.getMessage());
		System.exit(0);
	}

	/**
	 * Exists the client if there were problems with the connection.
	 *
	 * @param ex
	 */
	protected void exitWithError(IOException ex) {
		System.out.println("MainClient: There was something wrong with the connection to the server:");
		System.out.println(ex.getMessage());
		System.exit(0);
	}

	/**
	 * Exits the client if the given hostname is not valid.
	 *
	 * @param ex
	 */
	protected void exitWithError(UnknownHostException ex) {
		System.out.println("MainClient: Could not find \"" + server + ":" + serverPort + "\".");
		System.exit(0);
	}

	/**
	 * Registers all the different handlers
	 *
	 * @param handlerRegistry registry to use
	 */
	public void registerHandlers(MessageHandlerRegistry handlerRegistry) {
		handlerRegistry.registerHandler(
			RegisterErrorMessage.TYPE, new RegisterErrorMessageHandler()
		);

		RegisterAckMessageHandler registerAckMessageHandler = new RegisterAckMessageHandler();
		try {
			registerAckMessageHandler.addObserver(messageObserver);
			handlerRegistry.registerHandler(
				RegisterAckMessage.TYPE, registerAckMessageHandler
			);
		} catch (NullPointerException ex) {
			exitWithError(ex);
		}

		BoardMessageHandler b = new BoardMessageHandler();
		try {
			b.addObserver(messageObserver);
			handlerRegistry.registerHandler(BoardMessage.TYPE, b);
		} catch (NullPointerException ex) {
			exitWithError(ex);
		}

		PrioChangeMessageHandler p = new PrioChangeMessageHandler();
		p.addObserver(messageObserver);
		handlerRegistry.registerHandler(
			PrioChangeMessage.TYPE, p
		);

		PlayerInfoMessageHandler pi = new PlayerInfoMessageHandler();
		pi.addObserver(messageObserver);
		handlerRegistry.registerHandler(PlayerInfoMessage.TYPE, pi);
		
		GameOverMessageHandler go = new GameOverMessageHandler();
		go.addObserver(messageObserver);
		handlerRegistry.registerHandler(GameOverMessage.TYPE, go);
	}

	/**
	 * Runs the client.
	 */
	public void run() {
		try {
			MessageHandlerRegistry messageHandlerRegistry = new MessageHandlerRegistry();

			registerHandlers(messageHandlerRegistry);

			TCPServer server = new TCPServer(port, messageHandlerRegistry);
			(new Thread(server)).start();
		} catch (Exception ex) {
			exitWithError(ex);
		}

		try {
			System.out.println("MainClient: Listen to port " + port);
			client = new TCPClient(new InetSocketAddress(server, serverPort));
			client.sendMessage(new RegisterMessage(playerName, port));

			inputHandler = new InputHandler(client, mainFrame);
			mainFrame.addKeyListener(new InputListener(inputHandler));
			mainFrame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					try {
						client.sendMessage(new UnregisterMessage(playerName));
						System.exit(0);    // closing all...
					} catch (Exception ex) {
						exitWithError(ex);
					}
				}
			});

		} catch (Exception ex) {
			exitWithError(ex);
		}
	}
}
