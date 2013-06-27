package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.RegisterAckMessage;
import foop.java.snake.common.message.RegisterErrorMessage;
import foop.java.snake.common.message.RegisterMessage;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.ClientInterface;
import foop.java.snake.common.tcp.ClientRegistryInterface;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Handles messages sent by the client to the server to register new players.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class RegisterMessageHandler implements MessageHandlerInterface
{
	protected PlayerRegistry playerRegistry;
	protected ClientRegistryInterface clientRegistry;

	public RegisterMessageHandler(PlayerRegistry playerRegistry, ClientRegistryInterface clientRegistry)
	{
		this.playerRegistry = playerRegistry;
		this.clientRegistry = clientRegistry;
	}

	/**
	 * Handles a message sent by the client to the server to register a new player.
	 *
	 * @param rawMessage
	 * @param address
	 * @throws NoMessageHandlerFoundException when no handler can be found
	 */
	public void handle(MessageInterface rawMessage, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		if (rawMessage.getType() != RegisterMessage.TYPE) {
			throw new NoMessageHandlerFoundException("This is not a RegisterMessage.");
		}
		RegisterMessage message = (RegisterMessage) rawMessage;

		System.out.println("RegisterMessageHandler: " + message.getPlayerName() + " wants to register at the server.");

		MessageInterface response;

		InetSocketAddress newAddress = new InetSocketAddress(((InetSocketAddress) address).getHostName(), message.getPort());

		if (playerRegistry.hasPlayerName(message.getPlayerName())) {
			response = new RegisterErrorMessage("The name \"" + message.getPlayerName() + "\" is already taken. Please choose another name.");
			System.out.println("RegisterMessageHandler: Username \"" + message.getPlayerName() + "\" already exists.");
			return;
		} else {
			Player p = new Player(message.getPlayerName());
			p.setAddress(newAddress);
			playerRegistry.addPlayer(p);
			System.out.println("RegisterMessageHandler: Registered " + message.getPlayerName());
			response = new RegisterAckMessage(p.getId());
		}

		System.out.println("RegisterMessageHandler: address=" + newAddress.getHostName() + ";port=" + newAddress.getPort());

		try {
			ClientInterface client = clientRegistry.getClient(newAddress);
			client.sendMessage(response);
		} catch (Exception ex) {
			System.out.println("RegisterMessageHandler: Couldn\'t send response to \"" + ((InetSocketAddress) newAddress).getHostName() + ":" + ((InetSocketAddress) newAddress).getPort() + "\".");
			System.out.println(ex.getMessage());
		}
	}

}
