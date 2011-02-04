/**
 * Project:			Nimpres Android Client
 * File name: 		ConnectionReceiver.java
 * Date modified:	2011-02-03
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

public class ConnectionReceiver {
	private boolean isDisabled = true;
    private BlockingQueue<Socket> socketQue;
    
    public ConnectionReceiver(){
        this.socketQue = new ArrayBlockingQueue<Socket>(NimpresSettings.SERVER_QUE_SIZE);
    }

    public synchronized boolean put(Socket s){
        return socketQue.offer(s);
    }

    public synchronized Socket get(){
        try{
            return socketQue.poll(1, TimeUnit.MILLISECONDS);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void destroy(){
        socketQue.clear();
        isDisabled = true;
    }

    public synchronized boolean isActive(){
        return !isDisabled;
    }

    public synchronized boolean isDisabled(){
        return isDisabled;
    }

    public synchronized void enable(){
        isDisabled = false;
    }
}
