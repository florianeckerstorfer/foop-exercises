package foop.java.snake.common.message;

/**
 * RegisterMessage
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterMessage implements MessageInterface
{
    public static final long serialVersionUID = 0;
    public static final int TYPE = 1;

    protected String playerName;

    public RegisterMessage(String playerName)
    {
        this.playerName = playerName;
    }

    public String getPlayerName()
    {
        return playerName;
    }

    public int getType()
    {
        return TYPE;
    }
}
