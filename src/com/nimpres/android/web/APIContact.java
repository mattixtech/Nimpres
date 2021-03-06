/**
 * Project:			Nimpres Android Client
 * File name: 		APIContact.java
 * Date modified:	2011-03-18
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
package com.nimpres.android.web;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.nimpres.android.settings.NimpresSettings;

/**
 * 
 * @author
 * 
 */
public class APIContact {

	/**
	 * Performs a post request to the specified api with the specified parameters and returns the result
	 * 
	 * @param apiAddress
	 * @param postParams
	 * @return the result in an HttpEntity object
	 */
	public static BufferedHttpEntity apiPostRequest(String apiAddress, List<NameValuePair> postParams) {
		BufferedHttpEntity resEntity = null;
		try {
			HttpClient client = new DefaultHttpClient();
			// Set address for API call
			HttpPost post = new HttpPost(getAPIAddress(apiAddress));
			// Add post parameters to request
			UrlEncodedFormEntity ent = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
			post.setEntity(ent);
			// Send post and store result
			Log.d("APIContact", "Contacting API - api-method:" + apiAddress + ", api-query: " + postParams);
			HttpResponse responsePOST = client.execute(post);
			resEntity = new BufferedHttpEntity(responsePOST.getEntity());
			String result = EntityUtils.toString(resEntity);
			Log.d("APIContact", "post result: " + result);
			if (resEntity != null) {
				return resEntity;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * This method creates a presentation entry in the pres and pres_status tables
	 * 
	 * @param user
	 * @param title
	 * @param password
	 * @param length
	 * @param slide_num
	 * @param status
	 * @param over
	 * @return
	 */
	public static int createPresentation(String userID, String userPass, String title, String presPass, int length, String fileName) {

		String queryString = "?title=" + title + "&user_id=" + userID + "&user_password=" + userPass + "&pres_password=" + presPass + "&length=" + length;
		String url = NimpresSettings.API_BASE_URL + NimpresSettings.API_CREATE_PRESENTATION + NimpresSettings.API_EXTENSION;
		String queryUrl = url + queryString;
		FileUploader upFile = new FileUploader(queryUrl, fileName);
		String response = upFile.upload();
		Log.d("APIContact", "FileUploader's response: " + response);
		if (!response.equals(NimpresSettings.API_RESPONSE_NEGATIVE))
			return Integer.parseInt(response);
		else
			return -1;
	}

	/**
	 * This method creates a user with a login/password combination
	 * 
	 * @param user_id
	 * @param user_password
	 * @return true if user is created, false otherwise
	 */
	public static boolean createUser(String login, String password) {
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", login));
		params.add(new BasicNameValuePair("user_password", password));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_CREATE_ACCOUNT, params);
		try {
			result = EntityUtils.toString(resEntity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (result.equals(NimpresSettings.API_RESPONSE_POSITIVE))
			return true;
		return false;
	}

	/**
	 * This method deletes a presentation from pres and pres_status based on id
	 * 
	 * @param id
	 * @param password
	 * @return true if sucessfully deleted, false otherwise
	 */
	public static boolean deletePresentation(String userID, String userPass, int presID, String presPass) {
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", userID));
		params.add(new BasicNameValuePair("user_password", userPass));
		params.add(new BasicNameValuePair("pres_id", String.valueOf(presID)));
		params.add(new BasicNameValuePair("pres_password", presPass));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_DELETE_PRESENTATION, params);
		try {
			result = EntityUtils.toString(resEntity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (result.equals(NimpresSettings.API_RESPONSE_POSITIVE))
			return true;
		return false;
	}

	/**
	 * Downloads the presentation dps file
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public static InputStream downloadPresentation(int presID, String presPass) {
		InputStream downloadedDps = null;
		String response = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pres_id", String.valueOf(presID)));
		params.add(new BasicNameValuePair("pres_password", presPass));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_DOWNLOAD_PRESENTATION, params);
		try {
			BufferedHttpEntity buffEnt = new BufferedHttpEntity(resEntity); // Added for file download
			downloadedDps = buffEnt.getContent();
			response = new String(EntityUtils.toString(buffEnt).trim());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (!response.equals(NimpresSettings.API_RESPONSE_NEGATIVE) && response != "") {
			Log.d("APIContact", "api download success");
			return downloadedDps;
		}
		else {
			Log.d("APIContact", "api download failed");
			return null;
		}
	}

	/**
	 * Returns the full URL of the requested api call
	 * 
	 * @param apiCall
	 * @return the full URL of the requested api call
	 */
	public static String getAPIAddress(String apiCall) {
		return NimpresSettings.API_BASE_URL + apiCall + NimpresSettings.API_EXTENSION;
	}

	/**
	 * This method gets the current slide number for the DPS identified by id
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public static int getSlideNumber(int presID, String presPass) {
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("pres_id", String.valueOf(presID)));
		params.add(new BasicNameValuePair("pres_password", presPass));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_PRESENTATION_CURRENT_SLIDE, params);
		try {
			result = EntityUtils.toString(resEntity);

		}
		catch (Exception e) {
			e.printStackTrace();
		}

		if (validateSlideNumResponse(result))
			return Integer.parseInt(result);
		else
			return -1;
	}

	/**
	 * This method lists the presentations in the tables based on a username
	 * 
	 * @param user
	 * @return echos of xml code
	 */
	public static String listPresentations(String userID, String userPass, String userSearch) {
		String result = NimpresSettings.API_RESPONSE_NEGATIVE; // Default to negative
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", userID));
		params.add(new BasicNameValuePair("user_password", userPass));
		params.add(new BasicNameValuePair("user_search", userSearch));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_LIST_PRESENTATIONS, params);
		try {
			result = EntityUtils.toString(resEntity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This method sets the current slide number for the DPS identified by id
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	public static boolean updateSlideNumber(String userID, String userPass, int presID, String presPass, int slide_num) {
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", userID));
		params.add(new BasicNameValuePair("user_password", userPass));
		params.add(new BasicNameValuePair("pres_id", String.valueOf(presID)));
		params.add(new BasicNameValuePair("pres_password", presPass));
		params.add(new BasicNameValuePair("slide_num", String.valueOf(slide_num)));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_PRESENTATION_UPDATE_SLIDE, params);
		try {
			result = EntityUtils.toString(resEntity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (result.equals(NimpresSettings.API_RESPONSE_POSITIVE))
			return true;
		return false;
	}

	/**
	 * This method validates a login/password combination
	 * 
	 * @param user_id
	 * @param user_password
	 * @return true if valid login, false otherwise
	 */
	public static boolean validateLogin(String login, String password) {
		String result = "";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", login));
		params.add(new BasicNameValuePair("user_password", password));
		HttpEntity resEntity = apiPostRequest(NimpresSettings.API_LOGIN, params);
		try {
			result = EntityUtils.toString(resEntity);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (result.equals(NimpresSettings.API_RESPONSE_POSITIVE))
			return true;
		return false;
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private static boolean validateSlideNumResponse(String result) {
		try {
			int response = Integer.parseInt(result);
			if (response >= 0)
				return true;
			else {
				Log.d("APIContact", "slide number response invalid: " + response);
				return false;
			}
		}
		catch (Exception e) {
			Log.d("APIContact", "slide number response invalid: " + e.getMessage());
			return false;
		}
	}
}
