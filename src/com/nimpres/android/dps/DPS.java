/**
 * Project:			Nimpres Android Client
 * File name: 		DPS.java
 * Date modified:	2011-03-17
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
package com.nimpres.android.dps;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nimpres.android.lan.TCPMessage;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.settings.NimpresSettings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

/*This class represents a fully extracted and read DSP presentation package*/

public class DPS {
	private String dpsPath = ""; 			//Stores the the on-disk path of where the extracted dps content is located
	private Presentation dpsPres = null; 	//Stores a presentation representation of the DPS file's contents
	private boolean isRemote = false; 		//Specifies whether this dps project originiated on device or was downloaded via LAN/Internet
	private String remoteType = "";			//"lan" or "internet"
	private String dpsOrigin = ""; 			//Stores the URL origin or IP Address of the DPS file's original location
	private int dpsID = -1;				//Stores the ID of this DPS file for synchronizing
	private String dpsPassword = "";		//Stores a password for password protected synchronizing
	
	/**
	 * Create a DPS from an ondevice DPS file
	 * @param dpsFolder
	 */
	public DPS(String dpsPath, String desiredFolderName, Context ctx){
		try {
			this.dpsPath = downloadFromSD(dpsPath, desiredFolderName, ctx);
		} catch (Exception e) {
			Log.d("DPS","Error: "+e.getMessage());
		}
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
	public DPS(String dpsLocation, String remoteType, int dpsID, String dpsPassword, String desiredFolderName, Context ctx){
		isRemote = true;
		dpsOrigin = dpsLocation;			
		this.dpsID = dpsID;
		this.dpsPassword = dpsPassword;
		if(Utilities.isOnline(ctx)){
			if(remoteType.equalsIgnoreCase("internet")){
				this.remoteType = "internet";
				try {
					dpsPath = downloadFromAPI(dpsID,dpsPassword,NimpresSettings.API_DOWNLOAD_PREFIX+desiredFolderName,desiredFolderName,ctx);
				} catch (Exception e) {
					Log.d("DPS","Error: "+e.getMessage());
				}
			}else if(remoteType.equalsIgnoreCase("lan")){
				this.remoteType = "lan";
				try {
					dpsPath = downloadFromLAN(dpsLocation, dpsID, dpsPassword, desiredFolderName, ctx);
				} catch (Exception e) {
					Log.d("DPS","Error: "+e.getMessage());
				}				
			}
			if( ! dpsPath.equals("")){
				dpsPres = DPSReader.makePresentation(dpsPath);
			}else{
				Log.d("DPS","no dps file could be found");
			}
		}else
			Log.d("DPS","Error: device is offline");
	}
	
	/**
	 * Download a dps package using the NimpresAPI
	 * @param id
	 * @param password
	 * @param fileName
	 * @param folderToSave
	 * @param ctx
	 * @return
	 * @throws Exception 
	 */
	public static String downloadFromAPI(int id, String password, String fileName, String folderToSave, Context ctx) throws Exception{
		String ret = null;
		//ByteBuffer downloadedResponse = ByteBuffer.allocate(NimpresSettings.MAX_PRESENTATION_SIZE);
		if(Utilities.isOnline(ctx)){
			try {
				Log.d("DPSGet", "download begining from api");
				Log.d("DPSGet","id:"+id+" password:"+password);
				InputStream is = APIContact.downloadPresentation(id, password);
				if(is != null){
					FileOutputStream fos = ctx.openFileOutput(fileName,Context.MODE_PRIVATE);
					
					/*Save downloaded file to disk*/			
					byte buf[]=new byte[1024];
				    int len;
				    long byteCounter = 0;
				    long downloadedCounter = 0;
				    long mbCounter = 0;
				    
				    while((len=is.read(buf))>0){
				    	fos.write(buf,0,len);
				    	byteCounter += len;
				    	downloadedCounter += len;
				    	if(downloadedCounter > 1024*1024){
				    		Log.d("DPS","downloaded "+ ++mbCounter +" MB");
				    		downloadedCounter = 0;
				    	}		    	
				    }
				    fos.close();
				    is.close();
				    Log.d("DPSGet", "downloaded file");			    			 				
					ret = Utilities.unzip(fileName, folderToSave, ctx); //Unzip package to requested folder and delete original file
				}else{
					Log.d("DPSGet", "unable to download dps file");
				}
			} catch (Exception e) {
				Log.d("DPSGet", "Error: " + e.getMessage());
			}
		}else{
			Log.d("DPSGet", "Error: device is not online");
		}
		if(ret == null){
			throw new Exception("unable to download");
		}else{
			return ret;
		}
	}
	
	
	
	/**
	 * Copy a dps package off of the SD card and extract it to the desired location
	 * @param fileName
	 * @param folderToSave
	 * @param ctx
	 * @return
	 * @throws Exception 
	 */
	private static String downloadFromSD(String fileName, String folderToSave,
			Context ctx) throws Exception {
		
		String ret = null;
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
		if(ret == null){
			throw new Exception("unable to download");
		}else{
			return ret;
		}
	}
	
	/**
	 * 
	 * @param ipAddress
	 * @param dpsID
	 * @param dpsPassword
	 * @param folderToSave
	 * @param ctx
	 * @return 
	 * @throws Exception 
	 */
	private static String downloadFromLAN(String ipAddress, int dpsID, String dpsPassword, String folderToSave, Context ctx) throws Exception{
		String ret = null;
		Socket connectionToLANPeer = null;
		if(Utilities.isOnline(ctx)){
			try{
				//Open socket connection to peer
				Log.d("DPS","attempting to connect to peer:"+ipAddress);
				connectionToLANPeer = new Socket(InetAddress.getByName(ipAddress.trim()),NimpresSettings.SERVER_FILE_PORT);
				
				if(connectionToLANPeer.isConnected()){//Check if socket is connected
					Log.d("DPS","connected to peer");
					DataOutputStream out = new DataOutputStream(connectionToLANPeer.getOutputStream());
					DataInputStream in = new DataInputStream(connectionToLANPeer.getInputStream());
					
					//Create and send the TCP request message
					String msgReq = dpsID + "__" + dpsPassword;//Format the request message				
					TCPMessage outMsg = new TCPMessage(NimpresSettings.MSG_REQUEST_FILE_TRANSFER,msgReq.getBytes(),out);
	
					Log.d("DPS","sent message to peer:"+msgReq);
					
					//Receive the response message
					TCPMessage inMsg = new TCPMessage(in);
					Log.d("DPS","received response from peer: "+inMsg);
					//Check the type of the response message
					if(inMsg.getType().equals(NimpresSettings.MSG_RESPONSE_FILE_TRANSFER)){
						//Server responded with the file
						Log.d("DPS","peer transfered file");
						
						//Store the data portion of the received message
						byte[] receivedFile = inMsg.getData();
						
						//Attempt to write the received dps file to disk and then extract it					
						FileOutputStream fos = ctx.openFileOutput(String.valueOf(dpsID), Context.MODE_WORLD_READABLE);
						fos.write(receivedFile);
						fos.close();
						File wroteFile = new File(ctx.getFilesDir()+File.separator+dpsID);
						if(wroteFile.exists())
							Log.d("DPS","file wrote successfully");
						else
							Log.d("DPS","file not wrote");
						
						
						Log.d("DPS","just wrote to disk: "+ctx.getFilesDir()+File.separator+dpsID);
						ret = Utilities.unzip(String.valueOf(dpsID), folderToSave, ctx);
						Log.d("DPS","extracted file to:"+ret);
					}else if(inMsg.getType().equals(NimpresSettings.MSG_RESPONSE_INVALID_REQ)){
						//Server denied transfer due to invalid id/password
						Log.d("DPS","peer denied transfer");			
					}else{
						//Something else went wrong
						Log.d("DPS","dps transfer failed");					}
				}else{
					Log.d("DPS","could not connect to peer:"+ipAddress);
				}
			}catch(Exception e){
				Log.d("DPS","Exception:"+e.getMessage());
			}
		}else{
			Log.d("DPSGet", "Error: device is not online");
		}
		if(ret == null){
			throw new Exception("unable to download");
		}else{
			return ret;
		}
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
	public int getDpsID() {
		return dpsID;
	}

	/**
	 * @param dpsID the dpsID to set
	 */
	public void setDpsID(int dpsID) {
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
