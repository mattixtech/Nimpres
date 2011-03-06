/**
 * Project:			Nimpres Android Client
 * File name: 		APIContact.java
 * Date modified:	2011-02-02
 * Description:		Static methods for performing api calls to the webserver
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
package android.nimpres.client.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.nimpres.client.settings.NimpresSettings;
import android.util.Log;

/**
 * 
 * @author
 *
 */
public class APIContact {

	/**
	 * Returns the full URL of the requested api call
	 * @param apiCall
	 * @return the full URL of the requested api call
	 */
	public static String getAPIAddress(String apiCall){
		return NimpresSettings.API_BASE_URL + apiCall +".php";
	}
	
	/**
	 * Performs a post request to the specified api with the specified parameters and returns the result
	 * @param apiAddress
	 * @param postParams
	 * @return the result in an HttpEntity object
	 */
	public static HttpEntity apiPostRequest(String apiAddress,List<NameValuePair> postParams){
		HttpEntity resEntity = null;
		try {
			HttpClient client = new DefaultHttpClient();  
	        //Set address for API call
	        HttpPost post = new HttpPost(getAPIAddress(apiAddress));
	        //Add post parameters to request
	        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(postParams,HTTP.UTF_8);
	        post.setEntity(ent);
	        //Send post and store result
	        Log.d("APIContact","performing post to:"+apiAddress);
            HttpResponse responsePOST = client.execute(post);  
            resEntity = responsePOST.getEntity();
            if (resEntity != null) { 
            	return resEntity;
            }
		} catch (Exception e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	/**
	 * This method validates a login/password combination
	 * @param login
	 * @param password
	 * @return true if valid login, false otherwise
	 */
	public static boolean validateLogin(String login, String password){
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("login", login));
        params.add(new BasicNameValuePair("password", password));
        HttpEntity resEntity = apiPostRequest(NimpresSettings.API_LOGIN,params);
		try{
			result = EntityUtils.toString(resEntity);
			Log.d("APIContact","post result:"+result);
		}catch (Exception e) {
	        e.printStackTrace();
	    }
		if(result.equals(NimpresSettings.API_RESPONSE_POSITIVE))
			return true;
		return false;
	}
	
	
	public static int getSlideNumber(String id, String password){
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", id));
		params.add(new BasicNameValuePair("password", password));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_SLIDE,params);
		try{
			result = EntityUtils.toString(resEntity);
			Log.d("APIContact","post result:"+result);
		}catch (Exception e) {
	        e.printStackTrace();
	    }
		
		if( ! result.equals(NimpresSettings.API_RESPONSE_NEGATIVE))
			return Integer.parseInt(result);
		else
			return 0;
	}
	
	public static void pushDPSToWeb(String login, String password, String fileName, String pesentationTitle, boolean passwordProtect){
		
		/*Posting file cod
		 	File file = new File("path/to/your/file.txt");
			try {
			         HttpClient client = new DefaultHttpClient();  
			         String postURL = "http://someposturl.com";
			         HttpPost post = new HttpPost(postURL); 
			     FileBody bin = new FileBody(file);
			     MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);  
			     reqEntity.addPart("myFile", bin);
			     post.setEntity(reqEntity);  
			     HttpResponse response = client.execute(post);  
			     HttpEntity resEntity = response.getEntity();  
			     if (resEntity != null) {    
			               Log.i("RESPONSE",EntityUtils.toString(resEntity));
			         }
			} catch (Exception e) {
			    e.printStackTrace();
			}
		 */
	}
}
