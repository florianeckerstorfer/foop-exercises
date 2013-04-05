package foop.java.snake.common.player;

import java.io.Serializable;
import java.net.SocketAddress;

/**
 * Represents a player.
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class Player implements Serializable
{
    protected String name;
    protected int Id;
    protected SocketAddress address;

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
     * @param  name
     * @return
     */
    public Player setID(int Id)
    {
       	this.Id = Id;
       
        return this;
    }

    /**
     * Returns the name of the player.
     *
     * @return
     */
    public int getId()
    {
        return Id;
    }

    /**
     * Sets the socket address of the player.
     *
     * @param  server
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
}
