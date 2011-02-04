/**
 * Project:			Nimpres Android Client
 * File name: 		DPS.java
 * Date modified:	2011-02-03
 * Description:		This class defines a DPS package
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
package android.nimpres.client.dps;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.nimpres.client.lan.Message;
import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.settings.NimpresSettings;
import android.nimpres.client.utilities.Utilities;
import android.os.Environment;
import android.util.Log;

/*This class represents a fully extracted and read DSP presentation package*/

public class DPS {
	private String dpsPath = ""; 			//Stores the the on-disk path of where the extracted dps content is located
	private Presentation dpsPres = null; 	//Stores a presentation representation of the DPS file's contents
	private boolean isRemote = false; 		//Specifies whether this dps project originiated on device or was downloaded via LAN/Internet
	private String remoteType = "";			//"lan" or "internet"
	private String dpsOrigin = ""; 			//Stores the URL origin or IP Address of the DPS file's original location
	private String dpsID = "";		//Stores the ID of this DPS file for synchronizing
	private String dpsPassword = "";	//Stores a password for password protected synchronizing
	
	/**
	 * Create a DPS from an ondevice DPS file
	 * @param dpsFolder
	 */
	public DPS(String dpsPath, String desiredFolderName, Context ctx){
		this.dpsPath = downloadFromSD(dpsPath, desiredFolderName, ctx);
		dpsOrigin = "device";
		dpsPres = DPSReader.makePresentation(dpsPath);
	}
	
	/**
	 * Create a DPS from a remote source (LAN/Internet)
	 * @param dpsLocation
	 * @param remoteType (must be either 'lan' or 'internet')
	 * @param dpsID
	 * @param dpsPassword
	 * @param desiredFolderName
	 * @param ctx
	 */
	public DPS(String dpsLocation, String remoteType, String dpsID, String dpsPassword, String desiredFolderName, Context ctx){
		isRemote = true;
		dpsOrigin = dpsLocation;		
		
		this.dpsID = dpsID;
		this.dpsPassword = dpsPassword;
		
		if(remoteType.equalsIgnoreCase("internet")){
			this.remoteType = "internet";
			dpsPath = downloadFromURL(dpsLocation+"?id="+this.dpsID+"&password="+this.dpsPassword, "tmp"+desiredFolderName+".dps", desiredFolderName, ctx);
			dpsPres = DPSReader.makePresentation(dpsPath);
		}else if(remoteType.equalsIgnoreCase("lan")){
			this.remoteType = "lan";
			dpsPath = downloadFromLAN(dpsLocation, dpsID, dpsPassword, desiredFolderName, ctx);
			dpsPres = DPSReader.makePresentation(dpsPath);
		}
	}
	
	/**
	 * Download a dps package off of the Internet and extract it to the desired folder.
	 * @param packageURL
	 * @param fileName
	 * @param folderToSave
	 * @param ctx
	 * @return the folder name of the extracted dps
	 */
	private static String downloadFromURL(String packageURL, String fileName,
			String folderToSave, Context ctx) {
		String ret="";
		try {
			/* Download the specified presentation off of the Internet */
			/******************************************************/
			URL url = new URL(packageURL);
			Log.d("DPSGet", "download begining");
			Log.d("DPSGet", "download url:" + url);
			Log.d("DPSGet", "downloaded file");
			URLConnection ucon = url.openConnection();
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			
			/*Save downloaded file to disk*/
			FileOutputStream fos = ctx.openFileOutput(fileName,
					Context.MODE_PRIVATE);// new FileOutputStream(file);
			fos.write(baf.toByteArray());
			fos.close();
			/******************************************************/
			
			/*Unzip package to requested folder and delete original file*/
			ret = Utilities.unzip(fileName, folderToSave, ctx);

		} catch (Exception e) {
			Log.d("DPSGet", "Error: " + e);
		}
		return ret;
	}
	
	/**
	 * Copy a dps package off of the SD card and extract it to the desired location
	 * @param fileName
	 * @param folderToSave
	 * @param ctx
	 * @return
	 */
	private static String downloadFromSD(String fileName, String folderToSave,
			Context ctx) {
		
		String ret="";
		try {

			/* Download the specified Presentation off of the SD Card */
			/******************************************************/
			File sd = Environment.getExternalStorageDirectory();
			
			Log.d("DPSGet", "checking SD card");
			if (sd.exists()) {
				Log.d("DPSGet", "found SD card");
				File toCopy = new File(sd, fileName);
				
				if (toCopy.exists()) {
					Log.d("DPSGet", "found file");
					FileChannel src = new FileInputStream(toCopy).getChannel();
					FileChannel dst = ctx.openFileOutput(fileName, Context.MODE_PRIVATE).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					Log.d("DPSGet", "file copied to internal storage");
				}
			}
			/******************************************************/
			/*Unzip package to requested folder and delte original file*/
			ret = Utilities.unzip(fileName, folderToSave, ctx);

		} catch (Exception e) {
			Log.d("DPSGet", "Error: " + e);
		}		
		return ret;
	}
	
	/**
	 * 
	 * @param ipAddress
	 * @param dpsID
	 * @param dpsPassword
	 * @param folderToSave
	 * @param ctx
	 * @return 
	 */
	private static String downloadFromLAN(String ipAddress, String dpsID, String dpsPassword, String folderToSave, Context ctx){
		String ret = "";
		Socket connectionToLANPeer = null;
		try{
			//Open socket connection to peer
			Log.d("DPS","attempting to connect to peer:"+ipAddress);
			connectionToLANPeer = new Socket(InetAddress.getByName(ipAddress.trim()),NimpresSettings.SERVER_FILE_PORT);
			
			if(connectionToLANPeer.isConnected()){//Check if socket is connected
				Log.d("DPS","connected to peer");
				DataOutputStream out = new DataOutputStream(connectionToLANPeer.getOutputStream());
				DataInputStream in = new DataInputStream(connectionToLANPeer.getInputStream());
				String msgReq = dpsID + "__" + dpsPassword;//Format the request message
				Message.sendMessage(out, NimpresSettings.MSG_REQUEST_FILE_TRANSFER,msgReq.getBytes());
				Log.d("DPS","sent message to peer:"+msgReq);
				byte[] resPkt  = Message.getMessage(in); //Get the response data from peer
				
				if(Message.hasType(resPkt, NimpresSettings.MSG_RESPONSE_FILE_TRANSFER)){
					//Server responded with the file
					Log.d("DPS","peer transfered file");
					byte[] receivedFile = Message.parseBinData(resPkt);
					//Attempt to write the received dps file to disk and then extract it
					
					FileOutputStream fos = ctx.openFileOutput(dpsID, Context.MODE_PRIVATE);
					fos.write(receivedFile);
					fos.close();
					ret = Utilities.unzip(ctx.getFilesDir()+File.separator+dpsID, folderToSave, ctx);
					Log.d("DPS","extracted file to:"+ret);
				}else if(Message.hasType(resPkt, NimpresSettings.MSG_RESPONSE_INVALID_REQ)){
					//Server denied transfer due to invalid id/password
					Log.d("DPS","peer denied transfer");
					ret = "__invalid";				
				}else{
					//Something else went wrong
					Log.d("DPS","dps transfer failed");
					ret = "__error";
				}
			}else{
				Log.d("DPS","could not connect to peer:"+ipAddress);
			}
		}catch(Exception e){
			Log.d("DPS","Exception:"+e.getMessage());
		}
		return ret;
	}

	/**
	 * @return the dpsPath
	 */
	public String getDpsPath() {
		return dpsPath;
	}

	/**
	 * @param dpsPath the dpsPath to set
	 */
	public void setDpsPath(String dpsPath) {
		this.dpsPath = dpsPath;
	}

	/**
	 * @return the dpsPres
	 */
	public Presentation getDpsPres() {
		return dpsPres;
	}

	/**
	 * @param dpsPres the dpsPres to set
	 */
	public void setDpsPres(Presentation dpsPres) {
		this.dpsPres = dpsPres;
	}

	/**
	 * @return the isRemote
	 */
	public boolean isRemote() {
		return isRemote;
	}

	/**
	 * @param isRemote the isRemote to set
	 */
	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}

	/**
	 * @return the remoteType
	 */
	public String getRemoteType() {
		return remoteType;
	}

	/**
	 * @param remoteType the remoteType to set
	 */
	public void setRemoteType(String remoteType) {
		this.remoteType = remoteType;
	}

	/**
	 * @return the dpsOrigin
	 */
	public String getDpsOrigin() {
		return dpsOrigin;
	}

	/**
	 * @param dpsOrigin the dpsOrigin to set
	 */
	public void setDpsOrigin(String dpsOrigin) {
		this.dpsOrigin = dpsOrigin;
	}

	/**
	 * @return the dpsID
	 */
	public String getDpsID() {
		return dpsID;
	}

	/**
	 * @param dpsID the dpsID to set
	 */
	public void setDpsID(String dpsID) {
		this.dpsID = dpsID;
	}

	/**
	 * @return the dpsPassword
	 */
	public String getDpsPassword() {
		return dpsPassword;
	}

	/**
	 * @param dpsPassword the dpsPassword to set
	 */
	public void setDpsPassword(String dpsPassword) {
		this.dpsPassword = dpsPassword;
	}
	
}
