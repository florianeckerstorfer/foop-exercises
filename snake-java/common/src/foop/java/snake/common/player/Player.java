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
    protected int priority; 
    protected int nextPriority; 
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

}
