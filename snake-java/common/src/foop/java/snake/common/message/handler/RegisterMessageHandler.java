package foop.java.snake.common.message.handler;

import java.net.SocketAddress;
import java.net.InetSocketAddress;

import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.message.*;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.tcp.TCPClientRegistry;
import foop.java.snake.common.tcp.TCPClient;

/**
 * Handles messages sent by the client to the server to register new players.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterMessageHandler implements MessageHandlerInterface
{
    protected PlayerRegistry playerRegistry;
    protected TCPClientRegistry clientRegistry;

    public RegisterMessageHandler(PlayerRegistry playerRegistry, TCPClientRegistry clientRegistry)
    {
        this.playerRegistry = playerRegistry;
        this.clientRegistry = clientRegistry;
    }

    /**
     * Handles a message sent by the client to the server to register a new player.
     *
     * @param  rawMessage
     * @param  address
     * @throws NoMessageHandlerFoundException when no handler can be found
     */
    public void handle(MessageInterface rawMessage, SocketAddress address)
        throws NoMessageHandlerFoundException
    {
        if (rawMessage.getType() != RegisterMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a RegisterMessage.");
        }
        RegisterMessage message = (RegisterMessage)rawMessage;

        System.out.println(message.getPlayerName() + " wants to register at the server.");

        MessageInterface response;

        if (playerRegistry.hasPlayerName(message.getPlayerName())) {
            response = new RegisterErrorMessage("The name \"" + message.getPlayerName() + "\" is already taken. Please choose another name.");
            System.out.println("Username \"" + message.getPlayerName() + "\" already exists.");
        } else {
            playerRegistry.addPlayer(new Player(message.getPlayerName()));
            System.out.println("Registered " + message.getPlayerName());
            response = new RegisterAckMessage();
        }

        SocketAddress newAddress = new InetSocketAddress(((InetSocketAddress)address).getHostName(), message.getPort());

        try {
            TCPClient client = clientRegistry.getClient(newAddress);
            client.sendMessage(response);
            client.close();
        } catch (Exception ex) {
            System.out.println("Couldn\'t send response to \"" + ((InetSocketAddress)newAddress).getHostName()+":"+((InetSocketAddress)newAddress).getPort() + "\".");
            System.out.println(ex.getMessage());
        }
    }
}
