/**
 * Project:			Nimpres Android Client
 * File name: 		ServerSocketListener.java
 * Date modified:	2011-03-12
 * Description:		Listens on a socket and adds requests to a receiver
 * 
 * License:			Copyright (c) 2011 (Matthew Brooks, Jordan Emmons, William Kong)
					
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

import java.net.InetAddress;
import java.net.ServerSocket;

import android.nimpres.client.settings.NimpresSettings;
import android.nimpres.client.utilities.Utilities;
import android.util.Log;

public class ServerSocketListener implements Runnable{
    private ConnectionReceiver receiver;
    private boolean isStopped = false;
    private ServerSocket serverSocket;

    
    public ServerSocketListener(ConnectionReceiver receiver){
    	this.receiver = receiver;
    	this.receiver.enable();
    }

    public void run(){
        //Attempt to listen on the server port
        try{            
        	Log.d("ServerSocketListener","trying to start up");
            serverSocket = new ServerSocket(NimpresSettings.SERVER_FILE_PORT,NimpresSettings.SERVER_QUE_SIZE,InetAddress.getByName(Utilities.getLocalIpAddress()));
            Log.d("ServerSocketListener","server socket created: "+serverSocket);
            //this.receiver.enable();
            initMessage();
        } catch(Exception e){
        	Log.d("ServerSocketListener","Cannot open port:"+NimpresSettings.SERVER_FILE_PORT+" "+e.getMessage());
            this.stop();
        }        

        /*
         * This loop is quite simple, it just keeps checking the open socket
         * if a connection is found it adds it to the queue and then goes
         * back to listening again
         */
        while(!isStopped()){
            try{
            	Log.d("ServerSocketListener","trying to receive");
                if(receiver.isActive()){
                	Log.d("ServerSocketListener","receiver was active");
                    if(receiver.put(serverSocket.accept())){	//This is where the magic happens, listen on the serverSocket and hand it off to the receiver
                    	Log.d("ServerSocketListener","added request to receiver");
                    }else
                    	Log.d("ServerSocketListener","que is full, dropping connection...");
                }else
                	Log.d("ServerSocketListener","Receiver inactive");
            }catch(Exception e){
            	Log.d("ServerSocketListener","ERROR - Cannot accept connection on port:"+NimpresSettings.SERVER_FILE_PORT+" "+e.getMessage());
            }
        }
        //If socket listener is stopped then exit program
        Log.d("ServerSocketListener","Exiting");
        System.exit(1);
    }

    
    /**
     * 
     */
    private void initMessage(){
    	Log.d("ServerSocketListener","now listening for transfer requests on port:"+NimpresSettings.SERVER_FILE_PORT);
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
}
