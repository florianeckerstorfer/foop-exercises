package foop.java.snake.common.message;

/**
 * Message sent by server to the client when the registration was successful.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterAckMessage implements MessageInterface
{
    public static final long serialVersionUID = 0;
    public static final int TYPE = 3;
    protected int playerID;
    
    public RegisterAckMessage()
    {
    }

    public int getType()
    {
        return TYPE;
    }
    
    public int getPlayerID() {
    	return playerID;
    }
    public void setPlayerID(int playerID) {
    	this.playerID=playerID;
    }
    
}
