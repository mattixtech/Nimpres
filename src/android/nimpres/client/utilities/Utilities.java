/**
 * Project:			Nimpres Android Client
 * File name: 		Utilities.java
 * Date modified:	2011-02-02
 * Description:		Provides utility methods for use accross the client
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
package android.nimpres.client.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.util.Log;

public class Utilities {

	public static String getLocalIpAddress() {
	    try {
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress.getHostAddress().toString();
	                }
	            }
	        }
	    } catch (SocketException ex) {
	        Log.e("Utilities", ex.toString());
	    }
	    return null;
	}
	
	/**
	 * Deletes the requested directory and all files inside of it.
	 * @param path
	 * @return
	 */
	static public boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	
	/**
	 * Extracts a zipped file to the requested folder, first deleting the contents of the requested folder.
	 * @param fileName
	 * @param toFolder
	 * @param ctx
	 * @return
	 */
	static public String unzip(String fileName, String toFolder, Context ctx) {
		
		Log.d("Utilities","attempting unzip: "+fileName+" to: "+toFolder);
		String ret="";
		try {
			ZipInputStream in = null;
			String zipPath = fileName;
			in = new ZipInputStream(ctx.openFileInput(zipPath));
			byte[] buf = new byte[1024];

			File dirToMake = ctx.getDir(toFolder, Context.MODE_WORLD_WRITEABLE);
			deleteDirectory(dirToMake);
			dirToMake = ctx.getDir(toFolder, Context.MODE_WORLD_WRITEABLE);
			Log.d("Utilities", "Dir to save to: " + dirToMake);

			for (ZipEntry entry = in.getNextEntry(); entry != null; entry = in
					.getNextEntry()) {
				Log.d("Utilities", "Extracting: " + entry);
				String entryName = entry.getName();
				int n;
				FileOutputStream fileoutputstream;
				File newFile = new File(dirToMake, entryName);
				fileoutputstream = new FileOutputStream(newFile);
				while ((n = in.read(buf, 0, 1024)) > -1)
					fileoutputstream.write(buf, 0, n);
				fileoutputstream.close();
				in.closeEntry();
				//ctx.deleteFile(fileName);
			}
			ret = dirToMake.toString();
		} catch (Exception e) {
			Log.d("Utilities","Unzip exception: "+e.getMessage());
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	public static boolean isInternetLocation(String location){
		if(location.indexOf("http://") >= 0 || location.indexOf("https://") >= 0 || location.indexOf("ftp://") >= 0)
			return true;
		return false;
	}
	
}
