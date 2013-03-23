package foop.java.snake.common.player;

import java.net.SocketAddress;

/**
 * Represents a player.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class Player
{
    protected String name;
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
