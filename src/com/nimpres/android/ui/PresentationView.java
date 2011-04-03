/**
 * Project:			Nimpres Android Client
 * File name: 		PresentationView.java
 * Date modified:	2011-03-18
 * Description:		Presentation Viewing UI
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
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.presentation.PeerStatus;
import com.nimpres.android.settings.NimpresSettings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class PresentationView extends Activity {

	private Handler mHandler = new Handler();
	private Menu controlMenu = null;
	private int viewedPresentationID = 0;
	private String viewedPresentationPassword = "";
	/**
	 * This task is responsible for updating the image displayed during viewing
	 */
	private Runnable viewerUpdateTask = new Runnable() {
		public void run() {
			int slideNum = -1;
			Log.d("PresentationView","Update Tick");
			if (!NimpresObjects.currentPresentation.isPaused()) {
				if (Utilities.isOnline(NimpresObjects.ctx)) {
					//TODO we should do something here to make resuming while on LAN quicker, currently we have to wait for a new status message
					if(NimpresObjects.updateSource.equals(NimpresSettings.UPDATE_SOURCE_INTERNET))
						slideNum = APIContact.getSlideNumber(viewedPresentationID, viewedPresentationPassword);						
					else if(NimpresObjects.updateSource.equals(NimpresSettings.UPDATE_SOURCE_LAN))
						slideNum = PeerStatus.getLANPresentationByID(viewedPresentationID).getSlideNumber();
					else{
						slideNum = 0;
						Log.d("NimpresClient","could not determine update source for presentation");
					}
				}else
					Log.d("NimpresClient","internet connection not present, api contact cancelled");
				
				// Make sure slide was not negative (error code -1)
				if (slideNum >= 0)
					NimpresObjects.currentPresentation.setCurrentSlide(slideNum); // Update slide number of presentation
				updateSlide();
			}
			mHandler.postDelayed(this, NimpresSettings.API_PULL_DELAY);
		}
	};

	public void leave() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	/**
	 * 
	 */
	public void onConfigurationChanged(){}

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.presentation_viewer);
		viewedPresentationID = NimpresObjects.presentationID;
		viewedPresentationPassword = NimpresObjects.presentationPassword;		
		
		if (NimpresObjects.currentPresentation.getNumSlides() > 0) {
			mHandler.removeCallbacks(viewerUpdateTask);
			mHandler.postDelayed(viewerUpdateTask, 1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pv_menu, menu);
		this.controlMenu = menu;
		// controlMenu.removeItem(R.id.pvmPause);
		resumeUI();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.pvmBack:
			if (NimpresObjects.currentPresentation.isPaused()) {
				NimpresObjects.currentPresentation.previousSlide();
				updateSlide();
			}
			return true;
		case R.id.pvmNext:
			if (NimpresObjects.currentPresentation.isPaused()) {
				NimpresObjects.currentPresentation.nextSlide();
				updateSlide();
			}
			return true;
		case R.id.pvmLeave:
			NimpresObjects.currentPresentation.setPaused(true);
			// setContentView(R.layout.end_presentation);
			leave();
			return true;
		case R.id.pvmPause:
			pauseButton();
			return true;
			/*
			 * case R.id.pvmResume: resumeUI();
			 * NimpresObjects.currentPresentation.setPaused(false); return true;
			 */
		case R.id.pvmJump:
			if (NimpresObjects.currentPresentation.isPaused()) {
				NimpresObjects.currentPresentation.setPaused(true);
				// TODO prompt for slide number
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void pauseButton() {
		boolean isPaused = NimpresObjects.currentPresentation.isPaused();
		if (isPaused) {
			NimpresObjects.currentPresentation.setPaused(false);
			resumeUI();
		} else {
			NimpresObjects.currentPresentation.setPaused(true);
			pauseUI();
		}
	}

	public void pauseUI() {
		controlMenu.getItem(0).setTitle("Resume");
		controlMenu.getItem(2).setEnabled(true);
		controlMenu.getItem(3).setEnabled(true);
		controlMenu.getItem(4).setEnabled(true);
	}

	public void resumeUI() {
		// Perform an update request to the api immediatly and then change the
		// menu
		mHandler.removeCallbacks(viewerUpdateTask);
		mHandler.postDelayed(viewerUpdateTask, 1);
		controlMenu.getItem(0).setTitle("Pause");
		controlMenu.getItem(2).setEnabled(false);
		controlMenu.getItem(3).setEnabled(false);
		controlMenu.getItem(4).setEnabled(false);
	}

	public void updateSlide() {
		ImageView slide = (ImageView) findViewById(R.id.pvSlide);
		TextView title = (TextView) findViewById(R.id.pvTitle);
		TextView slideTitle = (TextView) findViewById(R.id.pvSlideTitle);
		TextView slideNotes = (TextView) findViewById(R.id.pvNotes);
		title.setText(NimpresObjects.currentPresentation.getTitle());
		slideTitle.setText(NimpresObjects.currentPresentation
				.getCurrentSlideFile().getSlideTitle());
		slideNotes.setText(NimpresObjects.currentPresentation
				.getCurrentSlideFile().getSlideComments());
		slide.setImageBitmap(BitmapFactory
				.decodeFile(NimpresObjects.currentPresentation.getPath()
						+ NimpresObjects.currentPresentation
								.getCurrentSlideFile().getFileName()));
	}
}
