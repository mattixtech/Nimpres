/*
The person or persons who have associated work with this document (the "Dedicator" or "Certifier") hereby either (a) certifies that, to the best of his knowledge, the work of authorship identified is in the public domain of the country from which the work is published, or (b) hereby dedicates whatever copyright the dedicators holds in the work of authorship identified below (the "Work") to the public domain. A certifier, moreover, dedicates any copyright interest he may have in the associated work, and for these purposes, is described as a "dedicator" below.
A certifier has taken reasonable steps to verify the copyright status of this work. Certifier recognizes that his good faith efforts may not shield him from liability if in fact the work certified is not in the public domain.
Dedicator makes this dedication for the benefit of the public at large and to the detriment of the Dedicator's heirs and successors. Dedicator intends this dedication to be an overt act of relinquishment in perpetuity of all present and future rights under copyright law, whether vested or contingent, in the Work. Dedicator understands that such relinquishment of all rights includes the relinquishment of all rights to enforce (by lawsuit or otherwise) those copyrights in the Work.
Dedicator recognizes that, once placed in the public domain, the Work may be freely reproduced, distributed, transmitted, used, modified, built upon, or otherwise exploited by anyone for any purpose, commercial or non-commercial, and in any way, including by methods that have not yet been invented or conceived.
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
				ctx.deleteFile(fileName);
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
