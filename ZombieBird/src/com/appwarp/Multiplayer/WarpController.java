package com.appwarp.Multiplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kilobolt.Menus.MultiPlayerCreate;
import com.kilobolt.Menus.MultiPlayerLobby;
import com.kilobolt.Menus.MultiPlayerMenu;
import com.kilobolt.SimpleHelpers.Settings;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;
import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomEvent;

public class WarpController {

	private static WarpController instance;

	private boolean showLog = true;

	private final String apiKey = "a9ace888bd39927c196b4c4fed34519d2c3aa44b2145964370ccce34a890b230";
	private final String secretKey = "9188cd447392e71fcafc647000df72a7c0ecd4b9ef5b0c9ad4e1c67aa21cc57f";

	private WarpClient warpClient;

	private String localUser;

	boolean isOnline = false;
	
	boolean isUDPEnabled = false;

	private WarpListener warpListener;

	private int STATE;

	// Game state constants
	public static final int WAITING = 1;
	public static final int STARTED = 2;
	public static final int COMPLETED = 3;
	public static final int FINISHED = 4;

	// Game completed constants
	public static final int GAME_WIN = 5;
	public static final int GAME_LOOSE = 6;
	public static final int ENEMY_LEFT = 7;

	public String getStateToString() {
		switch (STATE) {
		case 1:
			return "WAITING";
		case 2:
			return "STARTED";
		case 3:
			return "COMPLETED";
		case 4:
			return "FINISHED";
		case 5:
			return "GAME WIN";
		case 6:
			return "GAME LOOSE";
		case 7:
			return "ENEMY LEFT";
		default:
			return "DEFAULT";
		}
	}

	public boolean isInLobby() {
		return RoomInformation.activRoomID!=null;
	}
	
	public boolean isOnline() {
		return isOnline;
	}

	public WarpController() {
		initAppwarp();
		warpClient.addConnectionRequestListener(new ConnectionListener(this));
		warpClient.addChatRequestListener(new ChatListener(this));
		warpClient.addZoneRequestListener(new ZoneListener(this));
		warpClient.addRoomRequestListener(new RoomListener(this));
		warpClient.addNotificationListener(new NotificationListener(this));
	}

	public static WarpClient getClient() {
		return getInstance().warpClient;
	}

	public String[] onlineUsers;

	public String[] getAllPlayers() {
		warpClient.getOnlineUsers();
		return onlineUsers;
	}
	
	public List<String> roomIDs = new ArrayList<String>();
	
	public List<String> getAllRoomID(){
		warpClient.getAllRooms();
		return roomIDs;
	}	
	
	public static WarpController getInstance() {
		if (instance == null) {
			instance = new WarpController();
		}
		return instance;
	}

	public void startApp(String localUser) {
		this.localUser = localUser;
		warpClient.connectWithUserName(localUser);
	}

	public void setListener(WarpListener listener) {
		this.warpListener = listener;
	}

	public void stopApp() {
		if (isInLobby()) {
			warpClient.unsubscribeRoom(RoomInformation.activRoomID);
			warpClient.leaveRoom(RoomInformation.activRoomID);
		}
		isOnline = false;
		warpClient.disconnect();
	}

	private void initAppwarp() {
		try {
			System.out.println("WCon: initAppwarp");
			WarpClient.initialize(apiKey, secretKey);
			WarpClient.setRecoveryAllowance(120);
			warpClient = WarpClient.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("WCon: initAppwarp Error: "
					+ e.getLocalizedMessage());
		}
	}

	public void sendChat(String string) {
		if (isInLobby()) {
			warpClient.sendChat(localUser + "#@" + string);
		}
	}

	public void sendGameUpdate(String msg) {
		if (isInLobby()) {
			if (isUDPEnabled) {
				warpClient.sendUDPUpdatePeers((localUser + "#@" + msg)
						.getBytes());
			} else {
				warpClient.sendUpdatePeers((localUser + "#@" + msg).getBytes());
			}
		}
	}

	public void updateResult(int code, String msg) {
		if (isInLobby()) {
			STATE = COMPLETED;
			HashMap<String, Object> properties = new HashMap<String, Object>();
			properties.put("result", code);
			warpClient.lockProperties(properties);
		}
	}

	public void onConnectDone(ConnectEvent e) {
		if (e.getResult()==WarpResponseResultCode.SUCCESS) {
			warpClient.initUDP();
			isOnline = true;
			// warpClient.joinRoomInRange(1, 1, false);
			MultiPlayerMenu.connected();
		} else {
			RoomInformation.activRoomID=null;
			isOnline = false;
			handleError("Connect Done : status false");
			MultiPlayerMenu.noConnection(e.getResult());
		}
	}
	
	public void joinRoomInRange(){
		if(isOnline){
			warpClient.joinRoomInRange(1, 1, false);
		}
	}

	public void onDisconnectDone(boolean status) {
		isOnline = false;
	}

	public void onRoomCreated(String roomId) {
		if (roomId != null) {
			System.out.println("WACON: onRoomCreated");
			warpClient.joinRoom(roomId);
		} else {
			handleError("Room Created : roomId = null");
		}
	}

	public void onJoinRoomDone(RoomEvent event) {
		log("WACON: onJoinRoomDone: " + event.getResult());
		if (event.getResult() == WarpResponseResultCode.SUCCESS) {// success
			
			System.out.println("WACON: onjoinRoomDone: found Room: "+event.getData().getId());
			RoomInformation.activRoomID=event.getData().getId();
			warpClient.subscribeRoom(event.getData().getId());
			MultiPlayerLobby.connected();
			
		} else if (event.getResult() == WarpResponseResultCode.RESOURCE_NOT_FOUND) {// no
																					// such
																					// room
																					// found
			HashMap<String, Object> data = new HashMap<String, Object>();
			data.put("result", "");
			
			System.out.println("WACON: onJoinRoomDone : create new room");
			MultiPlayerCreate.getInstance().createRoom();
//			warpClient.createRoom("superjumper", "shephertz", 2, data);
			
		} else {			
			warpClient.disconnect();
			handleError("JoinRoom Event Code"+event.getResult());
			

			MultiPlayerLobby.noConnection();
		}
	}

	public void onRoomSubscribed(String roomId) {
		System.out.println("WACon : onRoomSubcribed: "+roomId);
		if (roomId != null) {
			warpClient.getLiveRoomInfo(roomId);
		} else {
			warpClient.disconnect();
			handleError("WarpCon : onRoomSubscribed roomId = null");
		}
	}
	
	public String getRoomOwner(){
		LiveRoomInfoEvent event = RoomInformation.searchInHashMap(RoomInformation.activRoomID);
		if(event==null){
			return null;
		}
		return event.getData().getRoomOwner();
	}
	
	public boolean amIRoomOwner(){
		String owner = getRoomOwner();
		if(owner == null) return false;
		return owner.equals(localUser);
	}

	public void onGetLiveRoomInfo(LiveRoomInfoEvent event) {
		System.out.println("WACON: onGetLiveRoomInfo");
		if(event==null){
			RoomInformation.deleteRoomEvent(RoomInformation.activRoomID);
			warpClient.disconnect();
			handleError("WACON onLiveRoomInfoEvent event = null");
		}
		else{
			RoomInformation.updateRoomEvent(event.getData().getId(), event);
			String[] liveUsers = RoomInformation.getJoinedUsersArrayInRoom(event.getData().getId());
			if(liveUsers==null){
				warpClient.disconnect();
				handleError("LiveRoomInfoEvent liveUsers = null");
			}
			else{
				if (liveUsers.length == 4) {
					startGame();
				} else {
					System.out.println("WACON: Sttate waiting");
					waitForOtherUser();
				}
			}
		}
	}

	public void onSendChatDone(boolean status) {
		log("onSendChatDone WarpCon: " + status);
	}

	public void onGameUpdateReceived(String message) {
		String userName = message.substring(0, message.indexOf("#@"));
		String data = message.substring(message.indexOf("#@") + 2,
				message.length());
		if (!localUser.equals(userName)) {
			warpListener.onGameUpdateReceived(data);
			log("WarpController: Button: " + data);
		}
	}

	public void onResultUpdateReceived(String userName, int code) {
		if (localUser.equals(userName) == false) {
			STATE = FINISHED;
			warpListener.onGameFinished(code, true);
		} else {
			warpListener.onGameFinished(code, false);
		}
	}

	public void onUserJoinedRoom(String roomId, String userName) {
		/*
		 * if room id is same and username is different then start the game
		 */

		if (!localUser.equals(userName)) {
			
			MultiPlayerLobby.addMessage(Settings.getSystemName(), userName
					+ " joined");
//			startGame();
		}
	}
	
	public void onUserLeftRoom(String roomId, String userName) {
		
		if (!localUser.equals(userName)) {
			MultiPlayerLobby.addMessage(Settings.getSystemName(), userName
					+ " left");
		}

		if (STATE == STARTED && !localUser.equals(userName)) {// Game Started
																// and other
																// user left the
																// room
			warpListener.onGameFinished(ENEMY_LEFT, true);
		}
	}

	public int getState() {
		return this.STATE;
	}

	private void log(String message) {
		if (showLog) {
			System.out.println(message);
		}
	}

	private void startGame() {
		STATE = STARTED;
		warpListener.onGameStarted("Start the Game");
		System.out.println("Starting gmae");
	}

	private void waitForOtherUser() {
		STATE = WAITING;
		warpListener.onWaitingStarted("Waiting for other user");
		System.out.println("Waiting for others");
	}

	private void handleError(String reason) {
		if (RoomInformation.activRoomID != null && RoomInformation.activRoomID.length() > 0) {
			warpClient.deleteRoom(RoomInformation.activRoomID);
		}
		System.out.println("WCon: handleError : "+reason);
		disconnect();
	}

	
	
	public void handleLobbyLeave() {		
		if (RoomInformation.activRoomID!=null) {
			boolean deleteRoom = amIRoomOwner();						
			warpClient.unsubscribeRoom(RoomInformation.activRoomID);
			warpClient.leaveRoom(RoomInformation.activRoomID);
			
			if(deleteRoom){
				System.out.println("Room Deleted");
				warpClient.deleteRoom(RoomInformation.activRoomID);
			}
			
//			if (STATE != STARTED) {
//				warpClient.deleteRoom(roomId);
//			}
//			warpClient.disconnect();
			System.out.println("Quited SUCCESS");
		}
	}

	public void handleServerLeave() {
		warpClient.disconnect();
	}
	
	public void handleRuntimeErroer(){
		handleLobbyLeave();
		handleServerLeave();
	}

	public void disconnect() {
		warpClient.removeConnectionRequestListener(new ConnectionListener(this));
		warpClient.removeChatRequestListener(new ChatListener(this));
		warpClient.removeZoneRequestListener(new ZoneListener(this));
		warpClient.removeRoomRequestListener(new RoomListener(this));
		warpClient.removeNotificationListener(new NotificationListener(this));
		warpClient.disconnect();
		isOnline = false;
		RoomInformation.activRoomID=null;
	}

}
