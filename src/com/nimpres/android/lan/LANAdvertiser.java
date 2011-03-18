/**
 * Project:			Nimpres Android Client
 * File name: 		LANAdvertiser.java
 * Date modified:	2011-03-13
 * Description:		Advertises available presentations on the local LAN
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

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.nimpes.android.settings.NimpresSettings;
import com.nimpres.android.presentation.Presentation;

import android.os.Handler;
import android.util.Log;

public class LANAdvertiser implements Runnable{

	private Presentation pres;
	private String broadcastAddress = null;
	private boolean isStopped = false;
	private DatagramSocket outputSocket;
    private DatagramPacket pkt;
    private Handler mHandler = new Handler();
    private byte[] outputBuff = null;
	
    /**
     * 
     * @param pres
     */
    public LANAdvertiser(Presentation pres, String broadcastAddress){
    	this.pres = pres;
    	this.broadcastAddress = broadcastAddress;
    }
    
    /**
	 * This task is responsible for advertising the name of the hosted presentation to LAN peers
	 */
	private Runnable lanAdvertiseTask = new Runnable() {
		public void run() {			
			try{
				if( ! isStopped()){
					UDPMessage outPkt = new UDPMessage(NimpresSettings.MSG_PRESENTATION_STATUS, outputBuff, broadcastAddress, NimpresSettings.SERVER_PEER_PORT,true);
                    Log.d("LANAdvertiser"," sent presentation status message to: "+broadcastAddress);               	                    
	            }
	        }catch(Exception e){
	        	 Log.d("LANAdvertiser"," Exception: "+e.toString());
	        }	        
			mHandler.postDelayed(this, NimpresSettings.LAN_ADVERTISE_DELAY); //Add this task to the queue again, calls itself over and over...
		}
	};
    
	/**
	 * 
	 */
	public void run(){
		initMessage();
		outputBuff = (pres.getTitle()+NimpresSettings.STATUS_SEPERATOR+pres.getCurrentSlide()).getBytes();
		mHandler.removeCallbacks(lanAdvertiseTask);
		mHandler.postDelayed(lanAdvertiseTask, 100);        
	}
	
	/**
	 * 
	 */
	public static void initMessage(){
		Log.d("LANAdvertiser","init");
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isStopped(){
        return isStopped;
    }
	
	/**
	 * 
	 */
	public void stop(){
        isStopped = true;
    }

	/**
	 * @return the pres
	 */
	public Presentation getPres() {
		return pres;
	}

	/**
	 * @param pres the pres to set
	 */
	public void setPres(Presentation pres) {
		this.pres = pres;
	}
	
}
