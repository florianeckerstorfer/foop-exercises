package foop.java.snake.common.player;

import foop.java.snake.common.message.InputMessage;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 * Represents a player.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class Player implements Serializable
{
	private static final long serialVersionUID = -6423702100758766206L;
	
	protected String name;
	protected int id;
	protected SocketAddress address;
	protected InputMessage.Keycode keycode;
	protected Boolean isAi;

	/**
	 * Constructor.
	 *
	 * @param name
	 * @return
	 */
	public Player(String name) {
		setName(name);
		this.isAi = false;
	}

	/**
	 * Constructor, used to create an AI player.
	 * 
	 * @param name
	 * @param isAi
	 */
	public Player(String name, Boolean isAi) {
		this.name = name;
		this.isAi = isAi;
	}

	/**
	 * Sets the name of the player.
	 *
	 * @param name
	 * @return
	 */
	public Player setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Returns the name of the player.
	 *
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the ID of the player.
	 *
	 * @param id
	 * @return
	 */
	public Player setId(int id) {
		this.id = id;

		return this;
	}

	/**
	 * Returns the name of the player.
	 *
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the socket address of the player.
	 *
	 * @param address
	 * @return
	 */
	public Player setAddress(SocketAddress address) {
		this.address = address;
		return this;
	}

	/**
	 * Returns the socket address.
	 *
	 * @return
	 */
	public SocketAddress getAddress() {
		return address;
	}

	/**
	 * Returns the last pressed key by the player.
	 * 
	 * @return
	 */
	public InputMessage.Keycode getKeycode()
	{
		return keycode;
	}

	/**
	 * Sets the last pressed key by the player.
	 * 
	 * @param keycode
	 */
	public void setKeycode(InputMessage.Keycode keycode)
	{
		this.keycode = keycode;
	}

	/**
	 * Returns if the player is an AI player.
	 * 
	 * @return
	 */
	public Boolean isAi()
	{
		return this.isAi;
	}
}
