package com.appwarp.Multiplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.shephertz.app42.gaming.multiplayer.client.events.LiveRoomInfoEvent;

public class RoomInformation {

	// public static boolean poll = false;

	public static HashMap<String, LiveRoomInfoEvent> rooms = new HashMap<String, LiveRoomInfoEvent>();
	public static List<String> toPoll = new ArrayList<String>();

	public static String activRoomID;

	public static void workTheToPollList() {
		if (!toPoll.isEmpty()) {
			pollRoomEvent(toPoll.get(0));
		}
	}

	public static void resetList() {
		rooms = new HashMap<String, LiveRoomInfoEvent>();
	}

	public static void addPollEntry(String id) {
		if (id == null){
			return;
		}
		if (toPoll.contains(id)) {
			toPoll.remove(id);
		}
		toPoll.add(0, id);
	}

	public static void addPollEntrys(List<String> ids) {
		for (String id : ids) {
			addPollEntry(id);
		}
	}

	private static void pollRoomEvent(String rid) {
		if (WarpController.getInstance().isOnline()) {
			if (rid != null) {
				WarpController.getClient().getLiveRoomInfo(rid);
				
			}
		}
	}

	public static void pushRoomEvent(String id, LiveRoomInfoEvent event) {
		updateRoomEvent(id, event);
		toPoll.remove(id);
		id = null;
		// poll = false;
		workTheToPollList();
	}

	public static void updateRoomEvent(String id, LiveRoomInfoEvent event) {
		if (rooms.containsKey(id)) {
			rooms.remove(id);
		}
		rooms.put(id, event);
	}

	public static List<LiveRoomInfoEvent> getAllRoomEvents(List<String> ids) {
		List<LiveRoomInfoEvent> back = new ArrayList<LiveRoomInfoEvent>();

		for (Entry<String, LiveRoomInfoEvent> entry : rooms.entrySet()) {
			if (ids.contains(entry.getKey())) {
				back.add(entry.getValue());
			}
		}

		return back;
	}

	public static LiveRoomInfoEvent searchInHashMap(String id) {
		if (rooms.containsKey(id)) {
			return rooms.get(id);
		}
		return null;
	}

	public static void updateActivRoom() {
		if (activRoomID != null) {
			WarpController.getClient().getLiveRoomInfo(activRoomID);
		}
	}

	public static LiveRoomInfoEvent getActivRoomEvent() {
		return searchInHashMap(activRoomID);
	}

	public static void deleteRoomEvent(String id) {
		if (id != null) {
			rooms.remove(id);
			if (id.equals(activRoomID)) {
				activRoomID = null;
			}
		}
	}

	public static String getRoomNameInHashMap(String id) {
		LiveRoomInfoEvent event = searchInHashMap(id);
		if (event == null) {
			return id;
		}
		return event.getData().getName();
	}

	public static int getUsersAmountInRoom(String id) {
		LiveRoomInfoEvent event = searchInHashMap(id);
		if (event == null) {
			return 0;
		}
		String[] users = event.getJoinedUsers();
		if (users == null) {
			return 0;
		} else {
			return users.length;
		}

	}

	public static int getMaxUsersInHashMap(String id) {
		LiveRoomInfoEvent event = searchInHashMap(id);
		if (event == null) {
			return 0;
		}
		return event.getData().getMaxUsers();
	}

	public static String getOwnerOfRoom(String id) {
		LiveRoomInfoEvent event = searchInHashMap(id);
		if (event == null) {
			return id;
		}
		return event.getData().getRoomOwner();
	}

	public static String[] getJoinedUsersArrayInRoom(String id) {
		LiveRoomInfoEvent event = searchInHashMap(id);
		if (event == null) {
			return new String[0];
		}
		if(event.getJoinedUsers()==null){
			return new String[0];
		}
		return event.getJoinedUsers();
	}

	public static HashMap<String, String> getPropertysOfRoom(String id) {
		LiveRoomInfoEvent event = searchInHashMap(id);
		return event.getLockProperties();
	}

	public static List<String> getJoinedUsersListInRoom(String id) {
		String[] users = getJoinedUsersArrayInRoom(id);
		List<String> usersList = new ArrayList<String>();
		for (String user : users) {
			usersList.add(user);
		}
		return usersList;
	}

}
