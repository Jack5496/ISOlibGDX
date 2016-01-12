package com.kilobolt.Menus;

import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleElement;

public class MainMenu extends Menu {

	static String version = "1.2.13";
	
	public enum MenuState {
		SINGLEPLAYER, MULTIPLAYER, OPTIONS
	}

	private MenuState state;

	SimpleButton singleplayerButton;
	SimpleButton multiplayerButton;
	SimpleButton optionsButton;
	
	BitmapFont font;
	BitmapFontCache cache;
	
	public MainMenu(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);

		state = MenuState.SINGLEPLAYER;
		initButtons(midPointX, midPointY);
		initFonts();
	}
	
	float scale = .5f;
	
	
	private void initFonts(){
		font = new BitmapFont();
		cache = font.getCache();
		
		font.setColor(Color.BLACK);
		font.setScale(1 * scale, -1 * scale);
		cache.setText(version, 0, 0);
	}

	private void initButtons(float midPointX, float midPointY) {
		singleplayerButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "PLAY");
		singleplayerButton.setIsPressed(true);

		multiplayerButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (10f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "MULTI");

		optionsButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "OPTIONS");

		super.MenuElements.add(singleplayerButton);
		super.MenuElements.add(multiplayerButton);
		super.MenuElements.add(optionsButton);
	}

	@Override
	public void keyDown() {
		if (handler.keys[Keys.DOWN]) {
			menuDown();
		}
		if (handler.keys[Keys.UP]) {
			menuUp();
		}
		if (handler.keys[Keys.T]) {
			WarpController.getInstance().sendGameUpdate("Blöööp");
		}
		if (handler.keys[Keys.Z]) {
			WarpController.getInstance().sendChat("hallo");
		}
		if (handler.keys[Keys.ENTER]) {
			enterActivMenu();
		}
	}
	
	@Override
	public void switchToMenu(){
		handler.getGameWorld().initMenu();
		state = MenuState.SINGLEPLAYER;
		touchDown(-1,-1);
		singleplayerButton.setIsPressed(true);
		handler.activMenu = this;
	}
	
	@Override
	public void switchBackToMenu(){
		switchToMenu();
	}
	
	public void enterActivMenu(){
		switch (state) {
		case SINGLEPLAYER:
			handler.lastMenu = this;
			handler.singlePlayerHandler.switchToMenu();
			break;
		case MULTIPLAYER:
			handler.lastMenu = this;
			handler.multiPlayerMenuHandler.switchToMenu();
			break;
		case OPTIONS:
			handler.lastMenu = this;
			handler.optionsMenuHandler.switchToMenu();
			break;
		}
	}

	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (singleplayerButton.isTouchDown(screenX, screenY)) {
			state = MenuState.SINGLEPLAYER;
			buttonPressed = true;
		}
		if (multiplayerButton.isTouchDown(screenX, screenY)) {
			state = MenuState.MULTIPLAYER;
			buttonPressed = true;
		}
		if (optionsButton.isTouchDown(screenX, screenY)) {
			state = MenuState.OPTIONS;
			buttonPressed = true;
		}

		if (buttonPressed){
			enterActivMenu();
		}
	}

	public void menuDown() {
		touchDown(-1, -1); // resetting all Buttons

		switch (state) {
		case SINGLEPLAYER:
			multiplayerButton.setIsPressed(true);
			state = MenuState.MULTIPLAYER;
			break;
		case MULTIPLAYER:
			optionsButton.setIsPressed(true);
			state = MenuState.OPTIONS;
			break;
		case OPTIONS:
			singleplayerButton.setIsPressed(true);
			state = MenuState.SINGLEPLAYER;
			break;
		}
	}

	public void menuUp() {
		touchDown(-1, -1); // resetting all Buttons

		switch (state) {
		case SINGLEPLAYER:
			optionsButton.setIsPressed(true);
			state = MenuState.OPTIONS;
			break;
		case MULTIPLAYER:
			singleplayerButton.setIsPressed(true);
			state = MenuState.SINGLEPLAYER;
			break;
		case OPTIONS:
			multiplayerButton.setIsPressed(true);
			state = MenuState.MULTIPLAYER;
			break;
		}
	}
	
	@Override
	public void drawMenu(SpriteBatch batch){
		for (SimpleElement button : super.MenuElements) {
			button.draw(batch);
		}
		cache.draw(batch);
	}

}
