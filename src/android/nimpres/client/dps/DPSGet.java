/**
 * Project:			Nimpres Android Client
 * File name: 		DPSGet.java
 * Date modified:	2011-02-02
 * Description:		Static methods for downloading a Nimpres DPS package
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import org.apache.http.util.ByteArrayBuffer;
import android.content.Context;
import android.nimpres.client.utilities.Utilities;
import android.os.Environment;
import android.util.Log;


/**
 * This class offers static methods for storing and extracting dsp packages
 */
public class DPSGet {

	/**
	 * Download a dsp package off of the Internet and extract it to the desired folder.
	 * @param packageURL
	 * @param fileName
	 * @param folderToSave
	 * @param ctx
	 * @return the folder name of the extracted dps
	 */
	public static String DownloadFromURL(String packageURL, String fileName,
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
					Context.MODE_WORLD_READABLE);// new FileOutputStream(file);
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
	 * Copy a dsp package off of the SD card and extract it to the desired location
	 * @param fileName
	 * @param folderToSave
	 * @param ctx
	 * @return
	 */
	public static String DownloadFromSD(String fileName, String folderToSave,
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
	
	
	
	
}
