package com.kilobolt.Menus;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleColoredRectangle;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleText;

public class LoadMenu extends Menu{

	public static List<SimpleText> files;

	public static int pointer;
	
	public enum MenuState {
		LOAD, BACK
	}

	private MenuState state;
	
	static SimpleButton loadMenuLoadButton;
	static SimpleButton loadMenuBackButton;
	static SimpleButton loadMenuUpButton;
	static SimpleButton loadMenuDownButton;
		
	private List<SimpleElement> loadMenuButtons;
	private List<SimpleColoredRectangle> loadMenuBackgrounds;

	public LoadMenu(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler,midPointX, midPointY);

		state = MenuState.BACK;
		
		initBackgrounds(midPointX, midPointY);
		initButtons(midPointX, midPointY);
	}
	
	static int backgroundWidth = 50;
	static int border = 2;
	
	public void initBackgrounds(float midPointX, float midPointY){
		loadMenuBackgrounds = new ArrayList<SimpleColoredRectangle>();
		
		float w = midPointX-(backgroundWidth/2f);
		float h = textStartPos+maxMapsToShow*textGapSpace;
		
		SimpleColoredRectangle fileTextBackB = new SimpleColoredRectangle(w-border,textStartPos-3-border,backgroundWidth+2*border,h+2*border-3,Color.BLACK);
		SimpleColoredRectangle fileTextBackG = new SimpleColoredRectangle(w,textStartPos-3,backgroundWidth,h-3,Color.LIGHT_GRAY);
		
		loadMenuBackgrounds.add(fileTextBackB);
		loadMenuBackgrounds.add(fileTextBackG);
	}
	
	public void initButtons(float midPointX, float midPointY){
		pointer = 0;
		
		loadMenuButtons = new ArrayList<SimpleElement>();

		loadMenuLoadButton = new SimpleButton(midPointX * (2f / 4f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 4f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "LOAD");
		loadMenuLoadButton.setIsPressed(true);

		loadMenuBackButton = new SimpleButton(midPointX * (6f / 4f)
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 4f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "BACK");

		loadMenuUpButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (5f / 4f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "UP");

		loadMenuDownButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonDown.getRegionWidth() / 2), (7f / 4f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "DOWN");

		loadMenuButtons.add(loadMenuLoadButton);
		loadMenuButtons.add(loadMenuBackButton);
		loadMenuButtons.add(loadMenuUpButton);
		loadMenuButtons.add(loadMenuDownButton);		
	}
	
	static int textStartPos = 5;
	static int maxMapsToShow = 3;
	static int textGapSpace = 10;
	
	public static void loadAllMaps() {
		int pos = textStartPos;
		files = new ArrayList<SimpleText>();
		float xpos = loadMenuDownButton.x
				+ loadMenuDownButton.width / 2;

		FileHandle[] fileList = getAllMapFile();
		boolean first = true;
		
		if (fileList.length > 0) {
			for (int i = pointer; i < fileList.length && i < pointer+maxMapsToShow; i++) {
				String fileName = fileList[i].file().getName();
				fileName = fileName.replaceFirst("[.][^.]+$", "");
				if(first) {
					fileName = "> "+fileName+" <";
					first = false;
				}
				files.add(new SimpleText(xpos, pos, fileName, 1));
				pos += textGapSpace;
			}
		} else {
			files.add(new SimpleText(xpos, pos, "-Empty-", 1));
		}
	}

	public static List<SimpleText> getAllMapText() {
		return files;
	}

	public static FileHandle[] getAllMapFile() {
		return handler.getGameWorld().getLevelManager().getAllMaps();
	}

	@Override
	public void keyDown() {

		if (handler.keys[Keys.LEFT]) {
			menuLeft();
		}
		if (handler.keys[Keys.RIGHT]) {
			menuRight();
		}
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
			handler.singlePlayerHandler.switchToMenu();
		}
	}

	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (loadMenuLoadButton.isTouchDown(screenX, screenY)) {
			state = MenuState.LOAD;
			buttonPressed = true;
		}
		if (loadMenuBackButton.isTouchDown(screenX, screenY)) {
			state = MenuState.BACK;
			buttonPressed = true;
		}
		if (loadMenuUpButton.isTouchDown(screenX, screenY)) {
			menuUp();
		}
		if (loadMenuDownButton.isTouchDown(screenX, screenY)) {
			menuDown();
		}

		if (buttonPressed)
			enterActivMenu();
	}
	
	@Override
	public void switchToMenu() {
		LoadMenu.loadAllMaps();
		state = MenuState.LOAD;
		handler.activMenu = this;
	}
	
	@Override
	public void switchBackToMenu(){
		switchToMenu();
	}
	
	@Override
	public void enterActivMenu() {
		switch (state) {
		case LOAD:
			handler.getGameWorld().getLevelManager().loadMap();
			handler.lastMenu = this;
			handler.ingameMenuHandler.switchToMenu();
			break;
		case BACK:
			handler.lastMenu = this;
			handler.singlePlayerHandler.switchToMenu();
			break;
		}
	}

	public void menuDown() {
		touchDown(-1, -1); // resetting all Buttons
		loadMenuDownButton.setIsPressed(true);

		if (pointer < getAllMapFile().length-1) {
			pointer++;
			loadAllMaps();
		}
		else{
			pointer = getAllMapFile().length-1;
		}
	}

	public void menuUp() {
		touchDown(-1, -1); // resetting all Buttons
		state = MenuState.LOAD;
		loadMenuUpButton.setIsPressed(true);

		if (pointer > 0) {
			pointer--;
			loadAllMaps();
		}
		else{
			pointer = 0;
		}
	}

	public void menuLeft() {
		touchDown(-1, -1); // resetting all Buttons
		state = MenuState.LOAD;
		loadMenuLoadButton.setIsPressed(true);
	}

	public void menuRight() {
		touchDown(-1, -1); // resetting all Buttons
		state = MenuState.BACK;
		loadMenuBackButton.setIsPressed(true);
	}
	
	@Override
	public void drawMenu(SpriteBatch batch){
		for(SimpleElement background : loadMenuBackgrounds){
			background.draw(batch);
		}
		for (SimpleElement element : loadMenuButtons) {
			element.draw(batch);
		}
		for(SimpleElement fileText : files){
			fileText.draw(batch);
		}
	}

}
