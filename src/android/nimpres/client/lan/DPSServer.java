/**
 * Project:			Nimpres Android Client
 * File name: 		DPSServer.java
 * Date modified:	2011-02-03
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
package android.nimpres.client.lan;




import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

import android.content.Context;
import android.nimpres.client.settings.NimpresSettings;
import android.util.Log;

public class DPSServer implements Runnable{
	private boolean isStopped = false;
	private String dpsFile;
	private Socket connectionFromClient;
	private ConnectionReceiver receiver;
	private Context ctx;
	
	public DPSServer(String dpsFileName, Context ctx){
		dpsFile = dpsFileName;
		this.ctx = ctx;
		receiver = new ConnectionReceiver();
	}
	
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
            	if(connectionFromClient.isConnected()){
            		try{
            			Log.d("DPSServer","peer initiating transfer request");
                        DataInputStream in = new DataInputStream(connectionFromClient.getInputStream());
                        DataOutputStream out = new DataOutputStream(connectionFromClient.getOutputStream());
                        byte[] recPkt = Message.getMessage(in);
                        if(Message.hasType(recPkt, NimpresSettings.MSG_REQUEST_FILE_TRANSFER)){
                        	Message.sendMessage(out,NimpresSettings.MSG_RESPONSE_FILE_TRANSFER,outputFile);
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
	
	private void openServerSocket(){
        Thread socketListener = new Thread(new ServerSocketListener(receiver));
        socketListener.start();        
    }
	
	public static void initMessage(){
		Log.d("DPSServer","init");
	}
	
	public boolean isStopped(){
        return isStopped;
    }
	
	public void stop(){
        isStopped = true;
    }

}
