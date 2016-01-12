package com.kilobolt.SimpleHelpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.AssetLoader;

public class SimpleText extends SimpleElement {

	public CharSequence layer;
	private int align;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param name
	 * @param align	0:left | 1:centered
	 */
	public SimpleText(float x, float y, String name, int align) {
		super(x, y, AssetLoader.whiteFont.getBounds(name).width,
				AssetLoader.whiteFont.getBounds(name).height);
		this.layer = name;
		this.align = align;
	}
	
	public void setText(String text){
		layer = text;
	}
	
	public String getText(){
		return ""+layer;
	}

	@Override
	public void draw(SpriteBatch batcher) {
		if(align==0){
			AssetLoader.whiteFont.draw(batcher, layer, this.x, this.y);
		}
		else{
			float xpos = this.x -this.width/2;
			float ypos = this.y;
			AssetLoader.whiteFont.draw(batcher, layer, xpos, ypos);
		}
	}
}
