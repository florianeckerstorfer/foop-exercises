package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.UnregisterMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.ClientRegistryInterface;

import java.net.SocketAddress;

/**
 * Handles messages sent by the client to the server to indicate that a player leaves the game.
 * Currently no ACK forseen...
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Robert Kapeller <rkapeller@gmail.com>
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class UnregisterMessageHandler implements MessageHandlerInterface
{
	/**
	 * Player registry.
	 */
	protected PlayerRegistry playerRegistry;
	
	/**
	 * Client registry.
	 */
	protected ClientRegistryInterface clientRegistry;

	public UnregisterMessageHandler(PlayerRegistry playerRegistry, ClientRegistryInterface clientRegistry)
	{
		this.playerRegistry = playerRegistry;
		this.clientRegistry = clientRegistry;
	}

	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		if (message.getType() != UnregisterMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a UnregisterMessage.");
		}
		
		UnregisterMessage unregisterMessage = (UnregisterMessage) message;
		
		// message received, remove Player from player-registry
		String playerName = unregisterMessage.getPlayerName();
		System.out.println("UnregisterMessageHandler: " + playerName + " wants to unregister from the server.");

		if (playerRegistry.hasPlayerName(playerName)) {
			playerRegistry.removesPlayer(playerRegistry.getPlayerByName(playerName));

			System.out.println("UnregisterMessageHandler: Registered " + playerName);
		} else {
			// player cannot be removed as it is not registerred...
			// In that case we cannot send an error-response and we do nothing...
			System.out.println("UnregisterMessageHandler: Username \"" + playerName + "\" does not exist.");
		}
	}

}
