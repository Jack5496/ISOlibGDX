package com.kilobolt.Menus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.appwarp.Multiplayer.ChatMessage;
import com.appwarp.Multiplayer.RoomInformation;
import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.Settings;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleChatInput;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleText;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;

public class MultiPlayerLobby extends Menu {

	static SimpleButton startButton;

	public List<SimpleElement> HostButtons;

	static SimpleButton quickCHATButton;
	static SimpleButton backButton;

	static SimpleButton reconnectButton;

	public static List<SimpleText> playersTexts;
	public List<SimpleElement> MenuButtons;

	public static SimpleText connectingTextElement;
	public List<SimpleElement> connectingElements;

	public BitmapFont bitFont;

	private static List<ChatMessage> oldchatMessages = new ArrayList<ChatMessage>();
	private static List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
	public static int messagePointer = 0;
	public static boolean messagePointerOnLast = true;
	public static int messageAmountToShow = 5;

	public enum MenuState {
		CHAT, BACK, START, RECONNECT, CONNECTING;
	}

	private static MenuState state = MenuState.BACK;

	public MultiPlayerLobby(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);
		state = MenuState.CONNECTING;

		connectingElements = new ArrayList<SimpleElement>();

		initTextField(midPointX, midPointY);
		initButtons(midPointX, midPointY);
		initHostButtons(midPointX, midPointY);
		initText(midPointX, midPointY);
		refreshTotalPlayersOnline();
	}

	private float scale = .5f;
	private float lineSpaceHeight;

	private void initTextField(int midPointX, int midPointY) {
		bitFont = new BitmapFont();
		bitFont.setColor(Color.BLACK);
		bitFont.setScale(1 * scale, -1 * scale);
		lineSpaceHeight = bitFont.getBounds("Test").height;
		lineSpaceHeight += bitFont.getAscent();
	}

	public static float lastUpdate = 0;
	public static float refreshRate = 1;

	public void updateMPValues(float runTime) {
		if (handler.activMenu == this) {
			if (lastUpdate + refreshRate >= runTime) {
				return;
			}
			lastUpdate = runTime;

			if (WarpController.getInstance().isInLobby()) {
				refreshTotalPlayersOnline();
			} else {
				changeConnectingString();
			}

			checkIfChatMessagesTooOld(runTime);
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

	private void initHostButtons(float midPointX, float midPointY) {
		startButton = new SimpleButton(midPointX * (10f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "START");

		HostButtons = new ArrayList<SimpleElement>();

		HostButtons.add(startButton);
	}

	public void initButtons(float midPointX, float midPointY) {

		reconnectButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "RETRY");

		MenuButtons = new ArrayList<SimpleElement>();

		quickCHATButton = new SimpleButton((14f / 8f) * midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "CHAT");
		quickCHATButton.setIsPressed(true);

		backButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (10f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "BACK");

		MenuButtons.add(quickCHATButton);
		MenuButtons.add(backButton);
		connectingElements.add(backButton);
	}

	public static int startPosTop = 5;

	public static void refreshTotalPlayersOnline() {
		playersTexts = new ArrayList<SimpleText>();

		float xpos = reconnectButton.x + reconnectButton.width / 2;

		int pos = startPosTop;

		LiveRoomInfoEvent event = RoomInformation.getActivRoomEvent();

		if (event != null) {
			String id = event.getData().getId();

			RoomInformation.updateActivRoom();

			String roomName = RoomInformation.getRoomNameInHashMap(id);
			String roomOwner = RoomInformation.getOwnerOfRoom(id);
			int joinedUsers = RoomInformation.getUsersAmountInRoom(id);
			int maxUsers = RoomInformation.getMaxUsersInHashMap(id);
			//
			playersTexts.add(new SimpleText(xpos, pos, "ID: " + id, 1));
			playersTexts.add(new SimpleText(xpos, pos + 10,
					"Name: " + roomName, 1));
			playersTexts.add(new SimpleText(xpos, pos + 20, "Owner: "
					+ roomOwner, 1));
			playersTexts.add(new SimpleText(xpos, pos + 30, "Lobby: "
					+ joinedUsers + "/" + maxUsers, 1));
		} else {
			playersTexts.add(new SimpleText(xpos, pos, "None", 1));
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
		if (handler.keys[Keys.T]) {
			openChatInput();
		}
		if (handler.keys[Keys.ENTER]) {
			enterActivMenu();
		}
		if (handler.keys[Keys.ESCAPE]) {
			getBack();
		}
	}

	@Override
	public void switchBackToMenu() {
		state = MenuState.CHAT;
		clearMessages();
		handler.activMenu = this;
	}

	@Override
	public void switchToMenu() {
		state = MenuState.CONNECTING;
		clearMessages();
		WarpController.getInstance().joinRoomInRange();
		MultiPlayerLobby.refreshTotalPlayersOnline();
		handler.activMenu = this;
	}

	public void switchToMenu(String roomID) {
		handler.activMenu = this;

		clearMessages();
		MultiPlayerLobby.addMessage(Settings.getSystemName(), "joining "
				+ roomID);
		state = MenuState.CONNECTING;
		WarpController.getClient().joinRoom(roomID);
		MultiPlayerLobby.refreshTotalPlayersOnline();
	}

	public void switchToMenuAsHost(String name, String owner, int amount,
			HashMap<String, Object> data) {
		WarpController.getClient().createRoom(name, owner, amount, data);
		handler.activMenu = this;
		connected();
	}

	public void enterActivMenu() {
		switch (state) {
		case CHAT:
			openChatInput();
			break;
		case BACK:
			handler.lastMenu = this;
			getBack();
			break;
		case CONNECTING:
			handler.lastMenu = this;
			getBack();
			break;
		case RECONNECT:
			switchToMenu();
			break;
		}
	}

	private void openChatInput() {
		SimpleChatInput input = new SimpleChatInput();
		Gdx.input.getTextInput(input, "Chat", "Your Message here");
	}

	private void getBack() {
		WarpController.getInstance().handleLobbyLeave();
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

		if (WarpController.getInstance().isInLobby()) {
			if (quickCHATButton.isTouchDown(screenX, screenY)) {
				state = MenuState.CHAT;
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
		case CHAT:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case BACK:
			if (WarpController.getInstance().isInLobby()) {
				quickCHATButton.setIsPressed(true);
				state = MenuState.CHAT;
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
		case CHAT:
			backButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case BACK:
			if (WarpController.getInstance().isInLobby()) {
				quickCHATButton.setIsPressed(true);
				state = MenuState.CHAT;
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

		drawChatMessages(batch);

		if (WarpController.getInstance().isInLobby()) {

			for (SimpleElement button : MenuButtons) {
				button.draw(batch);
			}
			for (SimpleElement button : playersTexts) {
				button.draw(batch);
			}
			drawHostButtons(batch);
			// drawUsersInRoom(batch);
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

	private void drawHostButtons(SpriteBatch batch) {
		if (WarpController.getInstance().amIRoomOwner()) {
			for (SimpleElement button : HostButtons) {
				button.draw(batch);
			}
		}
	}

	public static void noConnection() {
		state = MenuState.RECONNECT;

		backButton.setIsPressed(false);
		reconnectButton.setIsPressed(true);
	}

	public static void connected() {
		state = MenuState.CHAT;
		quickCHATButton.setIsPressed(false);
	}

	private void drawChatMessages(SpriteBatch batch) {
		for (int i = 0; i < messageAmountToShow; i++) {
			if (i < chatMessages.size()) {
				chatMessages.get(i).drawMessage(bitFont, batch, 0,
						-i * lineSpaceHeight);
			}
		}
	}

	// private void drawUsersInRoom(SpriteBatch batch) {
	//
	// }

	private static void moveToOldMessage(ChatMessage mes) {
		oldchatMessages.add(mes);
		chatMessages.remove(mes);
	}

	private static int seconds_after_dissapear = 8;

	private static void checkIfChatMessagesTooOld(float runTime) {
		Date now = new Date();

		Calendar cal = Calendar.getInstance();

		List<ChatMessage> copyOfMessages = new ArrayList<ChatMessage>(
				chatMessages);

		for (ChatMessage mes : copyOfMessages) {
			Date date = mes.getDate();
			cal.setTime(date);
			cal.add(Calendar.SECOND, seconds_after_dissapear); // add 10 sec
			date = cal.getTime();

			if (now.after(date)) {
				moveToOldMessage(mes);
			}
		}

	}

	public static void addMessage(String sender, String message) {
		chatMessages.add(new ChatMessage(sender, message));
		if (chatMessages.size() > messageAmountToShow) {
			moveToOldMessage(chatMessages.get(0));
		}
	}
	
	public static void clearMessages(){
		chatMessages = new ArrayList<ChatMessage>();
	}

}
