package com.kilobolt.ISOHelpers;

import com.kilobolt.GameWorld.Block;
import com.kilobolt.GameWorld.GameRenderer;

public class RenderTile {

	public Block block;
	public float offset;
	public float xpos;
	public float ypos;

	public RenderTile(Block b, float xpos, float ypos, float offset) {
		this.block = b;
		this.xpos = xpos;
		this.ypos = ypos;
		this.offset = offset;
	}
	
	public float getDepth(float onePix){
		float yOff = (16f * GameRenderer.onePixel) * block.getYPercentage();
//		return -(ypos-yOff - offset);
		
		return (ypos-yOff);
	}

}
