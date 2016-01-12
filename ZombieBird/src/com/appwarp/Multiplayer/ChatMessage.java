package com.appwarp.Multiplayer;

//import java.text.SimpleDateFormat;
import java.util.Date;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kilobolt.SimpleHelpers.Settings;

public class ChatMessage{

	private Date date;
	private String orginalMessage;
	private String sender;
	private String displayMessage;
	private boolean systemMessage;
	
	public ChatMessage(String userName, String message){
		date = new Date();
		this.orginalMessage = message;
		this.sender = userName;
		
		systemMessage = Settings.isSystemName(sender);
		
		displayMessage = getMessageWithoutBBCodes();
	}
	
	public String getOrginalMessage(){
		return orginalMessage;
	}
	
	public String getDisplaySender(){
		if(systemMessage){
			return "&r"+sender+"&b: ";
		}
		return "&c"+sender+"&b: ";
	}
	
	public Date getDate(){
		return date;
	}
	
	public String getDisplayMessage(){
		return displayMessage;
	}
	
	public String getTotalMessage(){
		return getDisplaySender()+getOrginalMessage();
	}
	
	private String getMessageWithoutBBCodesAndMayColorIt(String Omessage, BitmapFontCache cache){
		String messageWBBC = "";
		
		int pos = 0;
		Color c = null;
		
		String[] messageSplits = Omessage.split("&");

		boolean first = true;
		
		for(String mes : messageSplits){
			if(mes.length()>0){
				if(ColorChars.isColorChar(mes.charAt(0))){
					if(cache!=null){	//only if we should color it get Color
						c = ColorChars.getColorFromChar(mes.charAt(0));
					}
					
					mes = mes.substring(1);
				}
				else{
					if(!first){
					messageWBBC+="&";
					first = false;
					}
				}
				messageWBBC+=mes;
				
				if(cache!=null){	//onyl if we should color
					if(c!=null){	//only if we calculated a color
						cache.setColor(c, pos, messageWBBC.length());
					}
				}
				pos = messageWBBC.length();	//setting begin for next color
			}
		}
		return messageWBBC;
	}
	
	public String getTime(){
//		return new SimpleDateFormat("HH:mm").format(date);
		return "";
	}
	
	public void drawMessage(BitmapFont font, SpriteBatch batch, float xpos, float ypos){
		BitmapFontCache cache = font.getCache();
		//set text before set color
		String message = getDisplayMessage();
		cache.setText(message, xpos, ypos);
		//set color after set text
		getMessageWithoutBBCodesAndMayColorIt(getTotalMessage(), cache);
		cache.draw(batch, 0,message.length());
	}
	
	public String getMessageWithoutBBCodes(){
		return  getMessageWithoutBBCodesAndMayColorIt(getTotalMessage(), null);
	}
	
}
