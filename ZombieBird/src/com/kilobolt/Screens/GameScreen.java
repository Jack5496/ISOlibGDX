package com.kilobolt.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;
import com.kilobolt.GameWorld.GameRenderer;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.Inputs.InputHandler;

public class GameScreen implements Screen {

	public GameWorld world;
	
//	private Game game;
	
	private GameRenderer renderer;
	private float runTime;
	private InputHandler inputHandler;
	
	private float vWidth;
	private float vHeight;
	
//	private float ASPECT_RATIO = (float) vWidth / (float) vHeight;


	// This is the constructor, not the class declaration
	public GameScreen(Game game) {
//		this.game = game;
		
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		
		vWidth = 136;
		vHeight = screenHeight / (screenWidth / vWidth);
		
		int midPointY = (int) (vHeight / 2);
		int midPointX = (int) (vWidth / 2);
		
		world = new GameWorld(game, midPointX, midPointY);

		inputHandler = new InputHandler(world, screenWidth
				/ vWidth, screenHeight / vHeight);
		
		Gdx.input.setInputProcessor(inputHandler);

		renderer = new GameRenderer(world, (int) vWidth, (int) vHeight,
				midPointX, midPointY);
		world.setRenderer(renderer);
		

	}

	@Override
	public void render(float delta) {
		runTime += delta;
		inputHandler.updateLogicInputs();
		world.update(delta);
		renderer.render(delta, runTime);
	}

	
	/**
	 * Resizes the Image on the predefined virtual width and height.
	 * Cuts up the Area and leaves it black :-)
	 */
	@Override
	public void resize(int width, int height) {
		
		Vector2 newVirtualRes= new Vector2(0f, 0f);
		Vector2 crop = new Vector2(width, height);

		// get new screen size conserving the aspect ratio
		newVirtualRes.set(Scaling.fit.apply((float)vWidth, (float)vHeight, (float)width, (float)height));

		// ensure our scene is centered in screen
		crop.sub(newVirtualRes);
		crop.mul(.5f);

		// build the viewport for further application
		Rectangle viewport = new Rectangle(crop.x, crop.y, newVirtualRes.x, newVirtualRes.y);
	    
	    Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
	            (int) viewport.width, (int) viewport.height);	

	   
	    //ScreenSize changed, so an offset for touch must be set	    
	    inputHandler.updateOffset(crop.x ,crop.y);
	    
	    
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}
