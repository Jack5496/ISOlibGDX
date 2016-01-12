package com.kilobolt.ISOHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetLoader {

	public static Texture texture, logoTexture;
	public static Texture touchKnob;
	public static TextureRegion logo, zbLogo, bg, grass, bird, birdDown,
			birdUp, skullUp, skullDown, bar, playButtonUp, playButtonDown,
			ready, gameOver, highScore, scoreboard, star, noStar, retry, block;
	
	public static Texture numberTexture;
	public static TextureRegion zero,one,two,three,four,five,six,seven,eight,nine;
	
	public static TextureRegion whiteRegion;
	
	public static Texture playerFace;
	public static TextureRegion playerLogo;
	
	public static Animation birdAnimation;
	public static Sound dead, flap, coin, fall;
	public static BitmapFont font, shadow, whiteFont;
	private static Preferences prefs;
	
	public static Skin skin;
	
	public static void load() {
		//TEXTURES has to be in Formtat: 2^x * 2^y
		loadNumbers();
		loadBlocks();
		loadPlayerTextures();
	
		logoTexture = new Texture(Gdx.files.internal("data/logo.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		logo = new TextureRegion(logoTexture, 0, 0, 512, 114);

		texture = new Texture(Gdx.files.internal("data/texture.png"));
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		FileHandle test = Gdx.files.internal("data/touchBackground.png");
		System.out.println(test.exists());
		touchKnob = new Texture(test);	
		touchKnob.setFilter(TextureFilter.Linear, TextureFilter.Nearest);

		playButtonUp = new TextureRegion(texture, 0, 83, 29, 16);
		playButtonUp.flip(false, true);
		
		playButtonDown = new TextureRegion(texture, 29, 83, 29, 16);
		playButtonDown.flip(false, true);
		
		playerFace = new Texture(Gdx.files.internal("data/playerLogo.png"));
		playerLogo = new TextureRegion(playerFace, 0, 0, playerFace.getWidth(), playerFace.getHeight());
		playerLogo.flip(false, true);
		
		ready = new TextureRegion(texture, 59, 83, 34, 7);
		ready.flip(false, true);

		retry = new TextureRegion(texture, 59, 110, 33, 7);
		retry.flip(false, true);
		
		gameOver = new TextureRegion(texture, 59, 92, 46, 7);
		gameOver.flip(false, true);

		scoreboard = new TextureRegion(texture, 111, 83, 97, 37);
		scoreboard.flip(false, true);

		star = new TextureRegion(texture, 152, 70, 10, 10);
		noStar = new TextureRegion(texture, 165, 70, 10, 10);

		star.flip(false, true);
		noStar.flip(false, true);

		highScore = new TextureRegion(texture, 59, 101, 48, 7);
		highScore.flip(false, true);

		zbLogo = new TextureRegion(texture, 0, 55, 135, 24);
		zbLogo.flip(false, true);

		bg = new TextureRegion(texture, 0, 0, 136, 43);
		bg.flip(false, true);
		
		block = new TextureRegion(texture, 0, 100, 34, 25);
		block.flip(false, true);

		grass = new TextureRegion(texture, 0, 43, 143, 11);
		grass.flip(false, false);

		birdDown = new TextureRegion(texture, 136, 0, 17, 12);
		birdDown.flip(false, true);

		bird = new TextureRegion(texture, 153, 0, 17, 12);
		bird.flip(false, true);

		birdUp = new TextureRegion(texture, 170, 0, 17, 12);
		birdUp.flip(false, true);

		TextureRegion[] birds = { birdDown, bird, birdUp };
		birdAnimation = new Animation(0.06f, birds);
		birdAnimation.setPlayMode(Animation.LOOP_PINGPONG);

		skullUp = new TextureRegion(texture, 192, 0, 24, 14);
		// Create by flipping existing skullUp
		skullDown = new TextureRegion(skullUp);
		skullDown.flip(false, true);

		bar = new TextureRegion(texture, 136, 16, 22, 3);
		bar.flip(false, true);

//		dead = Gdx.audio.newSound(Gdx.files.internal("data/dead.wav"));
//		flap = Gdx.audio.newSound(Gdx.files.internal("data/flap.wav"));
//		coin = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
//		fall = Gdx.audio.newSound(Gdx.files.internal("data/fall.wav"));

		font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
		font.setScale(.25f, -.25f);

		whiteFont = new BitmapFont(Gdx.files.internal("data/whitetext.fnt"));
		whiteFont.setScale(.1f, -.1f);

		shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
		shadow.setScale(.25f, -.25f);

		// Create (or retrieve existing) preferences file
		prefs = Gdx.app.getPreferences("ZombieBird");

		if (!prefs.contains("highScore")) {
			prefs.putInteger("highScore", 0);
		}
	}
	
	public static void loadNumbers(){
		numberTexture = new Texture(Gdx.files.internal("data/whiteNumbers.png"));
		numberTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		TextureRegion[][] splits = TextureRegion.split(numberTexture, 40, 64);
		
//		TextureRegion[] numbers = {zero,one,two,three,four,five,six,seven,eight,nine};
		
//		int i = 0;
//		for(TextureRegion reg : numbers){
//			reg = splits[0][i];
//			reg.flip(false, true);
//			i++;
//		}
		whiteRegion = splits[0][0];
		whiteRegion.flip(false, true);
		
//		zero = splits[0][0];
//		zero.flip(false, true);
//		
//		one = splits[0][1];
//		one.flip(false, true);
//		
//		two = splits[0][2];
//		two.flip(false, true);
//		
//		three = splits[0][3];
//		three.flip(false, true);
//		
//		four = splits[0][4];
//		four.flip(false, true);
//		
//		five = splits[0][5];
//		five.flip(false, true);
//		
//		six = splits[0][6];
//		six.flip(false, true);
//		
//		seven = splits[0][7];
//		seven.flip(false, true);
//		
//		eight = splits[0][8];
//		eight.flip(false, true);
//		
//		nine = splits[0][9];
//		nine.flip(false, true);
	}
	
	

	private static Texture playerTexture;
	public static TextureRegion standUR, standUL, standOR, standOL;
	
	public static void loadPlayerTextures(){
		playerTexture = new Texture(Gdx.files.internal("data/PlayerTexture.png"));
//		playerTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		TextureRegion[][] splits = TextureRegion.split(playerTexture, 34, 64);
		
		standOR = splits[0][0];
		standOR.flip(false, true);
		
		standUR = splits[1][0];
		standUR.flip(false, true);
//		
//		standUR = splits[0][1];
//		standUR.flip(true, false);
//		
//		standUL = splits[0][1];
//		standUL.flip(true, false);
		
	}
	
	
	
	private static Texture blockTexture;
	public static TextureRegion grass1,grass2,grass3,def,water;
	
	public static void loadBlocks(){
		blockTexture = new Texture(Gdx.files.internal("data/blocks.png"));
		blockTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		TextureRegion[][] splits = TextureRegion.split(blockTexture, 34, 34);
		
//		int i = 0;
//		for(TextureRegion reg : numbers){
//			reg = splits[0][i];
//			reg.flip(false, true);
//			i++;
//		}
//		
		grass1 = splits[0][0];
		grass1.flip(false, true);
		
		grass2 = splits[0][1];
		grass2.flip(false, true);
		
		grass3 = splits[0][2];
		grass3.flip(false, true);
		
		def = splits[0][3];
		def.flip(false, true);
		
		water = splits[0][4];
		water.flip(false, true);
	}

	public static void setHighScore(int val) {
		prefs.putInteger("highScore", val);
		prefs.flush();
	}

	public static int getHighScore() {
		return prefs.getInteger("highScore");
	}

	public static void dispose() {
		// We must dispose of the texture when we are finished.
		texture.dispose();
		touchKnob.dispose();

		// Dispose sounds
//		dead.dispose();
//		flap.dispose();
//		coin.dispose();
//
//		font.dispose();
//		shadow.dispose();
	}

}