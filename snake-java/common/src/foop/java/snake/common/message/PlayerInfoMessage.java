package foop.java.snake.common.message;

import foop.java.snake.common.player.Player;

import java.util.List;

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
