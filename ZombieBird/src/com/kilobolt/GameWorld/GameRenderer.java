package com.kilobolt.GameWorld;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.kilobolt.GameScreens.ControlButtonsScreen;
import com.kilobolt.GameScreens.UIScreen;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.ISOHelpers.RenderTile;
import com.kilobolt.Inputs.InputHandler;
import com.kilobolt.Screens.CameraObject;
import com.kilobolt.TweenAccessors.Value;
import com.kilobolt.TweenAccessors.ValueAccessor;

public class GameRenderer {

	private GameWorld myWorld;

	private OrthographicCamera cam; // Real Camera to render, no need to edit
	private ShapeRenderer shapeRenderer;
	private SpriteBatch batcher;

	private OrthographicCamera camUI; // User Interface Camera, camera should be
										// static
	private ShapeRenderer shapeRendererUI;
	private SpriteBatch batcherUI;

	// MidPoints off Screen
	private int midPointY;
	private int midPointX;

	public static float onePixel = 0;

	private float sw;

	// Game Objects
	private CameraObject camera; // Virtual Camera for moving screen

	// Game Assets
	private TextureRegion blockP, playerLogo;
	
	// Tween stuff
	private TweenManager manager;
	private Value alpha = new Value();

	// Buttons
	private InputHandler inputHandler;
	private Color transitionColor;

	private UIScreen uiScreen;
	private ControlButtonsScreen controlButtonsScreen;
	
	public GameRenderer(GameWorld world, int gameWidth, int gameHeight,
			int midPointX, int midPointY) {
		
		myWorld = world;
		// this.surf = myWorld.getSurface();

		this.midPointY = midPointY;
		this.setMidPointX(midPointX);
		sw = getMidPointX() * 2;

		this.inputHandler = (InputHandler) Gdx.input.getInputProcessor();

		cam = new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.setToOrtho(true, gameWidth, gameHeight);
		cam.update();

		batcher = new SpriteBatch();
		batcher.setProjectionMatrix(cam.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(cam.combined);

		setCamUI(new OrthographicCamera(Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight()));
		getCamUI().setToOrtho(true, gameWidth, gameHeight);
		getCamUI().position.set(midPointX, midPointY, 0);
		// getCamUI().position.set(0, 0, 0);
		getCamUI().update();

		setBatcherUI(new SpriteBatch());
		getBatcherUI().setProjectionMatrix(getCamUI().combined);
		shapeRendererUI = new ShapeRenderer();
		shapeRendererUI.setProjectionMatrix(getCamUI().combined);

		initGameObjects();
		initAssets();
		initScreen();

		transitionColor = new Color();
		prepareTransition(255, 255, 255, .5f);
	}
	
	private void initScreen(){
		this.uiScreen = new UIScreen(this);
		this.controlButtonsScreen = new ControlButtonsScreen(this);
	}

	private void initGameObjects() {
		camera = myWorld.getCamera();
	}

	public void moveCam(float x, float y) {
		getCamUI().translate(x, y);
		getCamUI().update();
		getBatcherUI().setProjectionMatrix(getCamUI().combined);
	}

	private void initAssets() {
//		bg = AssetLoader.bg;
//		grass = AssetLoader.grass;
//		birdAnimation = AssetLoader.birdAnimation;
//		birdMid = AssetLoader.bird;
//		skullUp = AssetLoader.skullUp;
//		skullDown = AssetLoader.skullDown;
//		bar = AssetLoader.bar;
//		ready = AssetLoader.ready;
//		zbLogo = AssetLoader.zbLogo;
//		gameOver = AssetLoader.gameOver;
//		highScore = AssetLoader.highScore;
//		scoreboard = AssetLoader.scoreboard;
//		retry = AssetLoader.retry;
//		star = AssetLoader.star;
//		noStar = AssetLoader.noStar;
		blockP = AssetLoader.block;
		playerLogo = AssetLoader.playerLogo;
	}

	public void render(float delta, float runTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		drawGame(runTime, batcher);
		drawUI(delta, runTime, getBatcherUI());

		drawTransition(delta);
	}

	public void drawGame(float runTime, SpriteBatch batcher) {
		drawBlock(runTime, batcher);
	}

	public void drawUI(float delta, float runTime, SpriteBatch batcher) {

		batcher.begin();

		drawMenuUI(batcher);	//checks inside which menu to draw optionally
		
		if (myWorld.currentState == GameWorld.GameState.RUNNING) {
			drawPlayerFace(batcher);
			
			uiScreen.calcFPS(delta, runTime);
			uiScreen.drawFPS(batcher);
			uiScreen.drawPos(batcher);
			uiScreen.drawZoomLvl(batcher);
		}

		controlButtonsScreen.drawTouches(batcher);

		batcher.end();
	}

	private void drawMenuUI(SpriteBatch batch) {
		inputHandler.activMenu.drawMenu(batch);
	}

	private void drawPlayerFace(SpriteBatch batch) {
		int w = playerLogo.getRegionWidth();
		int h = playerLogo.getRegionHeight();

		float r = 0.1f;

		batch.draw(playerLogo, 1.95f * midPointX - w * r, 0.05f * midPointY, w
				* r, h * r);
	}

	public void zoom(int amount) {
		if (amount < 0)
			zoomIn();
		else
			zoomOut();
	}

	private void zoomIn() {
		if (getxAmount() > 5)
			setxAmount(getxAmount() - 2);
		else
			setxAmount(3);
	}

	private void zoomOut() {
		if (getxAmount() < 19)
			setxAmount(getxAmount() + 2);
	}

	private float xAmount = 9f;
	float yAmount = 0;

	float xOff = 0;
	float yOff = 0;

	private void drawBlock(float runTime, SpriteBatch batch) {

		onePixel = (float) sw / (float) ((getxAmount() * 32.0f) + 2f); // breite
																	// eines
																	// Pixels
		float x = onePixel * 34f; // breite eines teiles

		float y = (x); // höhe eines teiles
		float yh = (float) (16) / blockP.getRegionWidth();
		yh = yh * (x);

		xOff = (32.0f * onePixel) * camera.getXPercentage();
		yOff = 16f * onePixel * camera.getYPercentage();

		yAmount = (int) (2.0f * midPointY / yh);

		
		
		List<RenderTile> toDraw = new ArrayList<RenderTile>();

		orderTilesToRender(toDraw, x, y, yh);
		drawTilesToRender(toDraw, batch, x, y);
	}
	
	public void drawTilesToRender(List<RenderTile> tileList, SpriteBatch batch, float x, float y){
		batcher.begin();

		for (RenderTile tile : tileList) {
			batcher.draw(BlockType.getTexture(tile.block.getBlockType()),
					tile.xpos, tile.ypos - tile.offset, x, y);
		}

		batcher.end();
	}
	
	public void orderTilesToRender(List<RenderTile> tileList, float x, float y, float yh){
		for (int j = (int) -yAmount - 2; j < yAmount + 2; j++) {
			for (int i = (int) -getxAmount() - 2; i < getxAmount() + 2; i++) {
				float xi = getTilePositionX((int) (i + getxAmount() - 1), (int) (j),
						onePixel, yh); // X Position in screen
				float xj = getTilePositionY((int) (i + getxAmount() - 1), (int) (j),
						onePixel, yh); // Y Position in screen
				
				myWorld.getSurface().drawColoumn(tileList,
						i + getCamera().getX(), j + getCamera().getY(),
						getBatcherUI(), xi + xOff, xj + yOff, x, y);
			}
		}
	}

	public float getTilePositionX(float dx, float dy, float xd, float yh) {
		return (dx - dy) * 32 * xd / 2.0f;
	}

	public float getTilePositionY(float dx, float dy, float xd, float yh) {
		return (dx + dy) * yh / 2.0f;
	}
	
	public void prepareTransition(int r, int g, int b, float duration) {
		transitionColor.set(r / 255.0f, g / 255.0f, b / 255.0f, 1);
		alpha.setValue(1);
		Tween.registerAccessor(Value.class, new ValueAccessor());
		manager = new TweenManager();
		Tween.to(alpha, -1, duration).target(0)
				.ease(TweenEquations.easeOutQuad).start(manager);
	}

	private void drawTransition(float delta) {
		if (alpha.getValue() > 0) {
			manager.update(delta);
			Gdx.gl.glEnable(GL10.GL_BLEND);
			Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(transitionColor.r, transitionColor.g,
					transitionColor.b, alpha.getValue());
			shapeRenderer.rect(0, 0, 136, 300);
			shapeRenderer.end();
			Gdx.gl.glDisable(GL10.GL_BLEND);
		}
	}

	public SpriteBatch getBatcherUI() {
		return batcherUI;
	}

	public void setBatcherUI(SpriteBatch batcherUI) {
		this.batcherUI = batcherUI;
	}

	public int getMidPointX() {
		return midPointX;
	}

	public void setMidPointX(int midPointX) {
		this.midPointX = midPointX;
	}

	public int getMidPointY() {
		return midPointX;
	}

	public void setMidPointY(int midPointY) {
		this.midPointY = midPointY;
	}

	public OrthographicCamera getCamUI() {
		return camUI;
	}

	public void setCamUI(OrthographicCamera camUI) {
		this.camUI = camUI;
	}

	public CameraObject getCamera() {
		return camera;
	}

	public void setCamera(CameraObject camera) {
		this.camera = camera;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}

	public float getxAmount() {
		return xAmount;
	}

	public void setxAmount(float xAmount) {
		this.xAmount = xAmount;
	}

	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}

	

}
