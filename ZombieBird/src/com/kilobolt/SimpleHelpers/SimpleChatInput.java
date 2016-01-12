package com.kilobolt.SimpleHelpers;

import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Input.TextInputListener;

public class SimpleChatInput implements TextInputListener{
	
	@Override
	public void input(String text) {
		WarpController.getInstance().sendChat(text);
	}

	@Override
	public void canceled() {

	}
	

}