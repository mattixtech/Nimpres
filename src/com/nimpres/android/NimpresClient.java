/**
 * Project:			Nimpres Android Client
 * File name: 		NimpresClient.java
 * Date modified:	2011-03-18
 * Description:		Android entrypoint for Nimpres app
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
package com.nimpres.android;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nimpres.R;
import com.nimpres.android.dps.DPS;
import com.nimpres.android.lan.DPSServer;
import com.nimpres.android.lan.LANAdvertiser;
import com.nimpres.android.lan.LANListener;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.ui.CreateAccount;
import com.nimpres.android.ui.ExistingAccount;
import com.nimpres.android.ui.HostPresentation;
import com.nimpres.android.ui.JoinPresentation;
import com.nimpres.android.ui.LoadingScreen;
import com.nimpres.android.ui.PresentationHost;
import com.nimpres.android.ui.PresentationView;
import com.nimpres.android.ui.Settings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class NimpresClient extends Activity {

	DPS testDPS = null;
	Presentation testPres = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set to main view
		setContentView(R.layout.main); // TODO call the main view from here and
		// have UI elements to access the other views
		
		// Use this object if you need to pass Context to something
		NimpresObjects.ctx = this.getApplicationContext();
		/*
		 * Testing Code Below
		 */

		//setContentView(R.layout.presentation_viewer); //TODO call the main view from here and have UI elements to access the other views
		// testListing();
		// testSlideNum();
		// testUpdateSlide();
		// testPresentation();
		// testPresentation();
		// testLoginAPI();
		// testLANAdvertising();
		// testLANListening();
		// testDPSDownload(ctx);
		// testDPSHosting("tmpdps_down.dps", ctx);
		// testLANAdvertising();
		// testLANListening();
		// testDPSDownload(ctx);
		// testDPSHosting("tmpdps_down.dps",ctx);
		// testCreate();

		/*
		 * End of testing code
		 */
/*
		// setup Create Account button listener
		 Button createAccountButton = (Button) findViewById(R.id.mCreate);
		 createAccountButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent launchview = new Intent(view.getContext(),CreateAccount.class);
				startActivity(launchview);
			}
		});
*/
		// setup Login button listener
		 Button loginButton = (Button) findViewById(R.id.mLogin);
		 loginButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent launchview = new Intent(view.getContext(),ExistingAccount.class);
				startActivity(launchview);
			}
		});
		 
		// setup Join button listener
		 Button joinButton = (Button) findViewById(R.id.mJoin);
		 joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent launchview = new Intent(view.getContext(),JoinPresentation.class); 
				//Intent launchview = new Intent(view.getContext(),PresentationView.class);
				startActivity(launchview);
			}
		});
		 
		// setup Host button listener
		 Button hostButton = (Button) findViewById(R.id.mHost);
		 hostButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent launchview = new Intent(view.getContext(),HostPresentation.class);
				startActivity(launchview);
			}
		});
		 
		// setup Settings button listener
		 Button settingsButton = (Button) findViewById(R.id.mSettings);
		 settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent launchview = new Intent(view.getContext(),Settings.class);
				startActivity(launchview);
			}
		});
	}


	/*
	 * Testing methods
	 */


	public static void testLoginAPI() {
		APIContact.validateLogin("Jordan", "testing");
	}

	public static void testUpdateSlide(){
		APIContact.updateSlideNumber("win", "testing1", 25, "test", 1337);
	}
	
	public static void testSlideNum() {
		int slideNum = APIContact.getSlideNumber(25, "test");
		Log.d("NimpresClient", "slide # " + slideNum);
	}
	
	public static void testListing(){
		APIContact.listPresentations("win", "testing1", "win");
	}

	public static void testLANAdvertising() {
		Presentation Pres = new Presentation();
		Pres.setTitle("Test");
		Pres.setNumSlides(50);
		Pres.setCurrentSlide(5);
		Pres.setPresentationID(25);
		Thread LANAdvert;
		try {
			LANAdvert = new Thread(new LANAdvertiser(Pres,Utilities.getBroadcastAddress(NimpresObjects.ctx)));
			LANAdvert.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static void testLANListening() {
		Thread LANListen = new Thread(new LANListener());
		LANListen.start();
	}

	public static void testDPSHosting(String fileToServe, Context ctx) {
		Thread dpsServer = new Thread(new DPSServer(fileToServe, ctx));
		dpsServer.start();
	}

	public static void testDPSDownload(Context ctx) {
		APIContact.downloadPresentation(26, "test");
		DPS netDPS = new DPS("api","internet",26,"test","test",NimpresObjects.ctx);
		//DPS lanDPS = new DPS("192.168.1.4", "lan", "123", "pass","testing_dps", ctx);
		Log.d("NimpresClient", "DPS fully created");
		Log.d("NimpresClient", "DPS presentation title:"+ netDPS.getDpsPres().getTitle());
	}
	
	public static void testCreate()
	{
		if(APIContact.createPresentation("win", "testing1", "MattTesting", "test", 1, "will.dps") > 0)
			Log.d("NimpresClient","presentation created successfully");
		else
			Log.d("NimpresClient","presentation creation failed");
	}
}
