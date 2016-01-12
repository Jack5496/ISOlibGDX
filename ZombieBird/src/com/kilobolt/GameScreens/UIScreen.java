package com.kilobolt.GameScreens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.GameWorld.GameRenderer;
import com.kilobolt.ISOHelpers.AssetLoader;

public class UIScreen {

	private GameRenderer renderer;

	public UIScreen(GameRenderer renderer) {
		this.renderer = renderer;
	}

	public void drawGameScreen(SpriteBatch batch) {

	}
	
	public void drawPos(SpriteBatch batch) {
		int w = AssetLoader.whiteRegion.getRegionWidth();
//		int h = AssetLoader.whiteRegion.getRegionHeight();

		int x = renderer.getCamera().getX();
		int y = renderer.getCamera().getY();

		float r = 0.1f;

		AssetLoader.whiteFont.draw(batch, "X: " + x, renderer.getMidPointX() - (w * r) - 3
				* (w * r), 0);
		AssetLoader.whiteFont.draw(batch, "Y: " + y, renderer.getMidPointX() + (w * r), 0);
	}
	
	public void drawFPS(SpriteBatch batch) {
		AssetLoader.whiteFont.draw(batch, "FPS:" + avgFPS, 0, 0);
	}
	
	public void drawZoomLvl(SpriteBatch batch) {
		AssetLoader.whiteFont.draw(batch, "Zoom: " + renderer.getxAmount(), 0, 10);
	}
	
	int avgFPS = 0;
	int cFPS = 0;

	float count = 0;
	
	public void calcFPS(float delta, float runTime) {
		// int aFPS = (int) (1/delta);

		if (runTime > count + 1) {
			avgFPS = cFPS;
			cFPS = 0;
			count = runTime;
			// maxFPS=0;
			// minFPS=1000;
		}
		cFPS++;
	}

}
