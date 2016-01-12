package com.kilobolt.GameWorld;

import java.security.SecureRandom;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kilobolt.ISOHelpers.AssetLoader;

/*
 * Block
 * Desc; The general Block class
 * functions as walls, ground, blocks, obstacles
 *
 * Basically anything you can stand on.
 */

public enum BlockType {
	
	GRASS1, GRASS2, GRASS3, DEF, WATER,
	
	PLAYER_OR,PLAYER_UR;
	
	public static BlockType[] grassBlocks = {GRASS1,GRASS2,GRASS3};
	
	public static BlockType fromString(String s){
		switch(s){
		case "GRASS1" : return GRASS1;
		case "GRASS2" : return GRASS2;
		case "GRASS3" : return GRASS3;
		case "DEF"	: return DEF;
		case "WATER"	: return WATER;
		case "PLAYER_OR" : return PLAYER_OR;
		case "PLAYER_UR" : return PLAYER_UR;
		default : return DEF;
		}
	}
	
	public static TextureRegion getTexture(BlockType type){
		switch(type){
		case GRASS1 : return AssetLoader.grass1;
		case GRASS2 : return AssetLoader.grass2;
		case GRASS3 : return AssetLoader.grass3;
		case DEF	: return AssetLoader.def;
		case WATER	: return AssetLoader.water;
		case PLAYER_OR : return AssetLoader.standOR;
		case PLAYER_UR : return AssetLoader.standUR;
		default : return AssetLoader.def;
		}
		
	}
	
	public static float getBlockHeight(BlockType type){
		switch(type){
		case GRASS1 : case GRASS2 : case GRASS3 : case DEF : case WATER : return 8f; 
		case PLAYER_OR : case PLAYER_UR : return 64f;
		default : return 0f;
		}
	}
	
	public static float getBlockWidth(BlockType type){
		switch(type){
		case GRASS1 : case GRASS2 : case GRASS3 : case DEF : case WATER : return 32f; 
		default : return 0f;
		}
	}
	
	public static BlockType getRandomGrassBlock(){
		int x = random.nextInt(grassBlocks.length);
		return grassBlocks[x];
	}
	
	public static BlockType getRandomBlock(){
		float ch = random.nextFloat();
		ch-=0.2;
		if(ch<0) return WATER;
		return getRandomGrassBlock();
	}
	
	

	private static final SecureRandom random = new SecureRandom();
	
}