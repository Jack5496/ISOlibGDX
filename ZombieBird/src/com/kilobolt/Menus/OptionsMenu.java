package com.kilobolt.Menus;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.SimpleHelpers.Settings;
import com.kilobolt.SimpleHelpers.SimpleButton;
import com.kilobolt.SimpleHelpers.SimpleElement;
import com.kilobolt.SimpleHelpers.SimpleSettingsInput;


public class OptionsMenu extends Menu{
	
	private SimpleButton NameButton;
	private SimpleButton SoundButton;
	private SimpleButton FriendsButton;
	private SimpleButton BackButton;
	
		
	public enum MenuState {
		NAME, SOUND, FRIENDS,  BACK;
	}
	
	private MenuState state;
	
	public OptionsMenu(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);
		
		state = MenuState.NAME;
		initButtons(midPointX, midPointY);
	}
		
	public void initButtons(float midPointX, float midPointY){
		NameButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (2f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "NAME");
		NameButton.setIsPressed(true);

		SoundButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (6f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "SOUND");
		setSoundButtonNameIfSoundE();		
		

		FriendsButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (10f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "FREINDS");
		
		BackButton = new SimpleButton(midPointX
				- (AssetLoader.playButtonUp.getRegionWidth() / 2), (14f / 8f)
				* midPointY - (AssetLoader.playButtonUp.getRegionHeight() / 2),
				AssetLoader.playButtonUp.getRegionWidth(),
				AssetLoader.playButtonUp.getRegionHeight(),
				AssetLoader.playButtonUp, AssetLoader.playButtonDown, "BACK");

		super.MenuElements.add(NameButton);
		super.MenuElements.add(SoundButton);
		super.MenuElements.add(FriendsButton);
		super.MenuElements.add(BackButton);
	}
	
	private void setSoundButtonNameIfSoundE(){
		if(Settings.getSoundEnabled()){
			SoundButton.setText("SOUND");
		}
		else{
			SoundButton.setText("NO SOUND");
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
			handler.lastMenu = this;
			handler.mainMenuHandler.switchToMenu();
		}
	}

	@Override
	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (NameButton.isTouchDown(screenX, screenY)) {
			state = MenuState.NAME;
			buttonPressed = true;
		}
		if(SoundButton.isTouchDown(screenX, screenY)){
			state= MenuState.SOUND;
			buttonPressed = true;
		}
		if(FriendsButton.isTouchDown(screenX, screenY)){
			state= MenuState.FRIENDS;
			buttonPressed = true;
		}
		if (BackButton.isTouchDown(screenX, screenY)) {
			state = MenuState.BACK;
			buttonPressed = true;
		}
		
		if (buttonPressed)
			enterActivMenu();
	}
	
	@Override
	public void switchToMenu(){		
		handler.activMenu = this;
		touchDown(-1,-1);
		state = MenuState.NAME;
		NameButton.setIsPressed(true);
	}
	
	@Override
	public void switchBackToMenu(){
		switchToMenu();
	}
	
	@Override
	public void enterActivMenu(){
		switch(state){
		case NAME:
			SimpleSettingsInput.getInput(Settings.userNameKey,"Name",Settings.userName);
			break;
		case SOUND:
			Settings.setSetting(Settings.soundEnabledKey, ""+!Settings.getSoundEnabled());
			setSoundButtonNameIfSoundE();
			break;
		case FRIENDS:
			handler.lastMenu = this;
			handler.friendsMenuHandler.switchToMenu();
			break;
		case BACK:
			handler.lastMenu = this;
			handler.mainMenuHandler.switchToMenu();
			break;
		}
	}
	
	

	public void menuDown() {
		touchDown(-1, -1); // resetting all Buttons
		switch(state){
		case NAME: 
			SoundButton.setIsPressed(true);
			state = MenuState.SOUND;
			break;
		case SOUND: 
			FriendsButton.setIsPressed(true);
			state = MenuState.FRIENDS;
			break;
		case FRIENDS: 
			BackButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case BACK: 
			NameButton.setIsPressed(true);
			state = MenuState.NAME;
			break;
		}
	}

	public void menuUp() {
		touchDown(-1, -1); // resetting all Buttons
		switch(state){
		case NAME: 
			BackButton.setIsPressed(true);
			state = MenuState.BACK;
			break;
		case SOUND: 
			NameButton.setIsPressed(true);
			state = MenuState.NAME;
			break;
		case FRIENDS: 
			SoundButton.setIsPressed(true);
			state = MenuState.SOUND;
			break;
		case BACK: 
			FriendsButton.setIsPressed(true);
			state = MenuState.FRIENDS;
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
