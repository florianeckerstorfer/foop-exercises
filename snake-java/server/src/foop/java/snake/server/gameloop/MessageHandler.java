package foop.java.snake.server.gameloop;

import java.io.IOException;
import java.util.List;

import foop.java.snake.common.board.Board;
import foop.java.snake.common.message.BoardMessage;
import foop.java.snake.common.message.GameOverMessage;
import foop.java.snake.common.message.PlayerInfoMessage;
import foop.java.snake.common.message.PrioChangeMessage;
import foop.java.snake.common.player.Player;
import foop.java.snake.common.player.PlayerRegistry;
import foop.java.snake.common.tcp.ClientInterface;
import foop.java.snake.common.tcp.ClientRegistryInterface;

/**
 * MessageHandler
 *
 * @package   foop.java.snake.server.gameloop
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class MessageHandler
{
	/**
	 * Player registry.
	 */
	private PlayerRegistry playerRegistry;
	
	/**
	 * Client registry.
	 */
	private ClientRegistryInterface clientRegistry;

	public MessageHandler(PlayerRegistry playerRegistry, ClientRegistryInterface clientRegistry)
	{
		this.playerRegistry = playerRegistry;
		this.clientRegistry = clientRegistry;
	}
	
	/**
	 * Sends the {@see PlayerInfoMessage} to all players.
	 * 
	 */
	public void sendPlayerInfoMessage()
	{
		System.out.println("Sending initial player messages to players");
		List<Player> players = playerRegistry.getPlayers();
		for (Player player : players) {
			if (!player.isAi()) {
				try {
					ClientInterface client = clientRegistry.getClient(player.getAddress());
					client.sendMessage(new PlayerInfoMessage(players));
				} catch (IOException e) {
					System.out.println("Error while sending to " + player.getName());
					// Remove the player from the list of players
					players.remove(player);
				}
			}
		}
	}
	
	public void sendBoardMessage(Board board)
	{
		System.out.println("Sending board messages to players");
		for (Player player : playerRegistry.getPlayers()) {
			if (!player.isAi()) {
				try {
					ClientInterface client = clientRegistry.getClient(player.getAddress());
					client.sendMessage(new BoardMessage(board));
				} catch (IOException e) {
					System.out.println("Error while sending to " + player.getName());
				}
			}
		}
	}
	
	public void sendPrioChangeMessage(PriorityManager priorityManager)
	{
		System.out.println("Sending prio change messages to players");
		for (Player player : playerRegistry.getPlayers()) {
			if (!player.isAi()) {
				try {
					ClientInterface client = clientRegistry.getClient(player.getAddress());
					System.out.println("before sending: currPrios/nextPrios: " + priorityManager.getPriorities().size() + "/" + priorityManager.getUpcomingPriorities().size());
					client.sendMessage(new PrioChangeMessage(priorityManager.getPriorities(), priorityManager.getUpcomingPriorities()));
				} catch (IOException e) {
					System.out.println("Error while sending to " + player.getName());
				}
			}
		}
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
			ClientInterface client = clientRegistry.getClient(lostPlayer.getAddress());
			client.sendMessage(new GameOverMessage("You lost!\n" + message + " killed you!"));
		} catch (IOException e) {
			System.out.println("Error while sending to " + lostPlayer.getName());
		}
	}
}
