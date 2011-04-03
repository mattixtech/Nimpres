package com.nimpres.android.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.dps.DPSReader;
import com.nimpres.android.lan.DPSServer;
import com.nimpres.android.lan.LANAdvertiser;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.settings.NimpresSettings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class HostPresentation extends Activity {
	
	
	private Runnable loadTask = new Runnable() {
		public void run() {
			
			if(NimpresObjects.hostedPresentationFileName.equals(""))
				NimpresObjects.hostedPresentationFileName = NimpresSettings.DEFAULT_DPS_FILE;
			
			String dpsPath = Utilities.unzip(NimpresObjects.hostedPresentationFileName, "testpres", NimpresObjects.ctx);
			Presentation hostedPresentation = DPSReader.makePresentation(dpsPath);
			
			EditText editTitle = (EditText) findViewById(R.id.hpTitle);
			EditText editPassword = (EditText) findViewById(R.id.hpPassword);
			
			String newTitle = editTitle.getText().toString();
			String newPassword = editPassword.getText().toString();
			
			int presID = 0;
			
			try {
				presID = APIContact.createPresentation(URLEncoder.encode(NimpresObjects.presenterName,"UTF-8"), URLEncoder.encode(NimpresObjects.presenterPassword,"UTF-8"), URLEncoder.encode(newTitle,"UTF-8"), URLEncoder.encode(newPassword,"UTF-8"), hostedPresentation.getNumSlides(), NimpresObjects.hostedPresentationFileName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			hostedPresentation.setPresentationID(presID);
			
			Looper.prepare();
			
			Thread dpsServer = new Thread(new DPSServer(NimpresObjects.hostedPresentationFileName, NimpresObjects.ctx));
			dpsServer.start(); //Start up the DPS Server
			
			Thread LANAdvert;
			try {
				LANAdvert = new Thread(new LANAdvertiser(hostedPresentation,Utilities.getBroadcastAddress(NimpresObjects.ctx)));
				LANAdvert.start(); //Start advertising on the LAN
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			NimpresObjects.currentPresentation = hostedPresentation;
			NimpresObjects.currentlyPresenting = true;
			
			Intent intent = new Intent(NimpresObjects.ctx,PresentationHost.class);
			startActivity(intent);
		}
	};
	
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.host_presentation);
		
		// setup Host button listener
		 Button hostButton = (Button) findViewById(R.id.hpHost);
		 hostButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			//TODO code to create a presentation, taking title, password, and file	
			//TODO checking algorithms for the password and file chosen
			
				setContentView(R.layout.loading);
				ImageView loadingImage = (ImageView) findViewById(R.id.loading);
				loadingImage.setImageResource(R.drawable.loader);
				Thread load = new Thread(loadTask);
				load.start();
			}
		});
		 
			// setup Back button listener
			Button backButton = (Button) findViewById(R.id.hpBack);
			backButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				}
			});
			// setup Choose File button listener
			Button chooseFileButton = (Button) findViewById(R.id.hpChooseFile);
			chooseFileButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
				//TODO choose file code
					Intent launchview = new Intent(view.getContext(),FileExplorer.class);
					startActivity(launchview);
				}
			});
	}
}
