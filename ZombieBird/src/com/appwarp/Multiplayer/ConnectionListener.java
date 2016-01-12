package com.appwarp.Multiplayer;

import com.shephertz.app42.gaming.multiplayer.client.command.WarpResponseResultCode;
import com.shephertz.app42.gaming.multiplayer.client.events.ConnectEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.ConnectionRequestListener;

public class ConnectionListener implements ConnectionRequestListener {

	WarpController callBack;
	
	public ConnectionListener(WarpController callBack){
		this.callBack = callBack;
	}
	
	public void onConnectDone(ConnectEvent e) {
		callBack.onConnectDone(e);
	}

	public void onDisconnectDone(ConnectEvent e) {
		WarpController.getInstance().isOnline = false;
		System.out.println("Con Lis: onDisDone: "+e.getResult());
	}

	@Override
	public void onInitUDPDone (byte result) {
		if(result==WarpResponseResultCode.SUCCESS){
			callBack.isUDPEnabled = true;
		}
		
	}

}
