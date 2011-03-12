/**
 * Project:			Nimpres Android Client
 * File name: 		ConnectionReceiver.java
 * Date modified:	2011-03-12
 * Description:		Receives incoming file transfer requests
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

import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import android.nimpres.client.settings.NimpresSettings;
import android.util.Log;

public class ConnectionReceiver {
	private boolean isDisabled = false;
    private BlockingQueue<Socket> socketQue;
    
    public ConnectionReceiver(){
        this.socketQue = new ArrayBlockingQueue<Socket>(NimpresSettings.SERVER_QUE_SIZE);
    }
    
    /**
     * Puts a socket into the queue
     * @param s
     * @return
     */
    public boolean put(Socket s){
    	//TODO I think this should probably be a synchronized method but it was causing problems so I removed the synchronized keyword
        return socketQue.offer(s);
    }

    /**
     * Gets a socket from the queue if there is one
     * @return
     */
    public synchronized Socket get(){
        try{
            return socketQue.poll(10, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Destroys this queue and all connections currently in it
     */
    public synchronized void destroy(){
        socketQue.clear();
        isDisabled = true;
    }
    
    /**
     * Checks if this receiver is active
     * @return
     */
    public boolean isActive(){
        return !isDisabled;
    }

    /**
     * Checks if the receiver is disactivated
     * @return
     */
    public synchronized boolean isDisabled(){
        return isDisabled;
    }
    
    /**
     * Enables the receiver
     */
    public synchronized void enable(){
    	Log.d("ConnectionReceiver","Enabled");
        isDisabled = false;
    }
}
