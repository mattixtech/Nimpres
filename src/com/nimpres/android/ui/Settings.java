package com.nimpres.android.ui;

import com.nimpres.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Settings extends Activity {

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.settings);

		// setup ForgetID button listener
		Button settingsButton = (Button) findViewById(R.id.sForget);
		settingsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO code for forgetting ID
			}
		});

		// setup Settings button listener
		Button backButton = (Button) findViewById(R.id.sBack);
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
