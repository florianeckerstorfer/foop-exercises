package foop.java.snake.common.message;

/**
 * Message sent by server to the client when an error occured during the registration.
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class RegisterErrorMessage implements MessageInterface
{
    public static final long serialVersionUID = 0;
    public static final int TYPE = 2;

    protected String message;

    public RegisterErrorMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }

    public int getType()
    {
        return TYPE;
    }
}
