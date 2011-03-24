/**
 * Project:			Nimpres Android Client
 * File name: 		FileUploader.java
 * Date modified:	2011-03-17
 * Description:		Upload a file to a webserver
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
package com.nimpres.android.web;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

import com.nimpres.android.NimpresObjects;
import com.nimpres.android.settings.NimpresSettings;

public class FileUploader {

	private URL connectURL;
	private FileInputStream fileInputStream = null;
	private String responseMessage = NimpresSettings.API_RESPONSE_NEGATIVE; //default to negative response
	
	public FileUploader(String urlString, String fileName ){
		try{
			connectURL = new URL(urlString);
			Log.d("FileUploader","creating upload request to: "+urlString);
			this.fileInputStream = NimpresObjects.ctx.openFileInput(fileName);
		}catch(Exception e){
			Log.d("FileUploader","Error: "+e.getMessage());
		}
	}	
	
	public String upload(){
		String exsistingFileName = "NimpresFile.uploaded";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		
		try{
			HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);	
			Log.d("FileUploader","starting post");
			// Use a post method.
			conn.setRequestMethod("POST");	
			conn.setRequestProperty("Connection", "Keep-Alive");	
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);	
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + exsistingFileName +"\"" + lineEnd);
			dos.writeBytes(lineEnd);

			int bytesAvailable = fileInputStream.available();
			int maxBufferSize = 1024;
			int bufferSize = Math.min(bytesAvailable, maxBufferSize);
			byte[] buffer = new byte[bufferSize];

			int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0)
			{
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			fileInputStream.close();
			dos.flush();
			
			InputStream is = conn.getInputStream();
			// retrieve the response from server
			int ch;

			StringBuffer b =new StringBuffer();
			while( ( ch = is.read() ) != -1 ){
				b.append( (char)ch );
			}
			String s = b.toString();
			responseMessage = s;
			Log.d("FileUploader","response: "+s);
			dos.close();
			
		}catch(Exception e){
			Log.d("FileUploader","Error: "+e.getMessage());
		}
		
		return responseMessage;
	}
}
