package foop.java.snake.common.player;

import java.io.Serializable;
import java.net.SocketAddress;

import foop.java.snake.common.message.InputMessage;

/**
 * Represents a player.
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class Player implements Serializable
{
    protected String name;
    protected int id;
    protected SocketAddress address;
    protected InputMessage.Keycode keycode;
    protected Boolean AI;

    /**
     * Constructor.
     *
     * @param  name
     * @return
     */
    public Player(String name)
    {
        setName(name);
        this.AI=false;
    }

    public Player(String name, Boolean AI) {
        this.name = name;
        this.AI = AI;
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

	public InputMessage.Keycode getKeycode() {
        return keycode;
    }

    public void setKeycode(InputMessage.Keycode keycode) {
        this.keycode = keycode;
    }
    
    public Boolean isAI() {
    	return this.AI;
    }
}
