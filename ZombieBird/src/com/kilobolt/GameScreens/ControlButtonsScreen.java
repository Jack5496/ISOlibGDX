package com.kilobolt.GameScreens;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.GameWorld.GameRenderer;
import com.kilobolt.ISOHelpers.AssetLoader;
import com.kilobolt.ISOHelpers.TouchInfo;

public class ControlButtonsScreen {

	private GameRenderer renderer;

	public ControlButtonsScreen(GameRenderer renderer) {
		this.renderer = renderer;
		initAssets();
	}

	public void drawGameScreen(SpriteBatch batch) {

		

	}
	
	
	private void initAssets(){
		this.touches = renderer.getInputHandler().getTouches();
		this.knob = AssetLoader.touchKnob;
	}

	private Map<Integer, TouchInfo> touches;
	private Texture knob;
	
	int r = 10; // normal touches
	int br = 20;// touch knob base
	int md = br - r;// touch knob base
	
	public void drawTouches(SpriteBatch batch) {
		
		float dist = 0;
		float xr = 0;
		float yr = 0;

		for (int i = 0; i < 5; i++) {

			float xs = touches.get(i).startPos.x;
			float ys = touches.get(i).startPos.y;

			float xa = touches.get(i).lastPos.x;
			float ya = touches.get(i).lastPos.y;

			if (touches.get(i).touched) {
				if (touches.get(i).movePad) {

					batch.draw(knob, xs - br / 2, ys - br / 2, br, br);

					dist = touches.get(i).getDistance();
					if (dist > md) {
						dist = md;
					}
					float power = dist / md;
					double angle = touches.get(i).getVelocityAngleRadian();
					xr = (float) Math.cos(angle) * power;
					yr = (float) Math.sin(angle) * power;

					batch.draw(knob, xr * md + xs - r / 2,
							yr * md + ys - r / 2, r, r);
				} else {
					batch.draw(knob, xa - r / 2, ya - r / 2, r, r);
				}
			}
		}
	}
}
