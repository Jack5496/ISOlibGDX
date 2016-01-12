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

package com.kilobolt.SimpleHelpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.appwarp.Multiplayer.WarpController;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.kilobolt.ISOHelpers.AssetLoader;

public class FriendList {

	public static SimpleButton friendsButton = new SimpleButton(0, 0,
			AssetLoader.playButtonUp.getRegionWidth()/2,
			AssetLoader.playButtonUp.getRegionHeight()/2,
			AssetLoader.playButtonUp, AssetLoader.playButtonDown, "0");;

	public static final String friend_title_add = "Add Friend";
	public static final String friend_title_remove = "Remove Friend";

	public static final String friend_key_add = "friend_add";
	public static final String friend_key_remove = "friend_remove";

	static Preferences friends = Gdx.app.getPreferences("ISOWorld-friends");

	static List<String> friend_names;

	private static int refreshRate = 1;
	private static float lastUpdate = 0;

	public static void load() {
		friend_names = new ArrayList<String>();

		Map<String, ?> friend_keys = friends.get();

		for (String name : friend_keys.keySet()) {
			friend_names.add(name);
		}

		System.out.println("Loaded: " + friend_names.size() + " Friends");
	}

	static void addFriend(String name) {
		if (!friend_names.contains(name)) {
			friend_names.add(name);
		}
	}

	public static void addFriend() {
		SimpleSettingsInput.getInput(friend_key_add, friend_title_add, "");
	}

	public static void removeFriend() {
		SimpleSettingsInput
				.getInput(friend_key_remove, friend_title_remove, "");
	}

	static void removeFriend(String name) {
		if (friend_names.contains(name)) {
			friend_names.remove(name);
		}
	}

	public static List<String> getFriends() {
		return friend_names;
	}
	
	public static void updateValues(float runTime){
		if (lastUpdate + refreshRate >= runTime) {
			return;
		}
		lastUpdate = runTime;

		friendsButton.setText(""+getAmountFriendsOnline());
	}

	public static List<String> getFriendsOnline() {
		String[] onlineArray = WarpController.getInstance().getAllPlayers();

		List<String> friendsOnline = new ArrayList<String>();
		if (onlineArray != null) {
			List<String> online = Arrays.asList(onlineArray);
			for (String user : friend_names) {
				if (online.contains(user))
					friendsOnline.add(user);
			}
		}
		return friendsOnline;
	}

	public static int getAmountFriendsOnline() {
		return getFriendsOnline().size();
	}

	public static void save() {
		friends.clear();

		for (String name : friend_names) {
			friends.putString(name, name);
		}

		friends.flush();
	}

}
