/**
 * Project:			Nimpres Android Client
 * File name: 		PeerStatus.java
 * Date modified:	2011-03-16
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
package com.android.nimpres.lan;

import java.net.InetAddress;

import com.nimpes.android.settings.NimpresSettings;


public class PeerStatus {
	InetAddress peerIP = null;
	String presentationName = "";
	int slideNumber = 0;
	
	/**
	 * Default empty constructor
	 */
	public PeerStatus(){}
	
	/**
	 * Standard new PeerStatus with known values
	 * @param peerIP
	 * @param presentationName
	 * @param slideNumber
	 */
	public PeerStatus(InetAddress peerIP, String presentationName, int slideNumber){
		this.peerIP = peerIP;
		this.presentationName = presentationName;
		this.slideNumber = slideNumber;
	}
	
	/**
	 * Create new PeerStatus from a received UDPMessage
	 * @param message
	 */
	public PeerStatus(UDPMessage message){
		String dataStr = message.getDataAsString();
		int seperatorIndex = dataStr.indexOf(NimpresSettings.STATUS_SEPERATOR);
		String title = dataStr.substring(0, seperatorIndex);
		int slide = Integer.parseInt(dataStr.substring(seperatorIndex+NimpresSettings.STATUS_SEPERATOR.length()));
		
		this.peerIP = message.remoteIP;
		this.presentationName = title;
		this.slideNumber = slide;
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
	
	
}
