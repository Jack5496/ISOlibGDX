package com.kilobolt.Inputs;

import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.kilobolt.SimpleHelpers.SimpleElement;

public class InputHandlerChat {

	private static InputHandler handler;

	public static int pointer;

	public static String message;

	private static String noMessage = "Enter you Message";

	public static int maxMessageLength = 128;

	public InputHandlerChat(InputHandler inputHandler, int midPointX,
			int midPointY, List<SimpleElement> loadMenuButtons) {
		handler = inputHandler;
		pointer = 0;
		message = noMessage;

//		SimpleTextInputListener listener = new SimpleTextInputListener();
//		Gdx.input.getTextInput(listener, "Dialog Title", "Initial Textfield Value");
	}

	public void escape() {
		// Back to whatever
	}

	public void sendMessage() {

		message = ""; // clear message
	}

	public void keyDown(int keycode) {
		if (handler.keys[Keys.ESCAPE]) {
			escape();
			return;
		}
		if (handler.keys[Keys.ENTER]) {
			if (!message.equals(noMessage)) {
				sendMessage();
			}
			return;
		}
		if (handler.keys[Keys.BACK]) {
			if (message.length() != 0) {
				message = message.substring(0, message.length() - 1);
			}
			return;
		}
		if (message.length() < maxMessageLength) {
			if(message.equals(noMessage)){
				message = "";
			}
			message = message + String.valueOf(keycode);
		}
	}

}
