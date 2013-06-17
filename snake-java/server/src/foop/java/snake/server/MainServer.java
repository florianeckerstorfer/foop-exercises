package foop.java.snake.server;

import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.RegisterMessage;
import foop.java.snake.common.message.UnregisterMessage;
import foop.java.snake.common.message.handler.InputMessageHandler;
import foop.java.snake.common.message.handler.MessageHandlerRegistry;
import foop.java.snake.common.message.handler.RegisterMessageHandler;
import foop.java.snake.common.message.handler.UnregisterMessageHandler;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.TCPClientRegistry;
import foop.java.snake.common.tcp.TCPServer;
import foop.java.snake.server.gameloop.GameLoop;
import foop.java.snake.server.gameloop.MessageHandler;
import foop.java.snake.server.gameloop.PriorityManager;
import foop.java.snake.server.gameloop.ai.AiDirectionStrategyInterface;
import foop.java.snake.server.gameloop.ai.SimpleAiDirectionStrategy;
import foop.java.snake.server.gameloop.collisiondetection.CollisionDetectionStrategyInterface;
import foop.java.snake.server.gameloop.collisiondetection.SimpleCollisionDetectionStrategy;

import java.io.IOException;

/**
 * MainServer
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
class MainServer {
	public static void main(String[] args) {
		if (args.length < 1) {
			usage();
		}

		MainServer server = new MainServer(args);
		server.run();
	}

	/**
	 * Prints usage information.
	 *
	 * @return
	 */
	protected static void usage() {
		System.out.println("Usage: java -jar snake-server.jar PORT");
		System.exit(0);
	}

	protected int port;

	/**
	 * Initializes the server.
	 *
	 * @param args
	 * @return
	 */
	public MainServer(String[] args) {
		port = Integer.parseInt(args[0]);
	}

	/**
	 * Runs the server.
	 */
	public void run()
	{
		PlayerRegistry playerRegistry = new PlayerRegistry();
		TCPClientRegistry clientRegistry = new TCPClientRegistry();
		AiDirectionStrategyInterface aiDirectionStrategy = new SimpleAiDirectionStrategy();
		PriorityManager priorityManager = new PriorityManager();
		MessageHandler messageHandler = new MessageHandler(playerRegistry, clientRegistry);
		CollisionDetectionStrategyInterface collisionDetectionStrategy = new SimpleCollisionDetectionStrategy(
				priorityManager, messageHandler);
		
		GameLoop gameLoop = new GameLoop(playerRegistry, aiDirectionStrategy, priorityManager, 
				collisionDetectionStrategy, messageHandler);
		gameLoop.start();

		try {
			MessageHandlerRegistry messageHandlerRegistry = new MessageHandlerRegistry();
			messageHandlerRegistry.registerHandler(
				RegisterMessage.TYPE, new RegisterMessageHandler(playerRegistry, clientRegistry)
			);
			messageHandlerRegistry.registerHandler(
				UnregisterMessage.TYPE, new UnregisterMessageHandler(playerRegistry, clientRegistry)
			);

			InputMessageHandler i = new InputMessageHandler();
			messageHandlerRegistry.registerHandler(InputMessage.TYPE, i);
			i.addObserver(gameLoop);

			TCPServer server = new TCPServer(port, messageHandlerRegistry);
			(new Thread(server)).start();
		} catch (Exception ex) {
			exitWithError(ex);
		}
	}

	/**
	 * Exists the server with an IO error.
	 *
	 * @param ex
	 */
	protected void exitWithError(IOException ex) {
		System.out.println("MainServer: There was some error with the TCP connection:\n" + ex.getMessage());
		System.exit(0);
	}

	/**
	 * Exists the server when a message could not be understood.
	 *
	 * @param ex
	 */
	protected void exitWithError(ClassNotFoundException ex) {
		System.out.println("MainServer: Couldn't understand received message:\n" + ex.getMessage());
		System.exit(0);
	}

	/**
	 * Exists the server when some error occured.
	 *
	 * @param ex
	 */
	protected void exitWithError(Exception ex) {
		System.out.println("MainServer: Oh no! Something went wrong:\n" + ex.getMessage());
		System.exit(0);
	}
}
