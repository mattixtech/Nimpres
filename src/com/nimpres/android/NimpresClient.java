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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nimpres.R;
import com.nimpres.android.lan.DPSServer;
import com.nimpres.android.lan.LANAdvertiser;
import com.nimpres.android.lan.LANListener;
import com.nimpres.android.presentation.PeerStatus;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.ui.ExistingAccount;
import com.nimpres.android.ui.HostPresentation;
import com.nimpres.android.ui.JoinPresentation;
import com.nimpres.android.ui.Settings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class NimpresClient extends Activity {

	/*
	 * Testing methods
	 */
	
	public static void testDPSHosting(String fileToServe, Context ctx) {
		Thread dpsServer = new Thread(new DPSServer(fileToServe, ctx));
		dpsServer.start();
	}

	public static void testListing(){
		PeerStatus.getInternetPresentations(NimpresObjects.presenterName, NimpresObjects.presenterPassword, "test");
	}
	
	public static void testLoginAPI() {
		APIContact.validateLogin("Jordan", "testing");
	}
	
	public static void testAdvertising(){
		Presentation pres = new Presentation();
		pres.setTitle("LAN Test Presentation");
		pres.setCurrentSlide(5);
		pres.setPresentationID(500);
		try {
			Thread LANAdvert = new Thread(new LANAdvertiser(pres,Utilities.getBroadcastAddress(NimpresObjects.ctx)));
			LANAdvert.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public static void startup(){
		testAdvertising();
		//Startup the LAN listener thread
		Thread LANListen = new Thread(new LANListener());
		LANListen.start();
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.main); //Set to main view		
		NimpresObjects.ctx = this.getApplicationContext(); //Record the application Context
		startup(); //Perform the startup methods

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
}
