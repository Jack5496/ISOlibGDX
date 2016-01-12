package com.kilobolt.Menus;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleElement;

public class SinglePlayerMenu extends Menu{
	
	SimpleButton spMenuNewButton;
	SimpleButton spMenuLoadButton;
	SimpleButton spMenuSaveButton;
	SimpleButton mainButton;

	public enum MenuState {
		NEW, LOAD, SAVE, BACK;
	}
	
	private MenuState state;

	public SinglePlayerMenu(InputHandler inputHandler, int midPointX, int midPointY) {
		super(inputHandler, midPointX, midPointY);
		
		state = MenuState.NEW;
		initButtons(midPointX, midPointY);
	}
	
	private void initButtons(int midPointX, int midPointY){
		spMenuNewButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "NEW");
		spMenuNewButton.setIsPressed(true);
		
		spMenuLoadButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "LOAD");

		spMenuSaveButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (10f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "SAVE");

		mainButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "MAIN");

		super.MenuElements.add(spMenuNewButton);
		super.MenuElements.add(spMenuLoadButton);
		super.MenuElements.add(spMenuSaveButton);
		super.MenuElements.add(mainButton);
	}

	@Override
	public void switchToMenu() {
		handler.activMenu = this;
		touchDown(-1,-1);
		spMenuNewButton.setIsPressed(true);
		state = MenuState.NEW;
	}
	
	@Override
	public void switchBackToMenu(){
		switchToMenu();
	}

	@Override
	public void keyDown() {		
		if (handler.keys[Keys.DOWN]) {
			menuDown();
		}
		if (handler.keys[Keys.UP]) {
			menuUp();
		}
		if (handler.keys[Keys.ENTER]) {
			enterActivMenu();
		}
		if(handler.keys[Keys.ESCAPE]){
			handler.lastMenu = this;
			handler.mainMenuHandler.switchToMenu();
		}
	}
	
	public void touchDown(int screenX, int screenY){
		boolean buttonPressed = false;
		
		if(spMenuNewButton.isTouchDown(screenX, screenY)){
			state = MenuState.NEW;
			buttonPressed = true;
		}
		if(spMenuLoadButton.isTouchDown(screenX, screenY)){
			state = MenuState.LOAD;
			buttonPressed = true;
		}
		if(spMenuSaveButton.isTouchDown(screenX, screenY)){
			state = MenuState.SAVE;
			buttonPressed = true;
		}
		if(mainButton.isTouchDown(screenX, screenY)){
			state = MenuState.BACK;
			buttonPressed = true;
		}

		if(buttonPressed) enterActivMenu();
	}
	
	public void enterActivMenu(){
		switch (state) {
		case NEW:
			handler.getGameWorld().initWorld(50, 50);
			handler.lastMenu = this;
			handler.ingameMenuHandler.switchToMenu();
			break;
		case LOAD:
			handler.lastMenu = this;
			handler.loadMenuHandler.switchToMenu();
			break;
		case SAVE:
			handler.getGameWorld().getLevelManager().saveMap();
			break;
		case BACK:
			handler.lastMenu = this;
			handler.mainMenuHandler.switchToMenu();
			break;
		}
	}
	
	public void menuDown() {
		touchDown(-1,-1); 	//resetting all Buttons
		
		switch (state) {
		case NEW:
			spMenuLoadButton.setIsPressed(true);
			state = MenuState.LOAD;
			break;
		case LOAD:
			spMenuSaveButton.setIsPressed(true);
			state = MenuState.SAVE;
			break;
		case SAVE:
			mainButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case BACK:
			spMenuNewButton.setIsPressed(true);
			state = MenuState.NEW;
			break;
		}
	}

	public void menuUp() {
		touchDown(-1,-1);	//resetting all Buttons
		
		switch (state) {
		case NEW:
			mainButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case LOAD:
			spMenuNewButton.setIsPressed(true);
			state = MenuState.NEW;
			break;
		case SAVE:
			spMenuLoadButton.setIsPressed(true);
			state = MenuState.LOAD;
			break;
		case BACK:
			spMenuSaveButton.setIsPressed(true);
			state = MenuState.SAVE;
			break;
		}
	}
	
	@Override
	public void drawMenu(SpriteBatch batch){
		for (SimpleElement button : super.MenuElements) {
			button.draw(batch);
		}
	}

}
