/**
 * Project:			Nimpres Android Client
 * File name: 		DPSServer.java
 * Date modified:	2011-03-12
 * Description:		Serves a DPS file on the LAN
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import android.content.Context;
import android.util.Log;

import com.nimpres.android.settings.NimpresSettings;

public class DPSServer implements Runnable{
	/**
	 * Displays the init message
	 */
	public static void initMessage(){
		Log.d("DPSServer","init");
	}
	private boolean isStopped = false;
	private String dpsFile;
	private Socket connectionFromClient;
	private ConnectionReceiver receiver;
	
	private Context ctx;
	
	/**
	 * 
	 * @param dpsFileName
	 * @param ctx
	 */
	public DPSServer(String dpsFileName, Context ctx){
		dpsFile = dpsFileName;
		this.ctx = ctx;
		receiver = new ConnectionReceiver();
		receiver.enable();
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isStopped(){
        return isStopped;
    }
	
	/**
	 * Opens the server socket for listening
	 */
	private void openServerSocket(){
        Thread socketListener = new Thread(new ServerSocketListener(receiver));
        Log.d("DPSServer","Attempting to start ServerSocketListener");
        Log.d("DPSServer","Listener Object: "+socketListener);
        socketListener.start();
    }
	
	/**
	 * 
	 */
	public void run(){
		byte[] outputFile = null;
		
		//Start the listener		
        openServerSocket();
		initMessage();
		
		try{
			Log.d("DPSServer","attempting to read file for serving");
			File fs = new File(ctx.getFilesDir()+File.separator+dpsFile);
			int size = (int) fs.length();
			outputFile = new byte[size];
			FileInputStream inFile = ctx.openFileInput(dpsFile);
			inFile.read(outputFile);
			inFile.close();
			Log.d("DPSServer","file read");
		}catch(Exception e){
			Log.d("DPSServer","Error:"+e.getMessage());
		}
		
		/*
         * This loop continues to check the queue until it gets a socket connection from it
         * that connection is removed from the queue and now this server should begin servicing that connection
         */
		Log.d("DPSServer","waiting for connection from peer");
        while(!isStopped()){            
            //The following statement should always return null until
            //a socket connection is actually received from the queue
        	//Thread.sleep(1);
        	
            connectionFromClient = receiver.get();
            
            if(connectionFromClient != null && receiver.isActive()){
            	Log.d("DPSServer","got something");
            	if(connectionFromClient.isConnected()){
            		try{
            			Log.d("DPSServer","peer initiating transfer request");
                        DataInputStream in = new DataInputStream(connectionFromClient.getInputStream());
                        DataOutputStream out = new DataOutputStream(connectionFromClient.getOutputStream());
                        
                        //Get the message from client
                        TCPMessage inMsg = new TCPMessage(in);
                        
                        //Check to make sure the received message is a request for file
                        if(inMsg.getType().equals(NimpresSettings.MSG_REQUEST_FILE_TRANSFER)){
                        	//Send the file as a TCPMessage
                        	TCPMessage outMsg = new TCPMessage(NimpresSettings.MSG_RESPONSE_FILE_TRANSFER,outputFile,out);
                        	Log.d("DPSServer","transferred dps file to peer");
                        }else{
                        	Log.d("DPSServer","received improper request from peer");
                        }

            		}catch(Exception e){
                        Log.d("DPSServer","Error:"+e.getMessage());
                    }
            	}
            }
        }
	}
	
	/**
	 * Stops the DPSServer from listening for connections
	 */
	public void stop(){
        isStopped = true;
    }

}
