package com.kilobolt.SimpleHelpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;

public class SimpleColoredRectangle extends SimpleElement{
	
	private Color drawColor;
	
	public SimpleColoredRectangle(float x, float y, float width, float height, Color c) {
		
		super(x,y,width, height);
		setColor(c);		
	}
	
	public void setColor(Color c){
		drawColor = new Color(c);	//beware of mutation
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		batch.setColor(drawColor);
		batch.draw(AssetLoader.whiteRegion, x, y, width, height);
		batch.setColor(Color.WHITE);
	}

}
