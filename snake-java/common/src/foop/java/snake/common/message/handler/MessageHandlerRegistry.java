package foop.java.snake.common.message.handler;

import foop.java.snake.common.message.MessageInterface;
import foop.java.snake.common.message.exception.NoMessageHandlerFoundException;

import java.net.SocketAddress;
import java.util.HashMap;

/**
 * Registry for message handlers.
 *
 * @package   foop.java.snake.common.message.handler
 * @author    Florian Eckerstorfer <florian@eckerstorfer.co>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class MessageHandlerRegistry implements MessageHandlerInterface
{
	/**
	 * The list of message handlers.
	 */
	protected HashMap<Integer, MessageHandlerInterface> handlers = new HashMap<Integer, MessageHandlerInterface>();

	/**
	 * Registers a new message handler.
	 *
	 * @param type
	 * @param handler
	 */
	public void registerHandler(int type, MessageHandlerInterface handler)
	{
		System.out.println("MessageHandlerRegistry: registerHandler: " + type);
		handlers.put(type, handler);
	}

	/**
	 * Sends the given message to the correct message handler.
	 *
	 * @param message
	 * @param address
	 * 
	 * @throws NoMessageHandlerFoundException when no suitable handler is registered
	 */
	@Override
	public void handle(MessageInterface message, SocketAddress address)
		throws NoMessageHandlerFoundException
	{
		int type = message.getType();
		
		if (handlers.containsKey(type)) {
			handlers.get(type).handle(message, address);
		} else {
			throw new NoMessageHandlerFoundException("Unkown message type \"" + type + "\".");
		}
	}
}
