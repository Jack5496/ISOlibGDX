package com.kilobolt.SimpleHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;

public class SimpleSettingsInput implements TextInputListener{

	private String state;
	
	public SimpleSettingsInput(String state){
		this.state = state;
	}
	
	public static void getInput(String key, String title, String value){
		SimpleSettingsInput listener = new SimpleSettingsInput(key);
		Gdx.input.getTextInput(listener, title, value);
	}
	
	@Override
	public void input(String text) {
		Settings.setSetting(state, text);
	}

	@Override
	public void canceled() {

	}
	

}