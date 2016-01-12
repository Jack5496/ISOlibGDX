package com.kilobolt.Menus;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.SimpleElement;

public class Menu {

	protected static InputHandler handler;
		
	public List<SimpleElement> MenuElements;
	
	public Menu(InputHandler inputHandler, int midPointX,
			int midPointY) {
		handler = inputHandler;
		MenuElements = new ArrayList<SimpleElement>();
	}
		
	public void switchToMenu(){
		handler.activMenu = this;
	}
	
	public void switchBackToMenu(){
		switchToMenu();
	}

	public void keyHold() {

	}
	
	public void keyDown() {

	}

	public void touchDown(int screenX, int screenY) {
	
	}
	
	public void enterActivMenu(){
		
	}
	
	public void drawMenu(SpriteBatch batcher){
		
	}
}
