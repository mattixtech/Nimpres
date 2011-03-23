package com.nimpres.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;

public class JoinPresentation extends Activity {
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.join_presentation);

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
						PresentationView.class);
				startActivity(launchview);
			}
		});
		// setup Join button listener
		Button joinButton = (Button) findViewById(R.id.jpJoin);
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO join presentation code
				//Testing Code
				EditText presenterID = (EditText) findViewById(R.id.jpID);
				NimpresObjects.presentationID = Integer.parseInt(presenterID.getText().toString());
				NimpresObjects.presentationPassword = "test";
				NimpresObjects.updateSource = "internet";
				
				
				Intent launchview = new Intent(view.getContext(),
						PresentationView.class); //TODO change this to loading screen
				startActivity(launchview);
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
