package com.nimpres.android.ui;

import java.io.IOException;

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
import com.nimpres.android.dps.DPS;
import com.nimpres.android.dps.DPSReader;
import com.nimpres.android.lan.LANAdvertiser;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class HostPresentation extends Activity {
	
	
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
	
	private Runnable loadTask = new Runnable() {
		public void run() {
			String dpsFileName = "test.dps";
			String dpsPath = Utilities.unzip(dpsFileName, "testpres", NimpresObjects.ctx);
			Presentation hostedPresentation = DPSReader.makePresentation(dpsPath);
			
			int presID = APIContact.createPresentation("test", "test1234", "TestPres", "test", hostedPresentation.getNumSlides(), dpsFileName);
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
			
			
			Intent intent = new Intent(NimpresObjects.ctx,PresentationHost.class);
			startActivity(intent);
		}
	};
}
