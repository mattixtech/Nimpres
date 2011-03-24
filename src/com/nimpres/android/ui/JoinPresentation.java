package com.nimpres.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.dps.DPS;

public class JoinPresentation extends Activity {
	static TextView updateIDBox = null;
	
	public static void updateID() {	
		if(JoinPresentation.updateIDBox != null && NimpresObjects.presentationID > 0)
			JoinPresentation.updateIDBox.setText(NimpresObjects.presentationID);
	}
	
	private Runnable loadTask = new Runnable() {
		public void run() {
			NimpresObjects.currentDPS = new DPS("api", "internet", NimpresObjects.presentationID, NimpresObjects.presentationPassword, "downloaded", NimpresObjects.ctx);
			NimpresObjects.currentPresentation = NimpresObjects.currentDPS.getDpsPres();
			NimpresObjects.currentPresentation.setPresentationID(NimpresObjects.presentationID);
			NimpresObjects.currentlyViewing = true;
			Intent intent = new Intent(NimpresObjects.ctx,PresentationView.class);
			startActivity(intent);
		}
	};
	
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.join_presentation);
		
		updateIDBox = (TextView) findViewById(R.id.jpID);
		
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
				Intent launchview = new Intent(view.getContext(),
						ListOfPresentations.class);
				startActivity(launchview);
			}
		});
		// setup Join button listener
		//TODO grey out join button until presentation id and/or password is filled in
		Button joinButton = (Button) findViewById(R.id.jpJoin);
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO join presentation code
				//Testing Code
				
				
				
				EditText presenterID = (EditText) findViewById(R.id.jpID);
				EditText presenterPassword = (EditText) findViewById(R.id.jpPassword);
				NimpresObjects.presentationID = Integer.parseInt(presenterID.getText().toString());
				NimpresObjects.presentationPassword = presenterPassword.getText().toString();
				NimpresObjects.updateSource = "internet";	//TODO should check here to see if it should be LAN
				
				setContentView(R.layout.loading);
				ImageView loadingImage = (ImageView) findViewById(R.id.loading);
				loadingImage.setImageResource(R.drawable.loader);
				Thread load = new Thread(loadTask);
				load.start();

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
