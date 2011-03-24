package com.nimpres.android.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.nimpres.R;

public class LoadingScreen extends Activity {
	
	private Handler mHandler = new Handler();
	
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
}
