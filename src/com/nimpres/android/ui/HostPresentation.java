/**
 * Project:			Nimpres Android Client
 * File name: 		
 * Date modified:	2011-04-4
 * Description:		
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
import android.widget.TextView;

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

	static TextView updateFileText = null;

	public static void updateFileName() {
		if (HostPresentation.updateFileText != null) {
			HostPresentation.updateFileText.setText("Current File loaded: " + NimpresObjects.hostedPresentationFileName);
		}
	}

	private Runnable loadTask = new Runnable() {
		public void run() {

			if (NimpresObjects.hostedPresentationFileName.equals(""))
				NimpresObjects.hostedPresentationFileName = NimpresSettings.DEFAULT_DPS_FILE;

			String dpsPath = Utilities.unzip(NimpresObjects.hostedPresentationFileName, "testpres", NimpresObjects.ctx);
			Presentation hostedPresentation = DPSReader.makePresentation(dpsPath);

			String newTitle = NimpresObjects.presentationTitle;
			String newPassword = NimpresObjects.presentationPassword;

			int presID = 0;
			if (NimpresObjects.hostOnInternet) {
				try {
					presID = APIContact.createPresentation(URLEncoder.encode(NimpresObjects.presenterName, "UTF-8"), URLEncoder.encode(NimpresObjects.presenterPassword, "UTF-8"), URLEncoder.encode(newTitle, "UTF-8"), URLEncoder.encode(newPassword, "UTF-8"), hostedPresentation.getNumSlides(), NimpresObjects.hostedPresentationFileName);
				}
				catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				hostedPresentation.setPresentationID(presID);
			}
			else {
				// Set their ID to their current IP Address, should be unique on the network
				String addressID = Utilities.getLocalIpAddress();
				addressID = addressID.substring(addressID.indexOf("."));
				addressID = addressID.replace(".", "");
				addressID = addressID.trim();
				hostedPresentation.setPresentationID(Integer.parseInt(addressID));
			}
			Looper.prepare();

			Thread dpsServer = new Thread(new DPSServer(NimpresObjects.hostedPresentationFileName, NimpresObjects.ctx));
			dpsServer.start(); // Start up the DPS Server

			Thread LANAdvert;
			try {
				LANAdvert = new Thread(new LANAdvertiser(hostedPresentation, Utilities.getBroadcastAddress(NimpresObjects.ctx)));
				LANAdvert.start(); // Start advertising on the LAN
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			NimpresObjects.currentPresentation = hostedPresentation;
			NimpresObjects.currentlyPresenting = true;

			Intent intent = new Intent(NimpresObjects.ctx, PresentationHost.class);
			startActivity(intent);
		}
	};

	public void populateHostedTitleAndPassword() {
		// Find and store the entred title and password
		EditText editTitle = (EditText) findViewById(R.id.hpTitle);
		EditText editPassword = (EditText) findViewById(R.id.hpPassword);

		NimpresObjects.presentationTitle = editTitle.getText().toString();
		NimpresObjects.presentationPassword = editPassword.getText().toString();
	}

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.host_presentation);

		updateFileText = (TextView) findViewById(R.id.hpFileName);

		// setup Host button listener
		Button hostButton = (Button) findViewById(R.id.hpHost);
		hostButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText presentationTitle = (EditText) findViewById(R.id.hpTitle);
				if (presentationTitle.getText().toString().equals("")) {
					TextView hostingError = (TextView) findViewById(R.id.hpNotice);
					hostingError.setText("You must enter a Presentation Title as a minimum");
				}
				else {
					// Find and store the entered title and password
					EditText editTitle = (EditText) findViewById(R.id.hpTitle);
					EditText editPassword = (EditText) findViewById(R.id.hpPassword);

					NimpresObjects.presentationTitle = editTitle.getText().toString();
					if (editPassword.getText().toString().equals(null)) {
						NimpresObjects.presentationPassword = "";
					}
					else {
						NimpresObjects.presentationPassword = editPassword.getText().toString();
						// TODO add password checking method
					}
					populateHostedTitleAndPassword();
					setContentView(R.layout.loading);
					ImageView loadingImage = (ImageView) findViewById(R.id.loading);
					loadingImage.setImageResource(R.drawable.loader);
					Thread load = new Thread(loadTask);
					load.start();
				}
			}
		});

		// setup Host on the Internet + LAN button listener
		Button hostInternetButton = (Button) findViewById(R.id.hpHostInternet);
		hostInternetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText presentationTitle = (EditText) findViewById(R.id.hpTitle);
				if (presentationTitle.getText().toString().equals("")) {
					TextView hostingError = (TextView) findViewById(R.id.hpNotice);
					hostingError.setText("You must enter a Presentation Title as a minimum");
				}
				else {
					// Find and store the entered title and password
					EditText editTitle = (EditText) findViewById(R.id.hpTitle);
					EditText editPassword = (EditText) findViewById(R.id.hpPassword);

					NimpresObjects.presentationTitle = editTitle.getText().toString();

					if (editPassword.getText().toString().equals(null)) {
						NimpresObjects.presentationPassword = "";
					}
					else {
						NimpresObjects.presentationPassword = editPassword.getText().toString();
						// TODO add password checking method
					}
					setContentView(R.layout.loading);
					ImageView loadingImage = (ImageView) findViewById(R.id.loading);
					loadingImage.setImageResource(R.drawable.loader);
					NimpresObjects.hostOnInternet = true;
					Thread load = new Thread(loadTask);
					load.start();
				}
			}
		});
		// setup Choose File button listener
		Button chooseFileButton = (Button) findViewById(R.id.hpChooseFile);
		chooseFileButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO choose file code
				Intent launchview = new Intent(view.getContext(), FileExplorer.class);
				startActivity(launchview);
			}
		});
	}
}
