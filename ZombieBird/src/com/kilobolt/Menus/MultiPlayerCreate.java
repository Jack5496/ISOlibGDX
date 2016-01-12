package com.kilobolt.Menus;

import java.util.ArrayList;
import java.util.List;

import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.Settings;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleSettingsInput;

public class MultiPlayerCreate extends Menu {

	static SimpleButton createButton;
	static SimpleButton nameButton;
	static SimpleButton amountPlayerButton;
	static SimpleButton morePlayerButton;
	static SimpleButton lessPlayerButton;
	static SimpleButton backButton;

	private int maxAmount = 10;

	private int playerAmount = 1;
	private String nameEnd = "s Room";

	public List<SimpleElement> MenuButtons;

	public enum MenuState {
		NAME, CREATE, BACK, AMOUNT;
	}

	private static MenuState state = MenuState.AMOUNT;
	
	private static MultiPlayerCreate instance;

	public static MultiPlayerCreate getInstance(){
		if(instance==null){
			instance = new MultiPlayerCreate(null,0,0);
		}
		return instance;
	}

	public MultiPlayerCreate(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);
		instance = this;

		initButtons(midPointX, midPointY);
	}

	public void initButtons(float midPointX, float midPointY) {

		MenuButtons = new ArrayList<SimpleElement>();

		nameButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "NAME");

		amountPlayerButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, ""
						+ playerAmount);

		morePlayerButton = new SimpleButton(midPointX * (12f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, ">");

		lessPlayerButton = new SimpleButton(midPointX * (4f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "<");

		createButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (10f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "CREATE");

		backButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "BACK");

		MenuButtons.add(createButton);
		MenuButtons.add(nameButton);
		MenuButtons.add(amountPlayerButton);
		MenuButtons.add(morePlayerButton);
		MenuButtons.add(lessPlayerButton);

		MenuButtons.add(backButton);
	}

	@Override
	public void keyDown() {
		if (handler.keys[Keys.UP]) {
			menuUp();
		}
		if (handler.keys[Keys.DOWN]) {
			menuDown();
		}
		if (handler.keys[Keys.RIGHT]) {
			amountPlayerUp();
			state = MenuState.AMOUNT;
		}
		if (handler.keys[Keys.LEFT]) {
			amountPlayerDown();
			state = MenuState.AMOUNT;
		}
		if (handler.keys[Keys.ENTER]) {
			enterActivMenu();
		}
		if (handler.keys[Keys.ESCAPE]) {
			getBack();
		}
	}

	@Override
	public void switchToMenu() {		
		handler.activMenu = this;
		nameButton.setIsPressed(true);
		state = MenuState.NAME;
	}
	
	@Override
	public void switchBackToMenu(){
		switchToMenu();
	}

	private void amountPlayerUp() {
		if (playerAmount < maxAmount) {
			playerAmount++;
			amountPlayerButton.setText("" + playerAmount);
		}
		touchDown(-1, -1);
		amountPlayerButton.setIsPressed(true);
		morePlayerButton.setIsPressed(true);
	}

	private void amountPlayerDown() {
		if (playerAmount > 1) {
			playerAmount--;
			amountPlayerButton.setText("" + playerAmount);
		}
		touchDown(-1, -1);
		amountPlayerButton.setIsPressed(true);
		lessPlayerButton.setIsPressed(true);
	}

	public void enterActivMenu() {
		switch (state) {
		case NAME:
			setName();
			break;
		case CREATE:
			createRoom();
			break;
		case BACK:
			getBack();
			break;
		default:
			break;
		}
	}

	public void createRoom() {
		if (Settings.roomName==null) {
			Settings.roomName = Settings.userName + nameEnd;
		}
		System.out.println("MUCREA: crete Room");
		createButton.setIsPressed(false);
		handler.lastMenu = this;
		handler.multiPlayerLobbyHandler.switchToMenuAsHost(Settings.roomName,
				Settings.userName, playerAmount, null);
	}

	private void setName() {
		SimpleSettingsInput listener = new SimpleSettingsInput(
				Settings.roomNameKey);
		Gdx.input.getTextInput(listener, "RoomName", Settings.roomName);
	}

	// private void openChatInput() {
	// SimpleChatInput input = new SimpleChatInput();
	// Gdx.input.getTextInput(input, "Chat", "Your Message here");
	// }

	private void getBack() {
		backButton.setIsPressed(false);
		if (WarpController.getInstance().isOnline()) {
			handler.lastMenu = this;
			handler.multiPlayerMenuHandler.switchBackToMenu();
		} else {
			WarpController.getInstance().handleServerLeave();
			handler.lastMenu = this;
			handler.mainMenuHandler.switchToMenu();
		}
	}

	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (nameButton.isTouchDown(screenX, screenY)) {
			state = MenuState.NAME;
			buttonPressed = true;
		}
		if (amountPlayerButton.isTouchDown(screenX, screenY)) {
			state = MenuState.AMOUNT;
		}

		if (morePlayerButton.isTouchDown(screenX, screenY)) {
			amountPlayerUp();
		}
		if (lessPlayerButton.isTouchDown(screenX, screenY)) {
			amountPlayerDown();
		}
		if (createButton.isTouchDown(screenX, screenY)) {
			state = MenuState.CREATE;
			buttonPressed = true;
		}
		if (backButton.isTouchDown(screenX, screenY)) {
			state = MenuState.BACK;
			buttonPressed = true;
		}

		if (buttonPressed)
			enterActivMenu();
	}

	public void menuDown() {
		touchDown(-1, -1); // resetting all Buttons

		switch (state) {
		case NAME:
			amountPlayerButton.setIsPressed(true);
			state = MenuState.AMOUNT;
			break;
		case AMOUNT:
			createButton.setIsPressed(true);
			state = MenuState.CREATE;
			break;
		case CREATE:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case BACK:
			nameButton.setIsPressed(true);
			state = MenuState.NAME;
			break;
		}

	}

	public void menuUp() {
		touchDown(-1, -1); // resetting all Buttons
		switch (state) {
		case NAME:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case AMOUNT:
			nameButton.setIsPressed(true);
			state = MenuState.NAME;
			break;
		case CREATE:
			amountPlayerButton.setIsPressed(true);
			state = MenuState.AMOUNT;
			break;
		case BACK:
			createButton.setIsPressed(true);
			state = MenuState.CREATE;
			break;
		}
	}

	@Override
	public void drawMenu(SpriteBatch batch) {
		for (SimpleElement button : MenuButtons) {
			button.draw(batch);
		}
	}

}
