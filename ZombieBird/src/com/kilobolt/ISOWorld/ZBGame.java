package com.kilobolt.ISOWorld;

import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Game;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.Screens.SplashScreen;
import com.kilobolt.SimpleHelpers.Settings;

public class ZBGame extends Game {

	@Override
	public void create() {
		AssetLoader.load();
		Settings.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
		Settings.save();
		WarpController.getInstance().handleLobbyLeave();
		WarpController.getInstance().handleServerLeave();
	}

}