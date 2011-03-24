package com.nimpres.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.nimpres.R;
import com.nimpres.android.NimpresClient;

public class CreateAccount extends Activity {
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.create_account);

		// setup Create button listener
		Button createButton = (Button) findViewById(R.id.caCreate);
		createButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				// TODO code to create an account
				// TODO checking algorithms for the password

				Intent launchview = new Intent(view.getContext(),
						NimpresClient.class);
				startActivity(launchview);
			}
		});

		// setup Back button listener
		Button backButton = (Button) findViewById(R.id.caBack);
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
