package com.kilobolt.Menus;

import java.util.ArrayList;
import java.util.List;

import com.appwarp.Multiplayer.RoomInformation;
import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.ISOHelpers.ScrollHandler;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.FriendList;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleColoredRectangle;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleSettingsInput;
import com.kilobolt.SimpleHelpers.SimpleText;

public class FriendsMenu extends Menu {

	static SimpleButton backButton;

	static SimpleButton addButton;
	static SimpleButton removeButton;

	static SimpleButton upButton;
	static SimpleButton downButton;
	static SimpleButton chatButton;

	private static ScrollHandler<String> friends;
	private static int amountToShow = 3;

	private List<SimpleColoredRectangle> loadMenuBackgrounds;

	static int backgroundWidth = 50;
	static int border = 2;

	static int textStartPos = 5;
	static int textGapSpace = 10;

	public List<SimpleElement> MenuButtons;

	public enum MenuState {
		CHAT, ADD, REMOVE, BACK;
	}

	float xpos;

	private MenuState state;

	public FriendsMenu(InputHandler inputHandler, int midPointX, int midPointY) {
		super(inputHandler, midPointX, midPointY);
		state = MenuState.BACK;
		xpos = midPointX;
		friends = new ScrollHandler(amountToShow);
		updateFriendsToDisplay();
		initButtons(midPointX, midPointY);
		initBackGrounds(midPointX, midPointY);
	}

	private static float lastUpdate = 0;
	private static float refreshRate = 3;

	public void updateMPValues(float runTime) {
		if (handler.activMenu == this) {
			if (lastUpdate + refreshRate >= runTime) {
				return;
			}
			lastUpdate = runTime;

//			if (WarpController.getInstance().isOnline()) {
				updateFriendsToDisplay();
//			}
		}
	}

	public static List<SimpleText> friendsToDisplay;

	public void updateFriendsToDisplay() {
		friendsToDisplay = new ArrayList<SimpleText>();
		int pos = textStartPos;
		List<String> onlineFriends = FriendList.getFriends();
		friends.setObject(onlineFriends);
		List<String> toDisplay = friends.getObjects();

		boolean first = true;

		for (int i = 0; i < toDisplay.size(); i++) {

			String nameOrID = RoomInformation.getRoomNameInHashMap(toDisplay
					.get(i));

			if (first) {
				friendsToDisplay.add(new SimpleText(xpos, pos, "> " + nameOrID
						+ " <", 1));
				first = false;
			} else {
				friendsToDisplay.add(new SimpleText(xpos, pos, nameOrID, 1));
			}
			pos += textGapSpace;
		}
	}

	public void initBackGrounds(float midPointX, float midPointY) {
		loadMenuBackgrounds = new ArrayList<SimpleColoredRectangle>();

		float w = midPointX - (backgroundWidth / 2f);
		float h = textStartPos + friends.getAmountToShow() * textGapSpace;

		SimpleColoredRectangle fileTextBackB = new SimpleColoredRectangle(w
				- border, textStartPos - 3 - border, backgroundWidth + 2
				* border, h + 2 * border - 3, Color.BLACK);
		SimpleColoredRectangle fileTextBackG = new SimpleColoredRectangle(w,
				textStartPos - 3, backgroundWidth, h - 3, Color.LIGHT_GRAY);

		loadMenuBackgrounds.add(fileTextBackB);
		loadMenuBackgrounds.add(fileTextBackG);
	}

	public void initButtons(float midPointX, float midPointY) {

		MenuButtons = new ArrayList<SimpleElement>();

		backButton = new SimpleButton(midPointX * (2f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "BACK");

		addButton = new SimpleButton(midPointX * (6f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "ADD");

		removeButton = new SimpleButton(midPointX * (10f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "REM");

		chatButton = new SimpleButton(midPointX * (14f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "CHAT");

		upButton = new SimpleButton(midPointX * (14f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "UP");

		downButton = new SimpleButton(midPointX * (14f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "DOWN");

		MenuButtons.add(chatButton);

		MenuButtons.add(addButton);
		MenuButtons.add(removeButton);

		MenuButtons.add(upButton);
		MenuButtons.add(downButton);

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
			menuRight();
		}
		if (handler.keys[Keys.LEFT]) {
			menuLeft();
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

		touchDown(-1, -1);
		backButton.setIsPressed(true);
		state = MenuState.BACK;
	}

	public void enterActivMenu() {
		switch (state) {
		case CHAT:
			
			break;
		case ADD:
			FriendList.addFriend();
			break;
		case REMOVE:
			FriendList.removeFriend();
			break;
		case BACK:
			getBack();
			break;
		}
	}

	private void getBack() {
		handler.lastMenu.switchBackToMenu();
	}

	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (upButton.isTouchDown(screenX, screenY)) {
			menuUp();
			return;
		}
		if (downButton.isTouchDown(screenX, screenY)) {
			menuDown();
			return;
		}

		if (chatButton.isTouchDown(screenX, screenY)) {
			state = MenuState.CHAT;
			buttonPressed = true;
		}

		if (addButton.isTouchDown(screenX, screenY)) {
			state = MenuState.ADD;
			buttonPressed = true;
		}
		if (removeButton.isTouchDown(screenX, screenY)) {
			state = MenuState.REMOVE;
			buttonPressed = true;
		}

		if (backButton.isTouchDown(screenX, screenY)) {
			state = MenuState.BACK;
			buttonPressed = true;
		}

		if (buttonPressed)
			enterActivMenu();
	}

	public void menuLeft() {
		touchDown(-1, -1);

		switch (state) {
		case BACK:
			backButton.setIsPressed(true);
			break;
		case ADD:
			state = MenuState.BACK;
			backButton.setIsPressed(true);
			break;
		case REMOVE:
			state = MenuState.ADD;
			addButton.setIsPressed(true);
			break;
		case CHAT:
			state = MenuState.REMOVE;
			removeButton.setIsPressed(true);
			break;
		}
	}

	public void menuRight() {
		touchDown(-1, -1);

		switch (state) {
		case BACK:
			state = MenuState.ADD;
			addButton.setIsPressed(true);
			break;
		case ADD:
			state = MenuState.REMOVE;
			removeButton.setIsPressed(true);
			break;
		case REMOVE:
			state = MenuState.CHAT;
			chatButton.setIsPressed(true);
			break;
		case CHAT:
			chatButton.setIsPressed(true);
			break;
		}
	}

	public void menuDown() {
		friends.up();
		updateFriendsToDisplay();
		upButton.setIsPressed(false);
		downButton.setIsPressed(true);
	}

	public void menuUp() {
		friends.down();
		updateFriendsToDisplay();
		downButton.setIsPressed(false);
		upButton.setIsPressed(true);
	}

	@Override
	public void drawMenu(SpriteBatch batch) {
		for (SimpleElement button : MenuButtons) {
			button.draw(batch);
		}
		drawBackgrounds(batch);
		drawFriends(batch);
		FriendList.friendsButton.draw(batch);
	}

	private void drawBackgrounds(SpriteBatch batch) {
		for (SimpleElement elem : loadMenuBackgrounds) {
			elem.draw(batch);
		}
	}

	private void drawFriends(SpriteBatch batch) {
		for (SimpleElement text : friendsToDisplay) {
			text.draw(batch);
		}
	}

}
