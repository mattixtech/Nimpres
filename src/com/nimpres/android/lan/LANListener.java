/**
 * Project:			Nimpres Android Client
 * File name: 		LANListener.java
 * Date modified:	2011-03-18
 * Description:		Listens for updates about a presentation on the LAN
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

import java.util.ArrayList;

import com.nimpres.android.NimpresObjects;
import com.nimpres.android.settings.NimpresSettings;

import android.util.Log;

public class LANListener implements Runnable{
	
	ArrayList<PeerStatus> advertisingPeers; 	//stores the list of the LAN IP Addresses of all advertising peers
	boolean isStopped = false;
	
    /**
     * 
     */
	public LANListener(){
		advertisingPeers = NimpresObjects.peerPresentations;
	}	
	
	/**
	 * Continues checking for advertisements until told to stop
	 */
	public void run(){
		initMessage();
		while( ! isStopped()){
			try{
				Log.d("LANListener","attempting to receive peer status update: "); 
				UDPMessage inPkt = new UDPMessage(NimpresSettings.SERVER_PEER_PORT,1024);
				if(inPkt.getType().equals(NimpresSettings.MSG_PRESENTATION_STATUS)){
					PeerStatus recvStatus = new PeerStatus(inPkt);
	                Log.d("LANListener","received message from peer: "+inPkt.getRemoteIP()+", id: "+recvStatus.getPresentationID()
	                		+", presenter:"+recvStatus.getPresenterName()+", title:"+recvStatus.getPresenterName()+", current slide:"+recvStatus.getSlideNumber());	                
	                
	                if(NimpresObjects.currentPresentation != null && recvStatus.getPresentationID() == NimpresObjects.currentPresentation.getPresentationID() && ! NimpresObjects.currentPresentation.isPaused()) //This id matches the one being viewed so we will update its slide
	                	NimpresObjects.currentPresentation.setCurrentSlide(recvStatus.getSlideNumber());
	                
	                for(int i=0;i<advertisingPeers.size();i++){
	                	if(advertisingPeers.get(i).getPeerIP().equals(inPkt.getRemoteIP())){
	                		advertisingPeers.remove(i); //check list and remove peer status if already in, so we can update it
	                		Log.d("LANListener","peer existed, refreshing...");
	                	}
	                }
	                advertisingPeers.add(recvStatus); //add peer to list
	                Log.d("LANListener","Added peer to list");
	                Log.d("LANListener","Peer List Current Size: "+advertisingPeers.size());
				}else
					Log.d("LANListener","received improper message: "+inPkt.getType()); 
	        }catch(Exception e){
	        	 Log.d("LANListener"," Exception: "+e.toString());
	        	 e.printStackTrace();
	        }
		}      
	}
	
	/**
	 * 
	 */
	public static void initMessage(){
		Log.d("LANListener","init");
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isStopped(){
        return isStopped;
    }
	
	/**
	 * Stops execution of the LANListener
	 */
	public void stop(){
        isStopped = true;
    }

	/**
	 * @return the advertisingPeers
	 */
	public ArrayList<PeerStatus> getAdvertisingPeers() {
		return advertisingPeers;
	}
}
