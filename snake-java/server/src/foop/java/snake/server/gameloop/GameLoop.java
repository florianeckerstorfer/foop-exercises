package foop.java.snake.server.gameloop;

import foop.java.snake.common.player.PlayerRegistry;

/**
 * GameLoop 
 * 
 * @author florian@eckerstorfer.co (Florian Eckerstorfer)
 */
public class GameLoop extends Thread
{
	/**
	 * The time that each loop takes
	 */
	final private int loopTime = 2000;
	
	/**
	 * The number of iterations until the game starts
	 */
	final private int initialGameCountdown = 10;
	
	/**
	 * The number of iterations until the priority changes
	 */
	final private int priorityChangeInterval = 5;
	
	/**
	 * Stores the palyer registry
	 */
	private PlayerRegistry playerRegistry;
	
	/**
	 * Constructor.
	 * 
	 * @param playerRegistry
	 */
	public GameLoop(PlayerRegistry playerRegistry)
	{
		this.playerRegistry = playerRegistry;
	}
	
	/**
	 * Threads that runs the game loop.
	 */
	public void run()
	{
		int loopCount = 0;
		int playerCount = 0;
		int gameCountdown = initialGameCountdown;
		
		while (true) {
			System.out.println("Game Loop #" + loopCount);
			
			if (0 == playerCount && 0 == playerRegistry.getPlayerCount()) {
				// No registered players
				System.out.println("No registered players. But I will wait until the end of times");
			} else if (0 == playerCount && playerRegistry.getPlayerCount() > 0) {
				// The first iteration that has registered players. Start the countdown
				playerCount = playerRegistry.getPlayerCount();
				System.out.println("Registered " + playerRegistry.getPlayerCount() + " players.");
			} else if (playerCount > 0 && 0 == playerRegistry.getPlayerCount()) {
				// In the previous iteration we had players in the loop, but there are all gone.
				// --> End (or reset) the game
				playerCount = 0;
				gameCountdown = initialGameCountdown;
				// TODO: End game
			} else if (gameCountdown > 0) {
				// We have registered players, but the countdown is still running down.
				System.out.println("Game starts in " + gameCountdown);
				gameCountdown--;
			} else {
				// TODO: Implement "real" game logic, movement, collision detection, etc
				// TODO: Send board to players
			}
			
			if ((loopCount % priorityChangeInterval) == 0) {
				// TODO: Send priority change to the players
			}
			
			try {
				Thread.sleep(loopTime);
			} catch (InterruptedException e) {
				System.out.println("Sleep interrupted! Game Loop #" + loopCount);
				e.printStackTrace();
			}
			loopCount++;
		}
    }
}
