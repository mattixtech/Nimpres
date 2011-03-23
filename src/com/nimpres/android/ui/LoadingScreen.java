package com.nimpres.android.ui;

import java.io.IOException;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.dps.DPS;
import com.nimpres.android.dps.DPSReader;
import com.nimpres.android.lan.LANAdvertiser;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.settings.NimpresSettings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class LoadingScreen extends Activity {
	
	private Handler mHandler = new Handler();
	
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.loading);
		ImageView loadingImage = (ImageView) findViewById(R.id.loading);
		loadingImage.setImageResource(R.drawable.loader);
		
		/*if(NimpresObjects.loadType == "client"){
			NimpresObjects.finishedLoading = false;
			loadClientView();
		}
		else if(NimpresObjects.loadType == "host"){
			NimpresObjects.finishedLoading = false;
			loadHostView();
		}*/				
		
		/*mHandler.removeCallbacks(loadTask);
		mHandler.postDelayed(loadTask, 1);*/
	}
	
	/**
	 * This task is responsible for updating the image displayed during viewing
	 */
	/*private Runnable loadTask = new Runnable() {
		public void run() {
			if(NimpresObjects.finishedLoading){
				if(NimpresObjects.loadType == "client"){
					NimpresObjects.finishedLoading = false;
					loadClientView();
				}
				else if(NimpresObjects.loadType == "host"){
					NimpresObjects.finishedLoading = false;
					loadHostView();
				}					
				finish();
			}
			mHandler.postDelayed(this, 100);
		}
	};*/

	public void loadClientView() {

	}
	public void loadHostView() {

	}
}
