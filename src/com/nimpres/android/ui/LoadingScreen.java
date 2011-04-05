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
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.nimpres.R;

public class LoadingScreen extends Activity {

	private Handler mHandler = new Handler();

	/**
	 * This task is responsible for updating the image displayed during viewing
	 */
	/*
	 * private Runnable loadTask = new Runnable() { public void run() { if(NimpresObjects.finishedLoading){ if(NimpresObjects.loadType == "client"){ NimpresObjects.finishedLoading = false; loadClientView(); } else if(NimpresObjects.loadType == "host"){ NimpresObjects.finishedLoading = false; loadHostView(); } finish(); } mHandler.postDelayed(this, 100); } };
	 */

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

		/*
		 * if(NimpresObjects.loadType == "client"){ NimpresObjects.finishedLoading = false; loadClientView(); } else if(NimpresObjects.loadType == "host"){ NimpresObjects.finishedLoading = false; loadHostView(); }
		 */

		/*
		 * mHandler.removeCallbacks(loadTask); mHandler.postDelayed(loadTask, 1);
		 */
	}
}
