package com.kilobolt.SimpleHelpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.kilobolt.ISOHelpers.AssetLoader;

public class SimpleTextArea {

	public float x, y, width, height;

	private TextureRegion buttonUp;
	private TextureRegion buttonDown;
	
	private Rectangle bounds;

	private boolean isPressed = false;
	
	private CharSequence layer = "P";
	private float layerWidth;
	private float layerHeight;
	
	public SimpleTextArea(float x, float y, float width, float height,
			TextureRegion buttonUp, TextureRegion buttonDown, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.buttonUp = buttonUp;
		this.buttonDown = buttonDown;
		
		this.layer = name;
		
		TextBounds tb = AssetLoader.whiteFont.getBounds(layer);
		layerWidth = tb.width;
		layerHeight = tb.height;
		
		bounds = new Rectangle(x, y, width, height);
	}
	
	public void setIsPressed(boolean b){
		isPressed = b;
	}

	public boolean isClicked(int screenX, int screenY) {
		return bounds.contains(screenX, screenY);
	}

	public void draw(SpriteBatch batcher) {
		float xpos = x+width/2-(layerWidth/2);
		float ypos = y-(layerHeight);
		if (isPressed) {
			batcher.draw(buttonDown, x, y, width, height);
			AssetLoader.whiteFont.draw(batcher, layer, xpos, ypos);
		} else {
			batcher.draw(buttonUp, x, y, width, height);
			AssetLoader.whiteFont.draw(batcher, layer, xpos, ypos);
		}
	}

	public boolean isTouchDown(int screenX, int screenY) {
		if (bounds.contains(screenX, screenY)) {
			isPressed = true;
			return true;
		}

		isPressed = false;
		return false;
	}

	public boolean isTouchUp(int screenX, int screenY) {
		
		// It only counts as a touchUp if the button is in a pressed state.
		if (bounds.contains(screenX, screenY) && isPressed) {
			isPressed = false;
			AssetLoader.flap.play();
			return true;
		}
		
		// Whenever a finger is released, we will cancel any presses.
		isPressed = false;
		return false;
	}

}
