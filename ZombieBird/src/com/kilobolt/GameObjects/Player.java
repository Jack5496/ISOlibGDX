package com.kilobolt.GameObjects;

import com.kilobolt.GameWorld.Block;
import com.kilobolt.GameWorld.BlockType;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.ISOColumn;
import com.kilobolt.Screens.CameraObject.DIRECTION;

public class Player extends Block{

	private String name;
	private GameWorld world;
	public boolean jump;
	
	public Player(GameWorld world, BlockType type, ISOColumn col){
		super(type, col);
		this.world = world;
		this.name = "Dallas";
		col.addBlock(this);
	}	
	
	public String getName(){
		return name;
	}
	
	public GameWorld getGameWorld(){
		return world;
	}
	
	@Override
	public boolean allowedToMove(DIRECTION dir){
		if(jump){
			if(canJump(dir) && !canGo(dir)){
				super.type = BlockType.PLAYER_UR;
				super.amount = 2;
				return true;
			}
		}
		super.type = BlockType.PLAYER_OR;
		super.amount = 1;
		return canGo(dir);
	}
	
	public boolean canJump(DIRECTION dir){
		int x = 0;
		int y = 0;
		
		switch(dir){
		case OL : x-=2; break;
		case UL : y+=2; break;
		case OR : y-=2; break;
		case UR : x+=2; break;
		}
		
		ISOColumn col = getColumnFromOffset(x,y);
		return (!col.isBlockTypeIn(BlockType.WATER));
	}
	
	public boolean canGo(DIRECTION dir){
		int x = 0;
		int y = 0;
		
		switch(dir){
		case OL : x-=1; break;
		case UL : y+=1; break;
		case OR : y-=1; break;
		case UR : x+=1; break;
		}
		
		ISOColumn col = getColumnFromOffset(x,y);
		return (!col.isBlockTypeIn(BlockType.WATER));
	}

	public void doubleSpeed() {
		super.setSpeed(super.getNormalSpeed()*2);
	}

	public void resetToNormalSpeed() {
		super.resetToNormalSpeed();		
	}
	
}
