package foop.java.snake.common.message;

import java.util.List;

import foop.java.snake.common.player.Player;

public class PlayerInfoMessage implements MessageInterface {

	public static final int TYPE = 127;
	private List<Player> players;	
	
	public PlayerInfoMessage(List<Player> players) {
		this.players = players;
	}

	public int getType() {
		return TYPE;
	}

	public List<Player> getPlayers() {
		return this.players;
	}

}
