package com.appwarp.Multiplayer;

import java.util.HashMap;

import com.kilobolt.Menus.MultiPlayerLobby;
import com.kilobolt.SimpleHelpers.Settings;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;

public class NotificationListener implements NotifyListener{

	
	private WarpController callBack;
	
	public NotificationListener(WarpController callBack) {
		this.callBack = callBack;
	}
	
	

	public void onRoomCreated(RoomData arg0) {
		
	}

	public void onRoomDestroyed(RoomData arg0) {
		
	}

	public void onUpdatePeersReceived(UpdateEvent event) {
		System.out.println("NOLI: update peers");
		callBack.onGameUpdateReceived(new String(event.getUpdate()));
	}

	public void onUserJoinedLobby(LobbyData arg0, String arg1) {
		System.out.println("NOLI: user left lobby");
	}

	public void onUserJoinedRoom(RoomData data, String username) {
		callBack.onUserJoinedRoom(data.getId(), username);
	}

	public void onUserLeftLobby(LobbyData arg0, String arg1) {
		System.out.println("NOLI: user left lobby");
	}

	public void onUserLeftRoom(RoomData roomData, String userName) {
		callBack.onUserLeftRoom(roomData.getId(), userName);
	}

	@Override
	public void onGameStarted (String arg0, String arg1, String arg2) {
		
	}
	
	@Override
	public void onGameStopped (String arg0, String arg1) {
		
	}

	@Override
	public void onMoveCompleted (MoveEvent me) {
		
	}

	@Override
	public void onPrivateChatReceived (String arg0, String arg1) {
		System.out.println("NOLI: private chat");
	}

	@Override
	public void onUserChangeRoomProperty (RoomData roomData, String userName, HashMap<String, Object> properties, HashMap<String, String> lockProperties) {
		int code = Integer.parseInt(properties.get("result").toString());
		callBack.onResultUpdateReceived(userName, code);
	}

	@Override
	public void onUserPaused (String arg0, boolean arg1, String arg2) {
		
	}

	@Override
	public void onUserResumed (String arg0, boolean arg1, String arg2) {
		
	}

	@Override
	public void onPrivateUpdateReceived(String arg0, byte[] arg1, boolean arg2) {
		// TODO Auto-generated method stub
		System.out.println("NOLI: private update");
		
	}

	@Override
	public void onChatReceived(final ChatEvent event) {
		String completeMessage = event.getMessage();
		
		String userName = completeMessage.substring(0, completeMessage.indexOf("#@"));
		String message = completeMessage.substring(completeMessage.indexOf("#@")+2, completeMessage.length());
		MultiPlayerLobby.addMessage(userName, message);
	}

	
}
