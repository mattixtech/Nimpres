package com.nimpres.android.ui;

import com.nimpres.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class LoadingScreen extends Activity {
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.loading);
		ImageView loadingImage = (ImageView) findViewById(R.id.loading);
		loadingImage.setImageResource(R.drawable.loader);
	}
	
//TODO Code to check if the presentation is ready and switch to the appropriate activity ( host view or client view )

	public void loadClientView(View view1) {
		Intent launchview1 = new Intent(view1.getContext(),PresentationView.class);
		startActivity(launchview1);
	}
	public void loadHostView(View view2) {
		Intent launchview2 = new Intent(view2.getContext(),PresentationHost.class);
		startActivity(launchview2);
	}
}
