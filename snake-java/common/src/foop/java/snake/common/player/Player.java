package foop.java.snake.common.player;

import foop.java.snake.common.message.InputMessage;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 * Represents a player.
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class Player implements Serializable
{
    protected String name;
    protected int id;
    protected int priority;
    protected int nextPriority;
    protected SocketAddress address;
    protected InputMessage.Keycode keycode;

    /**
     * Constructor.
     *
     * @param  name
     * @return
     */
    public Player(String name)
    {
        setName(name);
    }

    public Player(String name, int id, int priority, int nextPriority) {
        this.name = name;
        this.id = id;
        this.priority = priority;
        this.nextPriority = nextPriority;
    }

    /**
     * Sets the name of the player.
     *
     * @param  name
     * @return
     */
    public Player setName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * Returns the name of the player.
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the ID of the player.
     *
     * @param  id
     * @return
     */
    public Player setId(int id)
    {
       	this.id = id;

        return this;
    }

    /**
     * Returns the name of the player.
     *
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * Sets the socket address of the player.
     *
     * @param  address
     * @return
     */
    public Player setAddress(SocketAddress address)
    {
        this.address = address;
        return this;
    }

    /**
     * Returns the socket address.
     *
     * @return
     */
    public SocketAddress getAddress()
    {
        return address;
    }

	/**
	 * @return the priority
	 */
    public int getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the nextPriority
	 */
	public int getNextPriority() {
		return nextPriority;
	}

	/**
	 * @param nextPriority the nextPriority to set
	 */
	public void setNextPriority(int nextPriority) {
		this.nextPriority = nextPriority;
	}

    public InputMessage.Keycode getKeycode() {
        return keycode;
    }

    public void setKeycode(InputMessage.Keycode keycode) {
        this.keycode = keycode;
    }
}
