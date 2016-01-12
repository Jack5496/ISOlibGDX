package com.kilobolt.Menus;
import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.Screens.CameraObject.DIRECTION;
import com.kilobolt.SimpleHelpers.SimpleElement;

public class IngameMenu extends Menu {


	public IngameMenu(InputHandler inputHandler, int midPointX,
			int midPointY) {
		super(inputHandler, midPointX, midPointY);
		
		initButtons(midPointX, midPointY);
	}

	private void initButtons(float midPointX, float midPointY) {
		
	}
	
	@Override
	public void keyHold(){
		if (handler.keys[Keys.RIGHT]) {
			handler.getGameWorld().getPlayer().move(DIRECTION.OR);
		}
		if (handler.keys[Keys.LEFT]) {
			handler.getGameWorld().getPlayer().move(DIRECTION.UL);
		}
		if (handler.keys[Keys.UP]) {
			handler.getGameWorld().getPlayer().move(DIRECTION.OL);
		}
		if (handler.keys[Keys.DOWN]) {
			handler.getGameWorld().getPlayer().move(DIRECTION.UR);
		}		
	}
	
	@Override
	public void keyDown() {
		if (handler.keys[Keys.ESCAPE]) {
			if(WarpController.getInstance().isInLobby()){
				handler.multiPlayerLobbyHandler.switchToMenu();
				handler.getGameWorld().currentState = GameWorld.GameState.PAUSE;
			}
			else{
				handler.singlePlayerHandler.switchToMenu();
				handler.getGameWorld().currentState = GameWorld.GameState.PAUSE;
			}
		}
		if (handler.keys[Keys.SPACE]) {
			handler.getGameWorld().getPlayer().jump = true;
		}
		if (handler.keys[Keys.SHIFT_LEFT]) {
			handler.getGameWorld().getPlayer().doubleSpeed();
		}
		if (handler.keys[Keys.Y]) {
			handler.getGameWorld().getCamera().setTrackingBlock(null);
		}
		if (handler.keys[Keys.X]) {
			handler.getGameWorld().getCamera().setTrackingBlock(handler.getGameWorld().getPlayer());
		}
		if (handler.keys[Keys.ENTER]) {
			handler.getGameWorld().renderer.getCamera().setToZero();
		}
	}
	
	@Override
	public void switchToMenu(){
		handler.activMenu = this;
		handler.getGameWorld().currentState = GameWorld.GameState.RUNNING;
	}
	
	@Override
	public void switchBackToMenu(){
		switchToMenu();
	}
	
	public void touchDown(int screenX, int screenY) {
		boolean buttonPressed = false;

		if (buttonPressed){
			enterActivMenu();
		}
	}

	@Override
	public void drawMenu(SpriteBatch batch){
		for (SimpleElement button : super.MenuElements) {
			button.draw(batch);
		}
	}

}
