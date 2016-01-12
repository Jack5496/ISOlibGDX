package com.kilobolt.ISOWorld;

import java.lang.Thread.UncaughtExceptionHandler;

import org.lwjgl.Sys;

import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kilobolt.ISOWorld.ZBGame;

public class Start {

	// static int width = 408;
	// static int height = 272;

	// Ratio: 4:3
	// static int width = 1024;
	// static int height = 768;

	// Galaxy Nexus S
	// static int width = 800;
	// static int height = 480;

	// HTC ONE
	// static int width = 1280;
	// static int height = 720;

	// iPhone Ratio: 16:9
	static int width = 1334 / 2;
	static int height = 750 / 2;

	//
	// xxhdpi Ratio: 16:9
	// static int width = 1920;
	// static int height = 1080;

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Iso World";
		cfg.useGL20 = true;
		cfg.width = width;
		cfg.height = height;
		cfg.addIcon("data/desktopIcon.png", FileType.Internal);
		cfg.backgroundFPS = 0;
		
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, final Throwable ex) {
				WarpController.getInstance().handleRuntimeErroer();
				
				String error = "An Error has occurred: \n\n\t"
						+ ex.getLocalizedMessage()
						+ "\n\t"
						+ ex.getCause();
				Sys.alert("Critical Failure!", error); // Works on desktop from
														// LWJGL, won't work for
														// other platforms.
				ex.printStackTrace();

			}
		});

		new LwjglApplication(new ZBGame(), cfg);
		
	}
}
