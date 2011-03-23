package com.nimpres.android.ui;

import com.nimpres.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

			Intent launchview = new Intent(view.getContext(),PresentationHost.class);
			startActivity(launchview);
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
