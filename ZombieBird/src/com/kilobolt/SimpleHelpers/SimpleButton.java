package com.kilobolt.SimpleHelpers;

import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kilobolt.ISOHelpers.AssetLoader;

public class SimpleButton extends SimpleElement{
	
	private TextureRegion buttonUp;
	private TextureRegion buttonDown;
	
	private CharSequence layer = "P";
	private float layerWidth;
	private float layerHeight;
	
	public SimpleButton(float x, float y, float width, float height,
			TextureRegion buttonUp, TextureRegion buttonDown, String name) {
		
		super(x,y,width, height);
		
		this.buttonUp = buttonUp;
		this.buttonDown = buttonDown;
		
		setText(name);
	}
	
	public void setText(String text){
		this.layer = text;
		TextBounds tb = AssetLoader.whiteFont.getBounds(layer);
		layerWidth = tb.width;
		layerHeight = tb.height;
	}
	
	@Override
	public void draw(SpriteBatch batcher) {
		float oScaleX = AssetLoader.whiteFont.getScaleX();
		float oScaleY = AssetLoader.whiteFont.getScaleY();
		
		float scaleX = width/10/AssetLoader.playButtonUp.getRegionWidth();
		float scaleY = height/-10/AssetLoader.playButtonUp.getRegionHeight();
		
		AssetLoader.whiteFont.setScale(scaleX, scaleY);
		
		TextBounds tb = AssetLoader.whiteFont.getBounds(layer);
		layerWidth = tb.width;
		layerHeight = tb.height;
		
		float xpos = x+width/2-(layerWidth/2);
		float ypos = y-(layerHeight);
		
		if (isPressed) {
			batcher.draw(buttonDown, x, y, width, height);
//			AssetLoader.whiteFont.drawWrapped(batcher, layer, xpos, ypos, t);
			AssetLoader.whiteFont.draw(batcher, layer, xpos, ypos);
		} else {
			batcher.draw(buttonUp, x, y, width, height);
//			AssetLoader.whiteFont.drawWrapped(batcher, layer, xpos, ypos, t);
			AssetLoader.whiteFont.draw(batcher, layer, xpos, ypos);
		}
		
		AssetLoader.whiteFont.setScale(oScaleX, oScaleY);
	}

}
