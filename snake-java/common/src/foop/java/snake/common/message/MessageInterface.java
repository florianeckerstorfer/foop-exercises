package foop.java.snake.common.message;

import java.io.Serializable;

/**
 * MessageInterface
 *
 * @author Florian Eckerstorfer <florian@eckerstorfer.co>
 */
public interface MessageInterface extends Serializable
{
    public int getType();
}
