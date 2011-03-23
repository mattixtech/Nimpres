/**
 * Project:			Nimpres Android Client
 * File name: 		PeerStatus.java
 * Date modified:	2011-03-18
 * Description:		Status message from peer
 * 
 * License:			Copyright (c) 2010 (Matthew Brooks, Jordan Emmons, William Kong)
					
					Permission is hereby granted, free of charge, to any person obtaining a copy
					of this software and associated documentation files (the "Software"), to deal
					in the Software without restriction, including without limitation the rights
					to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
					copies of the Software, and to permit persons to whom the Software is
					furnished to do so, subject to the following conditions:
					
					The above copyright notice and this permission notice shall be included in
					all copies or substantial portions of the Software.
					
					THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
					IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
					FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
					AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
					LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
					OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
					THE SOFTWARE.
 */
package com.nimpres.android.lan;

import java.net.InetAddress;

import android.util.Log;

import com.nimpres.android.settings.NimpresSettings;


public class PeerStatus {
	private InetAddress peerIP = null;
	private String presentationName = "";
	private String presenterName = "";
	private int presentationID = 0;
	private int slideNumber = 0;
	private String source = ""; //internet or lan
	/**
	 * Default empty constructor
	 */
	public PeerStatus(){}
	
	/**
	 * Standard new PeerStatus with known values
	 * @param peerIP
	 * @param presentationName
	 * @param slideNumber
	 * @param presenterName
	 * @param presentationID
	 */
	public PeerStatus(InetAddress peerIP, String presentationName, int slideNumber, String presenterName, int presentationID){
		this.peerIP = peerIP;
		this.presentationName = presentationName;
		this.slideNumber = slideNumber;
		this.presenterName = presenterName;
		this.presentationID = presentationID;
	}
	
	/**
	 * Create new PeerStatus from a received UDPMessage
	 * @param message
	 */
	public PeerStatus(UDPMessage message){
		String dataStr = message.getDataAsString();
		int seperatorLength = NimpresSettings.STATUS_SEPERATOR.length();
		int firstSeperatorIndex = dataStr.indexOf(NimpresSettings.STATUS_SEPERATOR);
		int secondSeperatorIndex = dataStr.indexOf(NimpresSettings.STATUS_SEPERATOR,firstSeperatorIndex+seperatorLength);
		int thirdSeperatorIndex = dataStr.indexOf(NimpresSettings.STATUS_SEPERATOR,secondSeperatorIndex+seperatorLength);
		//Log.d("PeerStatus","first_index:"+firstSeperatorIndex+", second_index:"+secondSeperatorIndex+", third_index:"+thirdSeperatorIndex);
		peerIP = message.remoteIP;
		presentationName = dataStr.substring(0, firstSeperatorIndex);
		slideNumber = Integer.parseInt(dataStr.substring(firstSeperatorIndex+seperatorLength,secondSeperatorIndex));
		presenterName = dataStr.substring(secondSeperatorIndex+seperatorLength, thirdSeperatorIndex);
		presentationID = Integer.parseInt(dataStr.substring(thirdSeperatorIndex+seperatorLength).trim()); //Need to trim off all of the trailing 0 bytes 
	}
	
	public String getDataString(){
		return presentationName + NimpresSettings.STATUS_SEPERATOR +
		String.valueOf(slideNumber) + NimpresSettings.STATUS_SEPERATOR +
		presenterName + NimpresSettings.STATUS_SEPERATOR +
		String.valueOf(presentationID);
	}

	/**
	 * @return the peerIP
	 */
	public InetAddress getPeerIP() {
		return peerIP;
	}

	/**
	 * @param peerIP the peerIP to set
	 */
	public void setPeerIP(InetAddress peerIP) {
		this.peerIP = peerIP;
	}

	/**
	 * @return the presentationName
	 */
	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * @param presentationName the presentationName to set
	 */
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	/**
	 * @return the slideNumber
	 */
	public int getSlideNumber() {
		return slideNumber;
	}

	/**
	 * @param slideNumber the slideNumber to set
	 */
	public void setSlideNumber(int slideNumber) {
		this.slideNumber = slideNumber;
	}

	/**
	 * @return the presenterName
	 */
	public String getPresenterName() {
		return presenterName;
	}

	/**
	 * @param presenterName the presenterName to set
	 */
	public void setPresenterName(String presenterName) {
		this.presenterName = presenterName;
	}

	/**
	 * @return the presentationID
	 */
	public int getPresentationID() {
		return presentationID;
	}

	/**
	 * @param presentationID the presentationID to set
	 */
	public void setPresentationID(int presentationID) {
		this.presentationID = presentationID;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	
}
