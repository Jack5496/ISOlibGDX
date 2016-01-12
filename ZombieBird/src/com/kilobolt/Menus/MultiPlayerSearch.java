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
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleColoredRectangle;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleText;

public class MultiPlayerSearch extends Menu {

	static SimpleButton joinButton;
	static SimpleButton resolveNamesButton;
	static SimpleButton backButton;

	static SimpleButton downButton;
	static SimpleButton upButton;

	private static ScrollHandler<String> rooms;
	private static int amountToShow = 3;

	private SimpleText roomsOnline;
	private List<SimpleColoredRectangle> loadMenuBackgrounds;

	static int backgroundWidth = 50;
	static int border = 2;

	static int textStartPos = 5;
	static int textGapSpace = 10;

	public List<SimpleElement> MenuButtons;

	public enum MenuState {
		JOIN, BACK;
	}

	private MenuState state;

	static float xpos;

	static Table table;
	
	public MultiPlayerSearch(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);
		state = MenuState.BACK;
		xpos = midPointX;
		initRoomsOnline();
		initRoomList();
		initButtons(midPointX, midPointY);
		backButton.setIsPressed(true);
		initBackGrounds(midPointX, midPointY);
	}

	private void initRoomsOnline() {
		roomsOnline = new SimpleText(5, 5, "", 0);
		updateRoomsOnline();
	}

	private void updateRoomsOnline() {
		roomsOnline.setText("Rooms: "
				+ WarpController.getInstance().getAllRoomID().size());
	}

	public void initRoomList() {
		roomDisplayIDs = new ArrayList<SimpleText>();
		rooms = new ScrollHandler<String>(amountToShow);
		updateValues();
	}

	public static List<SimpleText> roomDisplayIDs;

	public void updateRoomValues() {
		if (handler.activMenu == this) {

			System.out.println("MULobby: update Room values");
			
			roomDisplayIDs = new ArrayList<SimpleText>();
			int pos = textStartPos;
			List<String> roomIDs = WarpController.getInstance().getAllRoomID();
			rooms.setObject(roomIDs);
			List<String> toDisplay = rooms.getObjects();
			System.out.println("Update Values of: "+toDisplay.size()+" rooms");
			RoomInformation.addPollEntrys(toDisplay);
			RoomInformation.workTheToPollList();
			
			boolean first = true;

			for (int i = 0; i < toDisplay.size(); i++) {

				String nameOrID = RoomInformation
						.getRoomNameInHashMap(toDisplay.get(i));

				if (first) {
					roomDisplayIDs.add(new SimpleText(xpos, pos, "> "
							+ nameOrID + " <", 1));
					first = false;
				} else {
					roomDisplayIDs.add(new SimpleText(xpos, pos, nameOrID, 1));
				}
				pos += textGapSpace;
			}

		}
	}

	private static float lastUpdate = 0;
	private static float refreshRate = 3;

	public void updateMPValues(float runTime) {
		if (handler.activMenu == this) {
			if (lastUpdate + refreshRate >= runTime) {
				return;
			}
			lastUpdate = runTime;

			if (WarpController.getInstance().isOnline()) {
				updateValues();
			}
		}
	}

	private void updateValues() {
		updateRoomValues();
		updateRoomsOnline();
	}

	public void initBackGrounds(float midPointX, float midPointY) {
		loadMenuBackgrounds = new ArrayList<SimpleColoredRectangle>();

		float w = midPointX - (backgroundWidth / 2f);
		float h = textStartPos + rooms.getAmountToShow() * textGapSpace;

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

		backButton = new SimpleButton(midPointX * (4f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "BACK");

		resolveNamesButton = new SimpleButton(midPointX * (8f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "RESOLVE");

		joinButton = new SimpleButton(midPointX * (12f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "JOIN");

		upButton = new SimpleButton(midPointX * (14f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "UP");

		downButton = new SimpleButton(midPointX * (14f / 8f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (10f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "DOWN");

		MenuButtons.add(joinButton);
		MenuButtons.add(resolveNamesButton);

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
	public void switchBackToMenu(){
		switchToMenu();
	}
	
	@Override
	public void switchToMenu() {
		handler.activMenu = this;

		touchDown(-1, -1);
		backButton.setIsPressed(true);
		state = MenuState.BACK;
		RoomInformation.resetList();
		RoomInformation.activRoomID = null;
		initRoomList();
	}

	public void enterActivMenu() {
		switch (state) {
		case JOIN:
			joinRoom();
			break;
		case BACK:
			getBack();
			break;
		}
	}

	private void joinRoom() {
		String id = rooms.getObject();
		if (id != null) {
			handler.lastMenu = this;
			handler.multiPlayerLobbyHandler.switchToMenu(id);
		}
	}

	private void getBack() {
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

		if (upButton.isTouchDown(screenX, screenY)) {
			menuUp();
			return;
		}
		if (downButton.isTouchDown(screenX, screenY)) {
			menuDown();
			return;
		}

		if (joinButton.isTouchDown(screenX, screenY)) {
			state = MenuState.JOIN;
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
		state = MenuState.BACK;
		touchDown(-1, -1);
		backButton.setIsPressed(true);
	}

	public void menuRight() {
		state = MenuState.JOIN;
		touchDown(-1, -1);
		joinButton.setIsPressed(true);
	}

	public void menuDown() {
		rooms.up();
		updateRoomValues();
		upButton.setIsPressed(false);
		downButton.setIsPressed(true);
	}

	public void menuUp() {
		rooms.down();
		updateRoomValues();
		downButton.setIsPressed(false);
		upButton.setIsPressed(true);
	}

	@Override
	public void drawMenu(SpriteBatch batch) {
		for (SimpleElement button : MenuButtons) {
			button.draw(batch);
		}
		drawBackgrounds(batch);
		drawRooms(batch);

		roomsOnline.draw(batch);
	}

	private void drawBackgrounds(SpriteBatch batch) {
		for (SimpleElement elem : loadMenuBackgrounds) {
			elem.draw(batch);
		}
	}

	private void drawRooms(SpriteBatch batch) {
		for (SimpleElement text : roomDisplayIDs) {
			text.draw(batch);
		}
	}

}
