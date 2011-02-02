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

public class APIContact {

	public static boolean validateLogin(String login, String password){
		try {
	        HttpClient client = new DefaultHttpClient();  
	        String postURL = NimpresSettings.NimpresAPIAddress + NimpresSettings.API_Login_Address;
	        HttpPost post = new HttpPost(postURL); 
	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("login", login));
	            params.add(new BasicNameValuePair("password", password));
	            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
	            post.setEntity(ent);
	            HttpResponse responsePOST = client.execute(post);  
	            HttpEntity resEntity = responsePOST.getEntity();  
	            if (resEntity != null) {    
	                Log.d("APIContact"," API Response: "+EntityUtils.toString(resEntity));
	                //Check here for valid response
	            }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return false;
	}
	public static void pushDPSToWeb(String login, String password, String pesentationTitle, boolean passwordProtect){
		
	}
}
