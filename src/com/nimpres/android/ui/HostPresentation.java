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
import android.widget.ImageView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.dps.DPSReader;
import com.nimpres.android.lan.LANAdvertiser;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class HostPresentation extends Activity {
	
	
	private Runnable loadTask = new Runnable() {
		public void run() {
			String dpsFileName = "tmp_api-download_downloaded";
			String dpsPath = Utilities.unzip(dpsFileName, "testpres", NimpresObjects.ctx);
			Presentation hostedPresentation = DPSReader.makePresentation(dpsPath);
			
			int presID = 0;
			
			try {
				presID = APIContact.createPresentation(URLEncoder.encode(NimpresObjects.presenterName,"UTF-8"), URLEncoder.encode(NimpresObjects.presenterPassword,"UTF-8"), URLEncoder.encode(hostedPresentation.getTitle(),"UTF-8"), "test", hostedPresentation.getNumSlides(), dpsFileName);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			hostedPresentation.setPresentationID(presID);
			
			Looper.prepare();
			Thread LANAdvert;
			try {
				LANAdvert = new Thread(new LANAdvertiser(hostedPresentation,Utilities.getBroadcastAddress(NimpresObjects.ctx)));
				LANAdvert.start();
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
