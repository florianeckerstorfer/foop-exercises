package foop.java.snake.common.message;

import java.io.Serializable;

/**
 * MessageInterface
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public interface MessageInterface extends Serializable
{
    /**
     * Returns the type of message.
     *
     * @return
     */
    public int getType();
}
