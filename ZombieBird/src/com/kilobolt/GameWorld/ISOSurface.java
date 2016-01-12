package com.kilobolt.GameWorld;

import java.security.SecureRandom;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.ISOHelpers.RenderTile;

public class ISOSurface {

	private ISOColumn[][] colArray;
	
	public final int WIDTH;
	public final int HEIGHT;
	
	public ISOSurface(int w, int h) {
		WIDTH = w;
		HEIGHT = h;

		colArray = new ISOColumn[WIDTH][HEIGHT];

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j] = new ISOColumn(this, i, j);
				if((i)==0 || j==0){
					colArray[i][j].addDef();
					if(j==3){
						colArray[i][j].addDef();
					}
					if(j==2){
						colArray[i][j].addDef();
						colArray[i][j].addDef();
					}
					if(j==1){
						colArray[i][j].addDef();
						colArray[i][j].addDef();
						colArray[i][j].addDef();
					}
				}
				else{
//					colArray[i][j].addRandomGrass();
					colArray[i][j].addRandomBlocks();
				}
			}
		}
	}
	
	static String mapSizeSplitt = "x";
	static String mapPartSplitt = ":";
	
	public String surfaceToString(){
		String map = WIDTH+mapSizeSplitt+HEIGHT;
		
		for(int i=0; i<WIDTH; i++){
			for(int j=0; j<HEIGHT; j++){
				map=map+mapPartSplitt+colArray[i][j].toString();
			}
		}
		
		return map;
	}
	
	/**
	 * 
	 * @param map
	 * @return 0 WIDTH | 1 HEIGHT
	 */
	public int[] getMapSize(String map){
		String[] size = getSizeString(map).split(mapSizeSplitt);
		int[] IntSize = {Integer.parseInt(size[0]), Integer.parseInt(size[1])};
		return IntSize;
	}

	public String[] getMapSplitted(String map){
		String[] splits = map.split(mapPartSplitt);
		return splits;
	}
	
	public String getSizeString(String map){
		return getMapSplitted(map)[0];
	}
	
	public ISOSurface(String map){
		String[] splits = getMapSplitted(map);
		
		int[] IntSize = getMapSize(map);
		WIDTH = IntSize[0];
		HEIGHT = IntSize[1];

		colArray = new ISOColumn[WIDTH][HEIGHT];
		
		int pos = 1;
		
		for(int i=0; i<WIDTH; i++){
			for(int j=0; j<HEIGHT; j++){
				colArray[i][j] = new ISOColumn(this, i, j, splits[pos]);
				pos++;
			}
		}
	}
		
	
	ISOSurface(int w, int h, BlockType[] blocks) {		
		final SecureRandom random = new SecureRandom();
		
		WIDTH = w;
		HEIGHT = h;
		colArray = new ISOColumn[WIDTH][HEIGHT];

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j] = new ISOColumn(this, i, j);
				colArray[i][j].addBlock(blocks[random.nextInt(blocks.length)]);
			}
		}
	}
	
	public ISOColumn getColoumn(int x, int y){
		int dx = x%WIDTH;
		if(dx<0) dx = WIDTH+dx;
		
		int dy = y%HEIGHT;
		if(dy<0) dy = HEIGHT+dy;
		
		return colArray[dx][dy];
	}
	
	public Block getTopBlock(int x, int y){
		return getColoumn(x,y).getTopBlock();
	}
	
	public void drawColoumn(List<RenderTile> toDraw, int x, int y, SpriteBatch batcher, float xpos, float ypos, float width, float height){
		getColoumn(x,y).drawColoumn(toDraw, batcher, xpos, ypos, width, height);
	}
	
	public void updateColumns(float delta){
		for(int i = 0; i<WIDTH; i++){
			for(int j = 0; j<HEIGHT; j++){
				getColoumn(i,j).updateColoumn(delta);
			}
		}
	}	
}
