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


public class StartMultiplayerListener implements WarpListener {

//	private final String[] tryingToConnect = { "Connecting", "to AppWarp" };
//	private final String[] waitForOtherUser = { "Waiting for", "other user" };
//	private final String[] errorInConnection = { "Error in", "Connection",
//			"Go Back" };
//
//	private final String[] game_win = { "Congrats You Win!", "Enemy Defeated" };
//	private final String[] game_loose = { "Oops You Loose!", "Target Achieved",
//			"By Enemy" };
//	private final String[] enemy_left = { "Congrats You Win!",
//			"Enemy Left the Game" };

//	private String[] msg = tryingToConnect;

	private MultiPlayerState state;
//	private boolean newMessage;

	private enum MultiPlayerState {
		CONNECTING, WAITING, LEFT, STARTET, GAME_LOSE, GAME_WIN, FINISHED, COMPLETED;
	}

	public StartMultiplayerListener() {
		this.state = MultiPlayerState.CONNECTING;
//		this.newMessage = false;
		
		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
		
		WarpController.getInstance().setListener(this);
	}
	
	//14.5	15:48
//	public StartMultiplayerListener(Game game) {
//		this.state = MultiPlayerState.CONNECTING;
//		this.newMessage = false;
//		
//		this.game = game;
//		
//		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
//		
//		WarpController.getInstance().setListener(this);
//	}

	public void update() {
		// if (Gdx.input.justTouched()) {
		//
		// if (backBounds.contains(touchPoint.x, touchPoint.y)) {

//		if (false)
//			leave();
		// return;
		// }
		// }
	}

	public void leave() {
		this.state = MultiPlayerState.LEFT;

		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
		WarpController.getInstance().handleLobbyLeave();

		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
	}

	@Override
	public void onError(String message) {
//		this.msg = errorInConnection;

		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
		update();
	}

	@Override
	public void onGameStarted(String message) {
		this.state = MultiPlayerState.STARTET;

		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());

		System.out.println("StartMultiPlayerListener: Falls ein Fehler auftaucht, hier ist der Grund");
		
		
		//EINFACH WIEDER EINGLIEDERN
//		Gdx.app.postRunnable(new Runnable() {
//			@Override
//			public void run() {
//				new MultiplayerGameScreen(game, StartMultiplayerListener.this);
//			}
//		});

	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {

		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
		
		if (code == WarpController.GAME_WIN) {
//			this.msg = game_loose;
		} else if (code == WarpController.GAME_LOOSE) {
//			this.msg = game_win;
		} else if (code == WarpController.ENEMY_LEFT) {
//			this.msg = enemy_left;
		}
		update();
		// game.setScreen(this);
	}

	@Override
	public void onGameUpdateReceived(String message) {
		System.out.println("Recieved: " + message);
	}

	@Override
	public void onWaitingStarted(String message) {
		if (this.state != MultiPlayerState.WAITING) {
			this.state = MultiPlayerState.WAITING;
		}

//		System.out.println("SML: WarpController State: "+WarpController.getInstance().getStateToString());
		
//		this.msg = waitForOtherUser;
		update();
	}

}
