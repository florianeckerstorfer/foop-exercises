package foop.java.snake.common.message.handler;

import java.net.SocketAddress;

import foop.java.snake.common.message.RegisterAckMessage;
import foop.java.snake.common.message.RegisterErrorMessage;
import foop.java.snake.common.message.UnregisterMessage;
import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.TCPClientRegistry;

/**
 * Handles messages sent by the client to the server to indicate that a player leaves the game.
 * Currently no ACK forseen...
 * 
 * @author Robert Kapeller <rkapeller@gmail.com>
 */
public class UnregisterMessageHandler implements MessageHandlerInterface {

    protected PlayerRegistry playerRegistry;
    protected TCPClientRegistry clientRegistry;

    public UnregisterMessageHandler(PlayerRegistry playerRegistry, TCPClientRegistry clientRegistry)
    {
        this.playerRegistry = playerRegistry;
        this.clientRegistry = clientRegistry;
    }

    @Override
	public void handle(MessageInterface message, SocketAddress address)
			throws NoMessageHandlerFoundException {

		if (message.getType() != UnregisterMessage.TYPE) {
            throw new NoMessageHandlerFoundException("This is not a UnregisterMessage.");
        }
		UnregisterMessage unregisterMessage = (UnregisterMessage)message;
        MessageInterface response;

        // message received, remove Player from player-registry
        String playerName=unregisterMessage.getPlayerName();
        System.out.println(playerName + " wants to unregister from the server.");
        
        if (playerRegistry.hasPlayerName(playerName)) {
        	playerRegistry.removesPlayer(playerRegistry.getPlayerByName(playerName));
        	
//            playerRegistry.addPlayer(new Player(unregisterMessage.getPlayerName()));
            System.out.println("Registered " + playerName);

        } else {
        	// player cannot be removed as it is not registerred...
        	// send Error-response 
            response = new RegisterErrorMessage("The name \"" + playerName + "\" is not registerred and therefore cannot be removed");
            System.out.println("Username \"" + playerName + "\" does not exist.");
            response = new RegisterAckMessage();
        }
        
	}

}
