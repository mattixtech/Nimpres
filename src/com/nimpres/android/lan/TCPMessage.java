/**
 * Project:			Nimpres Android Client
 * File name: 		TCPMessage.java
 * Date modified:	2011-03-16
 * Description:		TCP Message sending/receiving
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

import android.util.Log;

public class TCPMessage {
	private byte[] data = {0};
	private String type = "";
	private int length = 0;
	
	public TCPMessage(){
	}
	
	/**
	 * Standard constructor to create a message
	 * @param type
	 * @param data
	 */
	public TCPMessage(String type, byte[] data){
		this.type = type;
		this.data = new byte[data.length];
		this.data = data;
		this.length = this.type.length() + this.data.length + "#$".length();
	}
	
	/**
	 * Constructor that reads in and creates a message
	 * @param in
	 */
	public TCPMessage(DataInputStream in){
		getMessage(in);
	}
	
	/**
	 * Constructor that creates a message and immediately sends it
	 * @param type
	 * @param data
	 * @param out
	 */
	public TCPMessage(String type, byte[] data, DataOutputStream out){
		this.type = type;
		this.data = new byte[data.length];
		this.data = data;
		this.length = this.type.length() + this.data.length + "#$".length();
		sendMessage(out);
	}
	
	/**
	 * Manually send the message
	 * @param out
	 */
	public void sendMessage(DataOutputStream out){
		if(!type.equals("") && length > 0){
	        try{            
	            String messageHead = "#"+type+"$";
	            out.writeInt(length); //Send over the length first            
	            out.write(messageHead.getBytes()); //Send the type
	            out.write(data); //Send the data
	            out.flush();
	            Log.d("TCPMessage","Sent message: "+messageHead);
	        }catch(Exception e){
	        	Log.d("TCPMessage","error: "+e.getMessage());
	            e.printStackTrace();
	        }
		}else
			Log.d("TCPMessage","attempted to send empty message");
    }
	
	/**
	 * Manually read a message in
	 * @param in
	 */
	public void getMessage(DataInputStream in){
		try{
            int len = in.readInt(); //Read the expected length of the messsage
            byte[] buff = new byte[len]; //Create a buffer to store the message
            in.readFully(buff);
            type = parseType(buff);
            data = parseData(type,buff,len);
            length = len;
            Log.d("TCPMessage","Received message: "+type);
        }catch(Exception e){
        	Log.d("TCPMessage","error: "+e.getMessage());
            e.printStackTrace();            
        }
    }

	/**
	 * 
	 * @param msg
	 * @return
	 */
    private String parseType(byte[] msg){
        String strMsg = new String(msg);
        return strMsg.substring(strMsg.indexOf("#")+1,strMsg.indexOf("$"));
    }

    /**
     * 
     * @param message
     * @return
     */
    private byte[] parseData(String type,byte[] message,int length){
        //String strMsg = new String(message);
        //int dataLoc = strMsg.indexOf("$")+1;
        //String dataPortion = strMsg.substring(dataLoc);
        byte[] data = new byte[length - (type.length()+"$#".length())];
        int count=0;
        for(int i=(type.length()+"$#".length());i<length;i++)
            data[count++] = message[i];
        return data;
    }
    
    /**
     * Returns the message as a string "TCPMessage(Type: <type>, Data: <data>)
     */
    public String toString(){
    	 String strMsg = new String(data);
    	 return "TCPMessage (Type: "+type+", Data: "+strMsg+")";
    }

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}
	
	/**
	 * 
	 * @return the data as a String
	 */
	public String getDataAsString(){
		String strMsg = new String(data);
		return strMsg;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(byte[] data) {
		this.data = new byte[data.length];
		this.data = data;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
}
