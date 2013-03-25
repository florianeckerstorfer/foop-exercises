package foop.java.snake.client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputListener implements KeyListener {

	private int lastKeyCode;
	
	public InputListener() {
		lastKeyCode=-1;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int currentKeyCode = arg0.getExtendedKeyCode();
		if(lastKeyCode!=currentKeyCode) {
			lastKeyCode=currentKeyCode;
			//TODO convert keycode to keycode enum, create inputmessage and send
		}
		System.out.println(currentKeyCode);
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
