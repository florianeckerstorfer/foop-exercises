package foop.java.snake.client.gui;

import foop.java.snake.client.InputHandler;
import foop.java.snake.common.message.InputMessage.Keycode;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * InputListener
 *
 * @package   foop.java.snake.client.gui
 * @author    Robert Kapeller<rkapeller@gmail.com>
 * @copyright 2013 Alexander Duml, Fabian Grünbichler, Florian Eckerstorfer, Robert Kapeller
 */
public class InputListener implements KeyListener
{
	/**
	 * The previously pressed key. 
	 */
	private Keycode lastKeyCode = Keycode.IGNORE;
	
	/**
	 * The input handler.
	 */
	private InputHandler inputHandler;

	/**
	 * Constructor
	 * 
	 * @param inputHandler
	 */
	public InputListener(InputHandler inputHandler)
	{
		this.inputHandler = inputHandler;
	}

	/**
	 * Key pressed event.
	 * 
	 * @param event
	 */
	@Override
	public void keyPressed(KeyEvent event)
	{
		Keycode convertedKeyCode = convertKeycode(event.getKeyCode());
		
		// Only handle when the user pressed a valid key
		if (convertedKeyCode != Keycode.IGNORE) {
			// Only react when the user pressed a different key
			if (lastKeyCode != convertedKeyCode) {
				lastKeyCode = convertedKeyCode;
				inputHandler.handleInput(lastKeyCode);
			}
			System.out.println("Pressed key: " + convertedKeyCode);
		}
	}

	/**
	 * Key released event. Do nothing.
	 * 
	 * @param event
	 */
	@Override
	public void keyReleased(KeyEvent event) {}

	/**
	 * Key typed event. Do nothing.
	 * 
	 * @param event
	 */
	@Override
	public void keyTyped(KeyEvent event) {}

	/**
	 * Convert the given key code into something more meaningful.
	 * 
	 * @param extendedKeycode
	 * @return
	 */
	private Keycode convertKeycode(int extendedKeycode)
	{
		switch (extendedKeycode) {
			case 65: // 'A'
			case 37: // Arrow Left
				return Keycode.LEFT;
			case 68: // 'D'
			case 39: // Arrow Right
				return Keycode.RIGHT;
			case 87: // 'W'
			case 38: // Arrow Up
				return Keycode.UP;
			case 83: // 'S'
			case 40: // Arrow Down
				return Keycode.DOWN;
			case 81: // 'Q for quit'
				return Keycode.QUIT;
			default:
				return Keycode.IGNORE;
		}
	}
}
