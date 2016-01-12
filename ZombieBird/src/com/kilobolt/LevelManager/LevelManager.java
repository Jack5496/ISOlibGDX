package com.kilobolt.LevelManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kilobolt.GameWorld.GameWorld;
import com.kilobolt.GameWorld.ISOSurface;
import com.kilobolt.Menus.LoadMenu;

public class LevelManager {

	private GameWorld world;
	
	public LevelManager(GameWorld world){
		this.world = world;
	}
	
	private static String mapsPath = "assets/data/maps/";
	
	public void saveMap(){

		FileHandle f = Gdx.files.local(mapsPath+"map.ser");
		FileHandle file = Gdx.files.absolute(f.file().getAbsolutePath());
		
		String map = world.getSurface().surfaceToString();
		
		file.writeString(map, false);
		
		System.out.println("File Saved");
	}
	
	public FileHandle[] getAllMaps(){
		FileHandle file = Gdx.files.absolute(mapsPath);
		FileHandle[] files = file.list();
		return files;
	}
	
	public void loadMap(){
		int pointer = LoadMenu.pointer;
		FileHandle[] files = LoadMenu.getAllMapFile();
		
		String map = files[pointer].readString();
		
		world.loadMap(new ISOSurface(map));
	}
	
}