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

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Settings {

	public static final String userNameKey = "UserName";
	public static String userName;
	public static final String roomNameKey = "RoomName";
	public static String roomName;
	public static String soundEnabled;
	public static final String soundEnabledKey = "SoundEnabled";

	static Preferences prefs = Gdx.app.getPreferences("ISOWorld-settings");

	public static void load() {
		FriendList.load();
		loadSettings();
		// loadUserName(prefs.getString(roomNameKey)); //Dont load roomName
	}
	
	private static void loadSettings(){
		setUserName(prefs.getString(userNameKey));
		setSoundEnabled(prefs.getString(soundEnabledKey));
		
	}

	public static void save() {
		saveSettings();
		FriendList.save();
	}
	
	private static void saveSettings(){
		prefs.putString(userNameKey, userName);
		prefs.putString(soundEnabledKey, soundEnabled);

		prefs.flush();
	}

	private static String getRandomHexString(int numchars) {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < numchars) {
			sb.append(Integer.toHexString(r.nextInt()));
		}
		return sb.toString().substring(0, numchars);
	}

	public static void setSetting(String state, String theValue) {
		switch (state) {
		case userNameKey:
			setUserName(theValue);
			break;
		case roomNameKey:
			setRoomName(theValue);
			break;
		case soundEnabledKey:
			setSoundEnabled(theValue);
			break;
		case FriendList.friend_key_add:
			FriendList.addFriend(theValue);
			break;
		case FriendList.friend_key_remove:
			FriendList.removeFriend(theValue);
			break;
		}
	}

	private static void setRoomName(String name) {
		for (String invailid : invaildRoomNames) {
			if (name.contains(invailid))
				return;
		}
		roomName = name;
	}

	private static String systemName = "System";
	private static String[] invaildUserNames = { systemName, "Admin" };
	private static String[] invaildRoomNames = { systemName, "Admin" };

	public static boolean isSystemName(String name) {
		return name.equals(getSystemName());
	}

	public static String getSystemName() {
		return systemName;
	}

	private static void setUserName(String name) {
		if (name == null) {
			System.out.println("Loading random Name");
			userName = "Guest" + getRandomHexString(5);
		} else {
			for (String invailid : invaildUserNames) {
				if (name.contains(invailid))
					return;
			}
			userName = name;
		}
		prefs.putString(userNameKey, userName);
		prefs.flush();
	}

	private static void setSoundEnabled(String value) {
		if (value == null) {
			soundEnabled = "true";
			return;
		}
		if (value.equalsIgnoreCase("true")) {
			soundEnabled = "true";
		} else {
			soundEnabled = "false";
		}
		prefs.putString(soundEnabledKey, soundEnabled);
		prefs.flush();
	}
	
	public static boolean getSoundEnabled(){
		if(soundEnabled.equalsIgnoreCase("true")) return true;
		return false;
	}

}
