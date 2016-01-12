/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.appwarp.Multiplayer;


public class World {
	public interface WorldListener {
		public void jump();

		public void highJump();

		public void hit();

		public void coin();
	}

//	public static final float WORLD_WIDTH = 10;
//	public static final float WORLD_HEIGHT = 15 * 20;
//	public static final int WORLD_STATE_RUNNING = 0;
//	public static final int WORLD_STATE_NEXT_LEVEL = 1;
//	public static final int WORLD_STATE_GAME_OVER = 2;
//	public static final Vector2 gravity = new Vector2(0, -12);

	// public final Bob local_bob;
	// public final List<Platform> platforms;
	// public final List<Spring> springs;
	// public final List<Squirrel> squirrels;
	// public final List<Coin> coins;
	// public Castle castle;
//	public final WorldListener listener;
//	public final Random rand;

	// public float heightSoFar;
	// public int score;
	// public int state;

	public World(WorldListener listener) {
		// this.local_bob = new Bob(5, 1);
		// this.platforms = new ArrayList<Platform>();
		// this.springs = new ArrayList<Spring>();
		// this.squirrels = new ArrayList<Squirrel>();
		// this.coins = new ArrayList<Coin>();
//		this.listener = listener;
//		rand = new Random();
		// generateLevel();

		// this.heightSoFar = 0;
		// this.score = 0;
		// this.state = WORLD_STATE_RUNNING;
	}

	private void generateLevel () {
		try{
//			JSONArray array = new JSONArray(Assets.platformDataString);
				
				//JSON COOL
//				JSONObject data = array.getJSONObject(i);
				
				//COOLE SACHE!!!
//				Platform platform = new Platform(data.getInt("type"), (float)data.getDouble("x"), (float)data.getDouble("y"));

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void update(float deltaTime, float accelX) {
		// updateBob(deltaTime, accelX);
		// updatePlatforms(deltaTime);
		// updateSquirrels(deltaTime);
		// updateCoins(deltaTime);
		// if (local_bob.state != Bob.BOB_STATE_HIT) checkCollisions();
		// checkGameOver();
	}

	private void checkGameOver() {
		// if (heightSoFar - 7.5f > local_bob.position.y) {
		// WarpController.getInstance().updateResult(WarpController.GAME_LOOSE,
		// null);
		// }
	}

}
