package com.nimpres.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nimpres.R;
import com.nimpres.android.NimpresClient;

public class HostEnd extends Activity {
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.end_presentation);

		// setup Yes button listener
		Button YesButton = (Button) findViewById(R.id.epYes);
		YesButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO code to remove presentation from host
				Intent launchview = new Intent(view.getContext(),
						NimpresClient.class);
				startActivity(launchview);
			}
		});
		// setup No button listener
		Button NoButton = (Button) findViewById(R.id.epNo);
		NoButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent launchview = new Intent(view.getContext(),
						NimpresClient.class);
				startActivity(launchview);
			}
		});
	}
}
