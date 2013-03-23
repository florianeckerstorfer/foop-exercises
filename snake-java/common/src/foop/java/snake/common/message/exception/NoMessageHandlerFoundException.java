package foop.java.snake.common.message.exception;

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
