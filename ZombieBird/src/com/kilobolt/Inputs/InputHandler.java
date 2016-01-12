package com.kilobolt.Inputs;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.ISOHelpers.TouchInfo;
import com.kilobolt.Menus.FriendsMenu;
import com.kilobolt.Menus.IngameMenu;
import com.kilobolt.Menus.LoadMenu;
import com.kilobolt.Menus.MainMenu;
import com.kilobolt.Menus.Menu;
import com.kilobolt.Menus.MultiPlayerCreate;
import com.kilobolt.Menus.MultiPlayerLobby;
import com.kilobolt.Menus.MultiPlayerMenu;
import com.kilobolt.Menus.MultiPlayerSearch;
import com.kilobolt.Menus.OptionsMenu;
import com.kilobolt.Menus.SinglePlayerMenu;
import com.kilobolt.Screens.CameraObject.DIRECTION;
import com.kilobolt.SimpleHelpers.FriendList;

public class InputHandler implements InputProcessor, GestureListener {
	private GameWorld myWorld;

	private Map<Integer, TouchInfo> touches = new HashMap<Integer, TouchInfo>();

	public Menu activMenu;
	
	public MainMenu mainMenuHandler;
	public SinglePlayerMenu singlePlayerHandler;
	public IngameMenu ingameMenuHandler;
	public MultiPlayerMenu multiPlayerMenuHandler;
	public MultiPlayerLobby multiPlayerLobbyHandler;	
	public MultiPlayerCreate multiPlayerCreateHandler;
	public MultiPlayerSearch multiPlayerSearchHandler;	
	public LoadMenu loadMenuHandler;
	public OptionsMenu optionsMenuHandler;
	public FriendsMenu friendsMenuHandler;
	
	public Menu lastMenu;
	
	private float scaleFactorX;
	private float scaleFactorY;

	private float offsetX = 0;
	private float offsetY = 0;

	public boolean[] keys = new boolean[256];
	long[] keysTime = new long[256];

	boolean useTouch = true;
	boolean movePadActiv;

	public InputHandler(GameWorld myWorld, float scaleFactorX,
			float scaleFactorY) {
		this.myWorld = myWorld;

		this.scaleFactorX = scaleFactorX;
		this.scaleFactorY = scaleFactorY;
		
		int midPointY = myWorld.getMidPointY();
		int midPointX = myWorld.getMidPointX();

		initHandlers(midPointX,midPointY);
		
		activMenu = mainMenuHandler;
		
		for (int i = 0; i < 5; i++) {
			touches.put(i, new TouchInfo());
		}
		updateMovePadActiv();
		myWorld.handler = this;
	}
	
	private void initHandlers(int midPointX, int midPointY){
		mainMenuHandler = new MainMenu(this, midPointX, midPointY);
		singlePlayerHandler = new SinglePlayerMenu(this, midPointX, midPointY);
		ingameMenuHandler = new IngameMenu(this, midPointX, midPointY);
		multiPlayerMenuHandler = new MultiPlayerMenu(this, midPointX, midPointY);
		multiPlayerLobbyHandler = new MultiPlayerLobby(this, midPointX, midPointY);
		multiPlayerCreateHandler = new MultiPlayerCreate(this, midPointX, midPointY);
		multiPlayerSearchHandler = new MultiPlayerSearch(this, midPointX, midPointY);
		loadMenuHandler = new LoadMenu(this, midPointX, midPointY);
		optionsMenuHandler = new OptionsMenu(this, midPointX, midPointY);
		friendsMenuHandler = new FriendsMenu(this, midPointX, midPointY);
	}
	
	public void updateMenuHandlerValues(float runTime){
		this.multiPlayerMenuHandler.updateMPValues(runTime);
		this.multiPlayerLobbyHandler.updateMPValues(runTime);
		this.multiPlayerSearchHandler.updateMPValues(runTime);
		this.friendsMenuHandler.updateMPValues(runTime);
		FriendList.updateValues(runTime);
	}

	public void updateMovePadActiv() {
		switch (Gdx.app.getType()) {
		case Android:
			movePadActiv = true;
		case Applet:
			movePadActiv = true;
		case Desktop:
			movePadActiv = useTouch;
		case WebGL:
			movePadActiv = true;
		case iOS:
			movePadActiv = true;
		}
	}

	/**
	 * Calculates new ScalingFactor with new ScreenSize and the offsets for the
	 * black bars
	 * 
	 * @param offsetX
	 * @param offsetY
	 */
	public void updateOffset(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.scaleFactorX = (Gdx.graphics.getWidth() - offsetX * 2)
				/ (float) (myWorld.getMidPointX() * 2);
		this.scaleFactorY = (Gdx.graphics.getHeight() - offsetY * 2)
				/ (float) (myWorld.getMidPointY() * 2);
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		System.out.println("TouchDown 1");
		// return touchDown((int)x,(int)y,pointer, button);
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		screenX -= offsetX;
		screenY -= offsetY;

		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (screenX < myWorld.getMidPointX() * 2 / 3) {
			if (!TouchInfo.aMovePadActiv) {
				touches.get(pointer).movePad = true;
				TouchInfo.aMovePadActiv = true;
			}
		}

		if (pointer < 5) {
			touches.get(pointer).startPos.x = screenX;
			touches.get(pointer).startPos.y = screenY;
			touches.get(pointer).lastPos.x = screenX;
			touches.get(pointer).lastPos.y = screenY;
			touches.get(pointer).touched = true;
		}

		activMenu.touchDown(screenX, screenY);
		
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		screenX -= offsetX;
		screenY -= offsetY;

		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		touches.get(pointer).lastPos.x = screenX;
		touches.get(pointer).lastPos.y = screenY;
		touches.get(pointer).touched = false;
		if (touches.get(pointer).movePad) {
			touches.get(pointer).movePad = false;
			TouchInfo.aMovePadActiv = false;
		}

		return false;
	}

	public void updateLogicInputs() {
		updateTouchInputs();
		updateKeyInputs();
	}

	int r = 10; // normal touches
	int br = 20;// touch knob base
	int md = br - r;// touch knob base

	public void updateTouchInputs() {
		for (int pointer = 0; pointer < 5; pointer++) {
			if (movePadActiv)
				updateTouchMovePadInputs(pointer);
		}
	}

	public void updateTouchMovePadInputs(int pointer) {

		if (touches.get(pointer).movePad) {

			float dist = touches.get(pointer).getDistance();
			if (dist > md) {
				dist = md;
			}
			float power = dist / md;
			double angle = touches.get(pointer).getVelocityAngleRadian();
			float xr = (float) Math.cos(angle) * power;
			float yr = (float) Math.sin(angle) * power;

			if ((int) (100 * dist / md) >= 98) {

				if (xr < 0) {
					if (yr < 0) {
						myWorld.getPlayer().move(DIRECTION.OL);
					} else {
						myWorld.getPlayer().move(DIRECTION.UL);
					}
				} else {
					if (yr < 0) {
						myWorld.getPlayer().move(DIRECTION.OR);
					} else {
						myWorld.getPlayer().move(DIRECTION.UR);
					}
				}

			}
		}

	}

	/**
	 * Updates Variables every Frame
	 */
	public void updateKeyInputs() {
		activMenu.keyHold();
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		screenX -= offsetX;
		screenY -= offsetY;

		screenX = scaleX(screenX);
		screenY = scaleY(screenY);

		if (pointer < 5) {
			touches.get(pointer).lastPos.x = screenX;
			touches.get(pointer).lastPos.y = screenY;
			touches.get(pointer).touched = true;
		}
		return true;
	}

	/**
	 * Updates every Key Input
	 */
	@Override
	public boolean keyDown(int keycode) {
		keys[keycode] = true;
		keysTime[keycode] = System.currentTimeMillis();

		activMenu.keyDown();

		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keys[keycode] = false;

		if (keycode == Keys.SHIFT_LEFT) {
			myWorld.getPlayer().resetToNormalSpeed();
			System.out.println("Reset Speed");
		}
		if (keycode == Keys.SPACE) {
			myWorld.getPlayer().jump = false;
			System.out.println("Jump End");
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		myWorld.renderer.zoom(amount);
		return true;
	}

	private int scaleX(int screenX) {
		return (int) (screenX / scaleFactorX);
	}

	private int scaleY(int screenY) {
		return (int) (screenY / scaleFactorY);
	}

	public Map<Integer, TouchInfo> getTouches() {
		return touches;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		System.out.println("Tap");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		System.out.println("Long Press");
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		System.out.println("Fling");
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		System.out.println("Pan");
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		System.out.println("PanStop");
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		System.out.println("Zoom");
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		System.out.println("Pinch");
		return false;
	}

	public GameWorld getGameWorld() {
		return myWorld;
	}
}
