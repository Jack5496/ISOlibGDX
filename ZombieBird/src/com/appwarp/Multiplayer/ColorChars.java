package com.appwarp.Multiplayer;

import com.badlogic.gdx.graphics.Color;

public class ColorChars{

	public static Color getColorFromChar(char c){
		switch(""+c){
		case "r" : return Color.RED;
		case "g" : return Color.GREEN;
		case "c" : return Color.CYAN;
		case "b" : return Color.BLACK;
		case "y" : return Color.YELLOW;
		case "w" : return Color.WHITE;
		}
		return null;
	}
	
	public static boolean isColorChar(char c){
		Color col = getColorFromChar(c);
		return col!=null;
	}
	
}
