package foop.java.snake.common.message.exception;

/**
 * NoMessageHandlerFoundException
 *
 * @package   foop.java.snake.common.message
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class NoMessageHandlerFoundException extends Exception
{
	private static final long serialVersionUID = 6269199539986003286L;

	public NoMessageHandlerFoundException()
	{
		super();
	}

	public NoMessageHandlerFoundException(String message)
	{
		super(message);
	}
}
