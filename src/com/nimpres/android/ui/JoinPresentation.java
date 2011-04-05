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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.dps.DPS;
import com.nimpres.android.presentation.PeerStatus;
import com.nimpres.android.settings.NimpresSettings;

public class JoinPresentation extends Activity {
	static TextView updateIDBox = null;

	public static void updateID() {
		if (JoinPresentation.updateIDBox != null && NimpresObjects.presentationID > 0)
			JoinPresentation.updateIDBox.setText(String.valueOf(NimpresObjects.presentationID));
	}

	private Runnable loadTask = new Runnable() {
		public void run() {
			if (NimpresObjects.updateSource.equals(NimpresSettings.UPDATE_SOURCE_INTERNET))
				NimpresObjects.currentDPS = new DPS("api", NimpresSettings.UPDATE_SOURCE_INTERNET, NimpresObjects.presentationID, NimpresObjects.presentationPassword, "downloaded", NimpresObjects.ctx);
			else if (NimpresObjects.updateSource.equals(NimpresSettings.UPDATE_SOURCE_LAN))
				NimpresObjects.currentDPS = new DPS(PeerStatus.getLANPresentationByID(NimpresObjects.presentationID).getPeerIP().getHostAddress(), NimpresSettings.UPDATE_SOURCE_LAN, NimpresObjects.presentationID, NimpresObjects.presentationPassword, "downloaded", NimpresObjects.ctx);

			NimpresObjects.currentPresentation = NimpresObjects.currentDPS.getDpsPres();
			NimpresObjects.currentPresentation.setPresentationID(NimpresObjects.presentationID);
			NimpresObjects.currentlyViewing = true;
			Intent intent = new Intent(NimpresObjects.ctx, PresentationView.class);
			startActivity(intent);
		}
	};

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.join_presentation);

		updateIDBox = (TextView) findViewById(R.id.jpID);
		// JoinPresentation.updateIDBox.setText(String.valueOf(NimpresObjects.presentationID));

		// setup Find button listener
		Button findButton = (Button) findViewById(R.id.jpFind);
		findButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO check and join entered presentation ID

				// If entered presentation ID exists, enter the specified
				// presentation
				// TODO need way of entering the presentation for the entered
				// presentation ID
				Intent launchview = new Intent(view.getContext(), ListOfPresentations.class);
				startActivity(launchview);
			}
		});
		// setup Join button listener
		// TODO grey out join button until presentation id and/or password is
		// filled in
		Button joinButton = (Button) findViewById(R.id.jpJoin);
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO join presentation code

				TextView presenterID = (TextView) findViewById(R.id.jpID);
				EditText presenterPassword = (EditText) findViewById(R.id.jpPassword);

				if (!presenterID.getText().toString().equals("")) {

					NimpresObjects.presentationID = Integer.parseInt(presenterID.getText().toString());
					if (presenterPassword.getText().toString().equals(null)) {
						NimpresObjects.presentationPassword = "";
					}
					else {
						NimpresObjects.presentationPassword = presenterPassword.getText().toString();
						// TODO add password checking method
					}

					setContentView(R.layout.loading);
					ImageView loadingImage = (ImageView) findViewById(R.id.loading);
					loadingImage.setImageResource(R.drawable.loader);
					Thread load = new Thread(loadTask);
					load.start();
				}
				else {
					TextView loginError = (TextView) findViewById(R.id.jpNotice);
					loginError.setText("You must enter the presentation ID as a minimum");
				}
			}
		});
		// setup Back button listener
		Button backButton = (Button) findViewById(R.id.jpBack);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
