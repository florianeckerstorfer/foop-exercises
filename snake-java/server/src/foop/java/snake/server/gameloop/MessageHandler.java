package foop.java.snake.server.gameloop;

import java.io.IOException;

import foop.java.snake.common.message.GameOverMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.TCPClient;
import foop.java.snake.common.tcp.TCPClientRegistry;

public class MessageHandler
{
	private PlayerRegistry playerRegistry;
	private TCPClientRegistry clientRegistry;

	public MessageHandler(PlayerRegistry playerRegistry, TCPClientRegistry clientRegistry)
	{
		this.playerRegistry = playerRegistry;
		this.clientRegistry = clientRegistry;
	}
	
	/**
	 * Sends a gameOver-Message to the given player
	 * 
	 * @param playerId
	 */
	public void sendGameOverMessage(int lostId, int winningId)
	{
		System.out.println("Sending messages to players");
		Player lostPlayer = playerRegistry.getPlayerById(lostId);
		
		// Don't send a game over message when the snake is computer-controlled
		if (lostPlayer.isAi()) {
			return;
		}
		
		// Send a GameOver message
		try {
			String message = playerRegistry.getPlayerById(winningId).getName();;
			TCPClient client = clientRegistry.getClient(lostPlayer.getAddress());
			client.sendMessage(new GameOverMessage("You lost!\n" + message + " killed you!"));
		} catch (IOException e) {
			System.out.println("Error while sending to " + lostPlayer.getName());
		}
	}
}
