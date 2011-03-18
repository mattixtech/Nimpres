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
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.dps.DPS;
import com.nimpres.android.settings.NimpresSettings;
import com.nimpres.android.utilities.Utilities;
import com.nimpres.android.web.APIContact;

public class PresentationView extends Activity {

	private Handler mHandler = new Handler();
	private Menu controlMenu = null;

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.presentation_viewer);
		
		//TODO we should show a loading screen before we do this download and then return to this screen after download is done
		/*NimpresObjects.currentDPS = new DPS(
				"http://presentations.nimpres.com/presentation_demo.dps",
				"internet", "", "", "dps-downloaded-from-internet", NimpresObjects.ctx);*/
		
		
		NimpresObjects.currentDPS = new DPS("api","internet","99","test","downloaded",NimpresObjects.ctx);
		NimpresObjects.currentPresentation = NimpresObjects.currentDPS.getDpsPres();
		NimpresObjects.currentlyViewing = true;
		
		if(NimpresObjects.currentPresentation.getNumSlides()>0)
		{
			mHandler.removeCallbacks(viewerUpdateTask);
			mHandler.postDelayed(viewerUpdateTask, 1);
		}
		
		// setup button listener

		// Button leaveButton = (Button) findViewById(R.id.pvmLeave);
		// leaveButton.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View view) {
		// Intent myIntent = new Intent(view.getContext(),NimpresClient.class);
		// startActivityForResult(myIntent, 0);

		// }
		// });
	}
	
	/**
	 * 
	 */
	public void onConfigurationChanged(){
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pv_menu, menu);
		this.controlMenu = menu;
		//controlMenu.removeItem(R.id.pvmPause);
		resumeUI();
		return true;
	}

	private float initialX = 0;
	private float initialY = 0;
	private float deltaX = 0;
	private float deltaY = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// This avoids touchscreen events flooding the main thread

		synchronized (event) {
			try {
				// Waits 16ms.
				event.wait(16);

				// when user touches the screen
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// reset deltaX and deltaY
					deltaX = deltaY = 0;

					// get initial positions
					initialX = event.getRawX();
					initialY = event.getRawY();
				}

				// when screen is released
				if (event.getAction() == MotionEvent.ACTION_UP) {
					deltaX = event.getRawX() - initialX;
					deltaY = event.getRawY() - initialY;

					// swiped up
					if (deltaY < 0) {
						// change to next slide
						NimpresObjects.currentPresentation.nextSlide();
						updateSlide();
					} else // swiped down
					{ // change to previous slide
						NimpresObjects.currentPresentation.previousSlide();
						updateSlide();
					}

					// swiped right
					if (deltaX > 0) {
						// change to next slide
						NimpresObjects.currentPresentation.previousSlide();
						updateSlide();
					} else { // swiped left
						// change to previous slide
						NimpresObjects.currentPresentation.nextSlide();
						updateSlide();
					}

					return true;
				}
			}

			catch (InterruptedException e) {
				return true;
			}
		}
		return true;
	}
	
	
	public void resumeUI(){	
		//Perform an update request to the api immediatly and then change the menu
		mHandler.removeCallbacks(viewerUpdateTask);
		mHandler.postDelayed(viewerUpdateTask, 1);
		controlMenu.getItem(0).setTitle("Pause");
		controlMenu.getItem(2).setEnabled(false);
		controlMenu.getItem(3).setEnabled(false);
		controlMenu.getItem(4).setEnabled(false);
	}
	
	public void pauseUI(){
		controlMenu.getItem(0).setTitle("Resume");
		controlMenu.getItem(2).setEnabled(true);
		controlMenu.getItem(3).setEnabled(true);
		controlMenu.getItem(4).setEnabled(true);
	}
	
	public void pauseButton(){
		boolean isPaused = NimpresObjects.currentPresentation.isPaused();		
		if(isPaused){
			NimpresObjects.currentPresentation.setPaused(false);
			resumeUI();
		}else{
			NimpresObjects.currentPresentation.setPaused(true);
			pauseUI();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.pvmBack:
			if(NimpresObjects.currentPresentation.isPaused()){
				NimpresObjects.currentPresentation.previousSlide();
				updateSlide();
			}
			return true;			
		case R.id.pvmNext:
			if(NimpresObjects.currentPresentation.isPaused()){
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
		/*case R.id.pvmResume:
			resumeUI();
			NimpresObjects.currentPresentation.setPaused(false);
			return true;*/
		case R.id.pvmJump:
			if(NimpresObjects.currentPresentation.isPaused()){
				NimpresObjects.currentPresentation.setPaused(true);
				//TODO prompt for slide number
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void leave() {
		Intent intent = new Intent();
		setResult(RESULT_OK, intent);
		finish();
	}

	public void updateSlide() {
		ImageView slide = (ImageView) findViewById(R.id.pvSlide);
		TextView title = (TextView) findViewById(R.id.pvTitle);
		TextView slideTitle = (TextView) findViewById(R.id.pvSlideTitle);
		TextView slideNotes = (TextView) findViewById(R.id.pvNotes);
		title.setText(NimpresObjects.currentPresentation.getTitle());
		slideTitle.setText(NimpresObjects.currentPresentation.getCurrentSlideFile().getSlideTitle());
		slideNotes.setText(NimpresObjects.currentPresentation.getCurrentSlideFile().getSlideComments());
		slide.setImageBitmap(BitmapFactory.decodeFile(NimpresObjects.currentPresentation.getPath()
				+ NimpresObjects.currentPresentation.getCurrentSlideFile().getFileName()));
	}



	/**
	 * This task is responsible for updating the image displayed during viewing
	 */
	private Runnable viewerUpdateTask = new Runnable() {
		public void run() {
			if (!NimpresObjects.currentPresentation.isPaused()) { // Check to make sure the user has not
										// paused the presentation
				if (Utilities.isOnline(NimpresObjects.ctx)) { // Check to make sure that the
												// device is connected to the
												// network
					// TODO change to get the correct slide number for the
					// current presentation rather then hard coded
					int slideNum = APIContact.getSlideNumber("2", "test");
					// Make sure slide was not negative (error code -1)
					if (slideNum >= 0)
						NimpresObjects.currentPresentation.setCurrentSlide(slideNum); // Update slide
															// number of
															// presentation
				} else
					Log.d("NimpresClient",
							"internet connection not present, api contact cancelled");
				updateSlide();
			}
			mHandler.postDelayed(this, NimpresSettings.API_PULL_DELAY); // Add
																		// this
																		// task
																		// to
																		// the
																		// queue
																		// again,
																		// calls
																		// itself
																		// over
																		// and
																		// over...
		}
	};
}
