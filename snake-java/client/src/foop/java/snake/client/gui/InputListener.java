package foop.java.snake.client.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import foop.java.snake.client.InputHandler;
import foop.java.snake.common.message.InputMessage.Keycode;

/**
 * TCPServer
 *
 * @author Robert Kapeller<rkapeller@gmail.com>
 */
public class InputListener implements KeyListener
{
	private Keycode lastKeyCode;
	private InputHandler inputHandler;

	public InputListener(InputHandler inputHandler)
	{
		this.inputHandler=inputHandler;
		lastKeyCode = Keycode.IGNORE;
	}

	@Override
	public void keyPressed(KeyEvent arg0)
	{
		// getExtendedKeyCode() is only available in Java 7. It is absolutley required?
		// int currentKeyCode = arg0.getExtendedKeyCode();
		int currentKeyCode = arg0.getKeyCode();

		Keycode convertedKeyCode = convertKeycode(currentKeyCode);
		if(convertedKeyCode != Keycode.IGNORE) {
			if(lastKeyCode != convertedKeyCode) {
				lastKeyCode = convertedKeyCode;
				inputHandler.handleInput(lastKeyCode);
			}
			System.out.println("Orig: " + currentKeyCode);
			System.out.println("Conv: " + convertedKeyCode);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0)
	{
		// TODO Auto-generated method stub
	}

	private Keycode convertKeycode(int extendedKeycode)
	{
		switch(extendedKeycode) {
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
