package foop.java.snake.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import foop.java.snake.common.message.InputMessage;
import foop.java.snake.common.message.InputMessage.Keycode;

public class InputListener implements KeyListener {

	private Keycode lastKeyCode;
	
	public InputListener() {
		lastKeyCode=Keycode.IGNORE;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int currentKeyCode = arg0.getExtendedKeyCode();
		Keycode convertedKeyCode = convertKeycode(currentKeyCode);
		if(convertedKeyCode != Keycode.IGNORE) { 
			if(lastKeyCode!=convertedKeyCode) {
				lastKeyCode=convertedKeyCode;
				InputMessage message = new InputMessage(0,convertedKeyCode); // TODO correct player ID
				// TODO send message
			}
			System.out.println("Orig: "+currentKeyCode);
			System.out.println("Conv: "+convertedKeyCode);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	private Keycode convertKeycode(int extendedKeycode) {
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
			default:
				return Keycode.IGNORE;
		}
	}
	
}
