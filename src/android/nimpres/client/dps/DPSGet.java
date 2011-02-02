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

/*Imports*/
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.http.util.ByteArrayBuffer;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
/*********/

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
	 * @return
	 */
	public static String DownloadFromURL(String packageURL, String fileName,
			String folderToSave, Context ctx) {
		String ret="";
		try {
			/* Download the specified presentation off of the Internet */
			/******************************************************/
			URL url = new URL(packageURL);
			Log.d("PresentationPackage", "download begining");
			Log.d("PresentationPackage", "download url:" + url);
			Log.d("PresentationPackage", "downloaded file");
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
			ret = unzip(fileName, folderToSave, ctx);

		} catch (Exception e) {
			Log.d("Error:", "Error: " + e);
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
			
			Log.d("PresentationPackage", "checking SD card");
			if (sd.exists()) {
				Log.d("PresentationPackage", "found SD card");
				File toCopy = new File(sd, fileName);
				
				if (toCopy.exists()) {
					Log.d("PresentationPackage", "found file");
					FileChannel src = new FileInputStream(toCopy).getChannel();
					FileChannel dst = ctx.openFileOutput(fileName, Context.MODE_PRIVATE).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					Log.d("PresentationPackage", "file copied to internal storage");
				}
			}
			/******************************************************/
			/*Unzip package to requested folder and delte original file*/
			ret = unzip(fileName, folderToSave, ctx);

		} catch (Exception e) {
			Log.d("Error:", "Error: " + e);
		}
		
		return ret;
	}
	
	/**
	 * Extracts a zipped file to the requested folder, first deleting the contents of the requested folder.
	 * @param fileName
	 * @param toFolder
	 * @param ctx
	 * @return
	 */
	static public String unzip(String fileName, String toFolder, Context ctx) {
		String ret="";
		try {
			ZipInputStream in = null;
			String zipPath = fileName;
			in = new ZipInputStream(ctx.openFileInput(zipPath));
			byte[] buf = new byte[1024];

			File dirToMake = ctx.getDir(toFolder, Context.MODE_WORLD_WRITEABLE);
			deleteDirectory(dirToMake);
			dirToMake = ctx.getDir(toFolder, Context.MODE_WORLD_WRITEABLE);
			Log.d("PresentationPackage", "Dir to save to: " + dirToMake);

			for (ZipEntry entry = in.getNextEntry(); entry != null; entry = in
					.getNextEntry()) {
				Log.d("PresentationPackage", "Extracting: " + entry);
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
			ret= dirToMake.toString();
		} catch (Exception e) {
			
		}
		return ret;
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
}
