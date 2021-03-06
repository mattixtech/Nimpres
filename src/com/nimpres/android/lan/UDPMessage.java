/**
 * Project:			Nimpres Android Client
 * File name: 		UDPMessage.java
 * Date modified:	2011-03-18
 * Description:		UDP Message sending/receiving
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
import java.net.InetSocketAddress;

import android.util.Log;

public class UDPMessage {
	private byte[] data = { 0 };
	private String type = "";
	private int length = 0;
	InetAddress remoteIP = null;
	boolean broadcast = false;

	/**
	 * Default empty constructor
	 */
	public UDPMessage() {
	}

	/**
	 * Get a new UDPMessage
	 * 
	 * @param port
	 *            The port to listen on for the packet
	 * @param size
	 *            The size of the buffer to use receiving, a message bigger than the buffer size will be truncated
	 */
	public UDPMessage(int port, int size) {
		getMessage(port, size);
		this.length = this.type.length() + this.data.length + "#$".length();
	}

	/**
	 * Standard constructor to create a message
	 * 
	 * @param type
	 * @param data
	 */
	public UDPMessage(String type, byte[] data) {
		this.type = type;
		this.data = new byte[data.length];
		this.data = data;
		this.length = this.type.getBytes().length + this.data.length + "#$".getBytes().length;
	}

	/**
	 * Create UDPMessage and send right away
	 * 
	 * @param type
	 * @param data
	 * @param ip
	 * @param port
	 */
	public UDPMessage(String type, byte[] data, String ip, int port) {
		this(type, data);
		sendMessage(ip, port);
	}

	/**
	 * Create UDPMessage and send right away as broadcast
	 * 
	 * @param type
	 * @param data
	 * @param ip
	 * @param port
	 * @param brodcastMessage
	 */
	public UDPMessage(String type, byte[] data, String ip, int port, boolean brodcastMessage) {
		this(type, data);
		setBroadcast(brodcastMessage);
		sendMessage(ip, port);
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
	public String getDataAsString() {
		String strMsg = new String(data);
		return strMsg;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Manually read a message
	 * 
	 * @param port
	 *            The port to listen on for the packet
	 * @param size
	 *            The size of the buffer to use receiving, a message bigger than the buffer size will be truncated
	 */
	public void getMessage(int port, int size) {
		try {
			// DatagramSocket inputSocket = new DatagramSocket(port,InetAddress.getByName(Utilities.getLocalIpAddress()));
			DatagramSocket inputSocket = new DatagramSocket(null);
			inputSocket.setReuseAddress(true);
			inputSocket.bind(new InetSocketAddress(port));
			byte[] inputBuff = new byte[size];
			DatagramPacket pkt = new DatagramPacket(inputBuff, size);
			inputSocket.receive(pkt);
			this.remoteIP = pkt.getAddress();
			Log.d("UDPMessage", "received udp packet: " + new String(pkt.getData()));
			type = parseType(pkt.getData());
			data = parseData(type, pkt.getData());
			Log.d("UDPMessage", "Received message: " + type + ", with data: " + new String(data));
		}
		catch (Exception e) {
			Log.d("UDPMessage", "error: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @return the remoteIP
	 */
	public InetAddress getRemoteIP() {
		return remoteIP;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the broadcast
	 */
	public boolean isBroadcast() {
		return broadcast;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private byte[] mergeBytes(byte[] a, byte[] b) {
		byte[] c = new byte[a.length + b.length];
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}

	/**
	 * Retrieve the data
	 * 
	 * @param type
	 * @param message
	 * @return
	 */
	private byte[] parseData(String type, byte[] message) {
		int length = message.length;
		byte[] data = new byte[length - (type.length() + "$#".length())];
		int count = 0;
		for (int i = (type.length() + "$#".length()); i < length; i++)
			data[count++] = message[i];
		return data;
	}

	/**
	 * Retrieve the type
	 * 
	 * @param msg
	 * @return
	 */
	private String parseType(byte[] msg) {
		String strMsg = new String(msg);
		return strMsg.substring(strMsg.indexOf("#") + 1, strMsg.indexOf("$"));
	}

	/**
	 * Manually send the message
	 * 
	 * @param ip
	 * @param port
	 */
	public void sendMessage(String ip, int port) {
		if (!type.equals("") && length > 0) {
			try {
				String messageHead = "#" + type + "$";
				InetAddress ipAddress = InetAddress.getByName(ip);
				DatagramSocket outputSocket = new DatagramSocket();

				// The code below concatenates the byte arrays of the type and data together as one byte array
				byte[] messageHeadBytes = messageHead.getBytes();
				byte[] dataToSend = mergeBytes(messageHeadBytes, data);

				DatagramPacket pkt = new DatagramPacket(dataToSend, dataToSend.length, ipAddress, port);
				if (isBroadcast()) {
					outputSocket.setBroadcast(true);
					outputSocket.setReuseAddress(true);
				}
				Log.d("UDPMessage", "Sent message: " + new String(pkt.getData()));
				outputSocket.send(pkt);

			}
			catch (Exception e) {
				Log.d("UDPMessage", "error: " + e.getMessage());
				e.printStackTrace();
			}
		}
		else
			Log.d("UDPMessage", "attempted to send empty message");
	}

	/**
	 * @param isBroadcast
	 *            the isBroadcast to set
	 */
	public void setBroadcast(boolean broadcast) {
		this.broadcast = broadcast;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(byte[] data) {
		this.data = new byte[data.length];
		this.data = data;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param remoteIP
	 *            the remoteIP to set
	 */
	public void setRemoteIP(InetAddress remoteIP) {
		this.remoteIP = remoteIP;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the message as a string "UDPMessage(Type: <type>, Data: <data>)
	 */
	public String toString() {
		String strMsg = new String(data);
		return "UDPMessage (Type: " + type + ", Data: " + strMsg + ")";
	}
}
