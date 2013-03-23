package foop.java.snake.common.message.exception;

/**
 * NoMessageHandlerFoundException
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public class NoMessageHandlerFoundException extends Exception
{
    public NoMessageHandlerFoundException()
    {
        super();
    }

    public NoMessageHandlerFoundException(String message)
    {
        super(message);
    }
}
