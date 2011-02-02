package android.nimpres;

import android.app.Activity;
import android.content.Context;
import android.nimpres.client.lan.LANAdvertiser;
import android.nimpres.client.lan.LANListener;
import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.web.APIContact;
import android.os.Bundle;


public class NimpresClient extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context ctx = this.getApplicationContext();
        
        //Set to main view
        setContentView(R.layout.main);        
              
        /*If testing code please make a method below and call it here*/
        testLoginAPI();
        //testLANAdvertising();
        //testLANListening();
        
    }
    
    public static void testLoginAPI(){
    	//Test login API
    	APIContact.validateLogin("Jordan", "testing");
    }
    //Testing the LAN advertising
    public static void testLANAdvertising(){
    	Presentation Pres = new Presentation();
    	Pres.setPresentationName("Test");
        Thread LANAdvert = new Thread(new LANAdvertiser(Pres));
        LANAdvert.start();
    }
    
    //Testing the LAN listening
    public static void testLANListening(){
    	Thread LANListen = new Thread(new LANListener());
    	LANListen.start();
    }
}