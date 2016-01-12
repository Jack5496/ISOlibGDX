package com.kilobolt.Menus;

import java.util.ArrayList;
import java.util.List;

import com.appwarp.Multiplayer.StartMultiplayerListener;
import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.Settings;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleSettingsInput;
import com.kilobolt.SimpleHelpers.SimpleText;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;

public class MultiPlayerMenu extends Menu {

	static SimpleButton randomButton;
	static SimpleButton listButton;
	static SimpleButton createButton;
	static SimpleButton backButton;

	static Table table;

	static SimpleButton reconnectButton;

	public static List<SimpleText> playersTexts;
	public List<SimpleElement> MenuButtons;

	public static SimpleText connectingTextElement;
	public List<SimpleElement> connectingElements;

	public BitmapFont bitFont;

	public enum MenuState {
		RANDOM, LIST, CREATE, BACK, RECONNECT, CONNECTING;
	}

	private static MenuState state = MenuState.BACK;

	public MultiPlayerMenu(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);

		state = MenuState.CONNECTING;

		connectingElements = new ArrayList<SimpleElement>();

		initTextField(midPointX, midPointY);
		initButtons(midPointX, midPointY);
		initText(midPointX, midPointY);
		refreshTotalPlayersOnline();
	}

	private float scale = .5f;
//	private float lineSpaceHeight;

	private void initTextField(int midPointX, int midPointY) {
		bitFont = new BitmapFont();
		bitFont.setColor(Color.BLACK);
		bitFont.setScale(1 * scale, -1 * scale);
//		lineSpaceHeight = bitFont.getBounds("Test").height;
//		lineSpaceHeight += bitFont.getAscent();
	}

	public static float lastUpdate = 0;
	public static float refreshRate = 1;
	private boolean lostConnection = false;

	public void updateMPValues(float runTime) {
		if (handler.activMenu == this) {
			
			if (lastUpdate + refreshRate >= runTime) {
				return;
			}
			lastUpdate = runTime;

			if (WarpController.getInstance().isOnline()) {
				if (lostConnection) { // if we were without connection this will
										// be cales once
					connected();
					lostConnection = false;
				}
				refreshTotalPlayersOnline();
			} else {
				if (!lostConnection) { // bad name of variable but ! is right
					noConnection((byte) 5); // set once that youre offline
					lostConnection = true; // next time when online goto
											// connected
				}
				changeConnectingString();
			}
		}
	}

	private static String connectingText = "Connecting";
	private static int dots = 0;

	private static void changeConnectingString() {
		dots++;
		String d = "";
		if (dots > 3) {
			dots = 0;
		}
		for (int i = 0; i < dots; i++) {
			d = d + ".";
		}

		connectingTextElement.setText(connectingText + d);
	}

	public void initText(float midPointX, float midPointY) {
		connectingTextElement = new SimpleText(midPointX,
				midPointY * (6f / 8f), connectingText, 1);
		connectingElements.add(connectingTextElement);
	}

	public void initButtons(float midPointX, float midPointY) {

		reconnectButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "RETRY");

		MenuButtons = new ArrayList<SimpleElement>();

		randomButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "RANDOM");

		listButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "LIST");

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

		MenuButtons.add(randomButton);
		MenuButtons.add(listButton);
		MenuButtons.add(createButton);

		MenuButtons.add(backButton);
		connectingElements.add(backButton);
	}

	public static int startPosTop = 5;

	public static void refreshTotalPlayersOnline() {
		playersTexts = new ArrayList<SimpleText>();

		String[] onlinePlayers = WarpController.getInstance().getAllPlayers();

		int pos = startPosTop;

		if (onlinePlayers != null) {
			playersTexts.add(new SimpleText(pos, pos, "Players: "
					+ onlinePlayers.length, 0));
		} else {
			playersTexts.add(new SimpleText(pos, pos, "Offline", 0));
		}
	}

	@Override
	public void keyDown() {
		if (handler.keys[Keys.UP]) {
			menuUp();
		}
		if (handler.keys[Keys.DOWN]) {
			menuDown();
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
		touchDown(-1, -1);
		backButton.setIsPressed(true);

		state = MenuState.CONNECTING;

		WarpController.getInstance().startApp(Settings.userName);
		new StartMultiplayerListener();

		MultiPlayerMenu.refreshTotalPlayersOnline();
		handler.activMenu = this;
	}

	@Override
	public void switchBackToMenu() {
		touchDown(-1, -1);
		randomButton.setIsPressed(true);
		state = MenuState.RANDOM;
		handler.activMenu = this;
	}

	public void enterActivMenu() {
		switch (state) {
		case RANDOM:
			handler.lastMenu = this;
			handler.multiPlayerLobbyHandler.switchToMenu();
			break;
		case LIST:
			handler.lastMenu = this;
			handler.multiPlayerSearchHandler.switchToMenu();
			break;
		case CREATE:
			handler.lastMenu = this;
			handler.multiPlayerCreateHandler.switchToMenu();
			break;
		case BACK:
			getBack();
			break;
		case CONNECTING:
			getBack();
			break;
		case RECONNECT:
			switchToMenu();
			break;
		}
	}

	private void getBack() {
		WarpController.getInstance().disconnect();
		handler.lastMenu = this;
		handler.mainMenuHandler.switchToMenu();
	}

	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (WarpController.getInstance().isOnline()) {
			if (randomButton.isTouchDown(screenX, screenY)) {
				state = MenuState.RANDOM;
				buttonPressed = true;
			}
			if (listButton.isTouchDown(screenX, screenY)) {
				state = MenuState.LIST;
				buttonPressed = true;
			}
			if (createButton.isTouchDown(screenX, screenY)) {
				state = MenuState.CREATE;
				buttonPressed = true;
			}
			if (backButton.isTouchDown(screenX, screenY)) {
				state = MenuState.BACK;
				buttonPressed = true;
			}
		} else {
			if (state == MenuState.RECONNECT) {
				if (reconnectButton.isTouchDown(screenX, screenY)) {
					buttonPressed = true;
				}
			}
			if (backButton.isTouchDown(screenX, screenY)) {
				state = MenuState.BACK;
				buttonPressed = true;
			}
		}

		if (buttonPressed)
			enterActivMenu();
	}

	public void menuDown() {
		touchDown(-1, -1); // resetting all Buttons

		switch (state) {
		case RANDOM:
			listButton.setIsPressed(true);
			state = MenuState.LIST;
			break;
		case LIST:
			createButton.setIsPressed(true);
			state = MenuState.CREATE;
			break;
		case CREATE:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case BACK:
			if (WarpController.getInstance().isOnline()) {
				randomButton.setIsPressed(true);
				state = MenuState.RANDOM;
			} else {
				reconnectButton.setIsPressed(true);
				state = MenuState.RECONNECT;
			}
			break;
		case CONNECTING:
			backButton.setIsPressed(true);
			break;
		case RECONNECT:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		}

	}

	public void menuUp() {
		touchDown(-1, -1); // resetting all Buttons
		switch (state) {
		case RANDOM:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case LIST:
			randomButton.setIsPressed(true);
			state = MenuState.RANDOM;
			break;
		case CREATE:
			listButton.setIsPressed(true);
			state = MenuState.LIST;
			break;
		case BACK:
			if (WarpController.getInstance().isOnline()) {
				createButton.setIsPressed(true);
				state = MenuState.CREATE;
			} else {
				reconnectButton.setIsPressed(true);
				state = MenuState.RECONNECT;
			}
			break;
		case CONNECTING:
			backButton.setIsPressed(true);
			break;
		case RECONNECT:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		}
	}

	@Override
	public void drawMenu(SpriteBatch batch) {
		if (WarpController.getInstance().isOnline()) {

			for (SimpleElement button : MenuButtons) {
				button.draw(batch);
			}
			for (SimpleElement button : playersTexts) {
				button.draw(batch);
			}
		} else {
			switch (state) {
			case CONNECTING:
				for (SimpleElement toDraw : connectingElements) {
					toDraw.draw(batch);
				}
				break;
			case RECONNECT:
			case BACK:
				reconnectButton.draw(batch);
				backButton.draw(batch);
				break;
			default:
				break;
			}

		}

	}

	public static void noConnection(byte b) {
		state = MenuState.RECONNECT;
		backButton.setIsPressed(false);
		reconnectButton.setIsPressed(true);
		
		if(b==WarpResponseResultCode.AUTH_ERROR){
			SimpleSettingsInput.getInput(Settings.userNameKey, "User Alredy logged in", Settings.userName);
		}
		
	}

	public static void connected() {
		state = MenuState.RANDOM;
		randomButton.setIsPressed(true);
		backButton.setIsPressed(false);
	}
}
