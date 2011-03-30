/**
 * Project:			Nimpres Android Client
 * File name: 		PeerStatus.java
 * Date modified:	2011-03-23
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
package com.nimpres.android.presentation;

import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.nimpres.android.lan.UDPMessage;
import com.nimpres.android.settings.NimpresSettings;
import com.nimpres.android.web.APIContact;


public class PeerStatus {
	
	/**
	 * This method use the APIContact.listPresentations method to retrieve an XML list of all presentations
	 * for a given user. It then parses this XML and creates a list of PeerStatus objects corresponding to the 
	 * presentations of that user.
	 * @param userID
	 * @param userPass
	 * @param userSearch
	 * @return
	 */
	public static ArrayList<PeerStatus> getInternetPresentations(String userID, String userPass, String userSearch){
		ArrayList<PeerStatus> internetPresentations = new ArrayList<PeerStatus>();
		String xmlResponse = APIContact.listPresentations(userID, userPass, userSearch);
		
		if( ! xmlResponse.equals(NimpresSettings.API_RESPONSE_NEGATIVE)){
			String user = userSearch;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder db = dbf.newDocumentBuilder();
				ByteArrayInputStream is = new ByteArrayInputStream(xmlResponse.getBytes("UTF-8")); //Convert the xml string to an InputStream
				Document doc = db.parse(is);	//Parse the new InputStream into a Document
				doc.getDocumentElement().normalize();
				Element presentationsRootElement = (Element) doc.getElementsByTagName("presentations").item(0);	//Root element for all 'presentations'
				
				NodeList presentationElements = presentationsRootElement.getElementsByTagName("presentation");
				
				//Loop through all of the presentation elements
				for(int i=0;i<presentationElements.getLength();i++){
					Log.d("PeerStatus","parsing presentation #"+(i+1)+" from xml api response");
					Element thisPresentation = (Element) presentationElements.item(i);
					String thisPresentationTitle = thisPresentation.getElementsByTagName("title").item(0).getTextContent();
					int thisPresentationID = Integer.parseInt(thisPresentation.getElementsByTagName("id").item(0).getTextContent());
					PeerStatus thisPeerStatus = new PeerStatus(thisPresentationTitle,user,thisPresentationID);
					Log.d("PeerStatus","adding internet peer status object: "+thisPeerStatus);
					internetPresentations.add(thisPeerStatus);
				}				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else
			return null;	//None found		
		return internetPresentations;
	}
	
	
	private InetAddress peerIP = null;
	private String presentationName = "";
	private String presenterName = "";
	private int presentationID = 0;
	private int slideNumber = 0;
	private String source = ""; //internet or lan
	private long advertisementTimestamp = 0;
	
	/**
	 * Default empty constructor
	 */
	public PeerStatus(){}
	
	/**
	 * Standard new PeerStatus with known values for LAN source
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
	 * Standard constructor for known values from Internet Source
	 * @param presentationName
	 * @param presenterName
	 * @param presentationID
	 */
	public PeerStatus(String presentationName, String presenterName, int presentationID){
		this.presentationName = presentationName;
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
		peerIP = message.getRemoteIP();
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
	 * @return the presentationID
	 */
	public int getPresentationID() {
		return presentationID;
	}

	/**
	 * @return the presentationName
	 */
	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * @return the presenterName
	 */
	public String getPresenterName() {
		return presenterName;
	}

	/**
	 * @return the slideNumber
	 */
	public int getSlideNumber() {
		return slideNumber;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param peerIP the peerIP to set
	 */
	public void setPeerIP(InetAddress peerIP) {
		this.peerIP = peerIP;
	}

	/**
	 * @param presentationID the presentationID to set
	 */
	public void setPresentationID(int presentationID) {
		this.presentationID = presentationID;
	}

	/**
	 * @param presentationName the presentationName to set
	 */
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	/**
	 * @param presenterName the presenterName to set
	 */
	public void setPresenterName(String presenterName) {
		this.presenterName = presenterName;
	}

	/**
	 * @param slideNumber the slideNumber to set
	 */
	public void setSlideNumber(int slideNumber) {
		this.slideNumber = slideNumber;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString(){
		return this.presentationName+", ID: "+this.presentationID+", Source: "+this.source;
	}

	/**
	 * @return the advertisementTimestamp
	 */
	public long getAdvertisementTimestamp() {
		return advertisementTimestamp;
	}

	/**
	 * @param advertisementTimestamp the advertisementTimestamp to set
	 */
	public void setAdvertisementTimestamp(long advertisementTimestamp) {
		this.advertisementTimestamp = advertisementTimestamp;
	}
	
}
