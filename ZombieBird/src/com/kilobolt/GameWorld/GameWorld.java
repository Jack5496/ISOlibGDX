package com.kilobolt.GameWorld;

import com.badlogic.gdx.Game;
import com.kilobolt.GameObjects.Player;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.LevelManager.LevelManager;
import com.kilobolt.Screens.CameraObject;

public class GameWorld {

	private Game game;

	private CameraObject camera;
//	private Rectangle ground;
	private int score = 0;
	private float runTime = 0;
	private int midPointY;
	private int midPointX;

	private LevelManager levelManager;
	public InputHandler handler;

	public GameRenderer renderer;

	private ISOSurface surf;
	private Player playerOne;

	public GameState currentState;

	public enum GameState {
		MAINMENU, PAUSE ,RUNNING
	}

	public GameWorld(Game game, int midPointX, int midPointY) {

		this.game = game;

		this.midPointY = midPointY;
		this.midPointX = midPointX;

//		ground = new Rectangle(0, midPointY + 66, 137, 11);

		levelManager = new LevelManager(this);

		initCamera();
		initMenu();
	}

	public void initCamera() {
		camera = new CameraObject(this);
	}

	public void initMenu() {
		camera.setTrackingBlock(null);
		initMenuWorld();
		currentState = GameState.MAINMENU;
	}

	public void initMenuWorld() {
		setSurface(new ISOSurface(20, 30, BlockType.grassBlocks));
		playerOne = new Player(this, BlockType.PLAYER_OR, surf.getColoumn(0, 0));
	}

	public void loadMap(ISOSurface surf) {
		setSurface(surf);
		playerOne = new Player(this, BlockType.PLAYER_OR, surf.getColoumn(0, 0));
		camera.setTrackingBlock(playerOne);
	}

	public void initWorld(int x, int y) {
		setSurface(new ISOSurface(x, y));
		playerOne = new Player(this, BlockType.PLAYER_OR, surf.getColoumn(0, 0));
		camera.setTrackingBlock(playerOne);
	}

	public void update(float delta) {
		// camera.update(delta);
		runTime += delta;
		
		updateMultiPlayerValues(runTime);
		
		switch (currentState) {
		case MAINMENU:
			camera.move(CameraObject.DIRECTION.OR);
			break;
		case PAUSE:
			break;
		case RUNNING:
			surf.updateColumns(delta);
			break;
		default:
			break;
		}

		camera.update(delta);
	}
	
	public void updateMultiPlayerValues(float runTime){
		if(handler!=null){
			handler.updateMenuHandlerValues(runTime);
		}
	}

	public void updateRunning(float delta) {
		if (delta > .15f) {
			delta = .15f;
		}
	}
	
	public Game getGame(){
		return this.game;
	}

	public ISOSurface getSurface() {
		return surf;
	}

	public void setSurface(ISOSurface face) {
		surf = face;
	}

	public int getMidPointY() {
		return midPointY;
	}

	public int getMidPointX() {
		return midPointX;
	}

	public int getScore() {
		return score;
	}

	public Player getPlayer() {
		return playerOne;
	}

	public void addScore(int increment) {
		score += increment;
	}

	public void start() {
		currentState = GameState.RUNNING;
	}

	public void ready() {
//		currentState = GameState.READY;
		renderer.prepareTransition(0, 0, 0, 1f);
	}

	public void restart() {
		score = 0;
		ready();
	}

	public void setRenderer(GameRenderer renderer) {
		this.renderer = renderer;
	}

	public CameraObject getCamera() {
		return this.camera;
	}
	

	public LevelManager getLevelManager() {
		return levelManager;
	}

}
