package com.kilobolt.GameWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.kilobolt.GameObjects.Player;
import com.kilobolt.ISOHelpers.RenderTile;

public class ISOColumn {

	private Vector2 position;
	private ArrayList<Block> arrayList;
	private ISOSurface surf;

	public ISOColumn(ISOSurface surf, int i, int j) {
		this.surf = surf;
		position = new Vector2(i, j);
		arrayList = new ArrayList<Block>();
	}

	public ISOSurface getSurface() {
		return surf;
	}
	
	// Character | already used
	public ISOColumn(ISOSurface surf, int i, int j,String content){
		this.surf = surf;
		position = new Vector2(i, j);

		arrayList = new ArrayList<Block>();
		
		String[] blocks = content.split("#");
		for(String block : blocks){
			arrayList.add(new Block(block,this));
		}
	}

	// Character | already used
	public String toString(){
		String column = "";
		
		for(int i=0; i<arrayList.size();i++){
			if(arrayList.get(i).getClass()==Player.class){
				arrayList.remove(i);
			}
		}
		
		for(int i=0; i<arrayList.size();i++){
			if(arrayList.get(i).getClass()!=Player.class){
				column=column+arrayList.get(i).toString();
				if(i<arrayList.size()-1){
					column=column+"#";
				}
			}
		}
		
		return column;
	}

	public void addBlock(BlockType type) {
		arrayList.add(new Block(type, this));
	}

	public void addBlock(Block block) {
		arrayList.add(block);
	}

	public void addDef() {
		BlockType type = BlockType.DEF;
		arrayList.add(new Block(type, this));
	}

	public void addRandomGrass() {
		BlockType type = BlockType.getRandomGrassBlock();
		arrayList.add(new Block(type, this));
	}
	
	public void addRandomBlocks() {
		BlockType type = BlockType.getRandomBlock();
		arrayList.add(new Block(type, this));
	}

	public int getX() {
		return (int) position.x;
	}

	public int getY() {
		return (int) position.y;
	}

	public Block getTopBlock() {
		if (arrayList.size() == 0)
			return null;
		return arrayList.get(arrayList.size() - 1);
	}

	public void deleteBlock(Block b) {
		if (arrayList.contains(b)) {
			arrayList.remove(b);
		}
	}

	public void updateColoumn(float delta) {
		ArrayList<Block> copy = new ArrayList<Block>(arrayList);
		for (Block b : copy) {
			b.update(delta);
		}
	}
	
	public boolean isBlockTypeIn(BlockType type){
		ArrayList<Block> copy = new ArrayList<Block>(arrayList);
		for(Block b : copy){
			if(b.getBlockType()==type) return true;
		}
		return false;
	}
	
	public boolean isBlockTypeOnTop(BlockType type){
		return arrayList.get(arrayList.size()).getBlockType()==type;
	}

	float onePix = GameRenderer.onePixel;

	public void drawColoumn(List<RenderTile> toDraw, SpriteBatch batcher,
			float xpos, float ypos, float width, float height) {
		float offset = 0;

		Comparator<RenderTile> c = new DepthComparator();

		ArrayList<Block> copy = new ArrayList<Block>(arrayList);
		for (Block b : copy) {
			BlockType type = b.getBlockType();

			// TextureRegion tx = BlockType.getTexture(b.getBlockType());
			float xOff = (32f * onePix) * b.getXPercentage();
			float yOff = (16f * onePix) * b.getYPercentage();

			// batcher.draw(tx, xpos-xOff, ypos-yOff-offset, width, height);

			int index;

			
			
			RenderTile tile = new RenderTile(b, xpos - xOff, ypos - yOff,
					offset);

			index = Collections.binarySearch(toDraw, tile, c);
			if (index < 0)
				index = -(index + 1);
			toDraw.add(index, tile);

			offset += BlockType.getBlockHeight(type) * GameRenderer.onePixel;
		}

	}

	class DepthComparator implements Comparator<RenderTile> {
		@Override
		public int compare(RenderTile tileA, RenderTile tileB) {

//			if (tileA.getDepth(onePix) == tileB.getDepth(onePix)) {
//				if (tileA.offset == tileB.offset) {
//					return 0;
//				}
//				if (tileA.offset < tileB.offset) {
//					return -1;
//				} else {
//					return 1;
//				}
//			}
//			if (tileA.getDepth(onePix) < tileB.getDepth(onePix))
//				return -1;
//			else
//				return 1;

			
			 if (tileA.offset == tileB.offset){
			
			 if (tileA.getDepth(onePix) == tileB.getDepth(onePix))
			 return 0;
			 if (tileA.getDepth(onePix) < tileB.getDepth(onePix))
			 return -1;
			 else
			 return 1;
			 }
			 if (tileA.offset < tileB.offset)
			 return -1;
			 else
			 return 1;
			

		}
	}

}
