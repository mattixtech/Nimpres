package com.nimpres.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.nimpres.R;
import com.nimpres.android.NimpresObjects;
import com.nimpres.android.presentation.Presentation;
import com.nimpres.android.web.APIContact;

public class PresentationHost extends Activity {
	
	Menu controlMenu;
	
	private Presentation hostedPresentation;
	private float initialX = 0;


	private float initialY = 0;

	private float deltaX = 0;

	private float deltaY = 0;
	
	public void onConfigurationChanged(){
	}
	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.presentation);

		// TODO we should show a loading screen before we do this download and
		// then return to this screen after download is done
		NimpresObjects.currentlyPresenting = true;
		hostedPresentation = NimpresObjects.currentPresentation;
		updateSlide();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pvh_menu, menu);
		this.controlMenu = menu;
		// controlMenu.removeItem(R.id.pvmPause);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.pvhmStats:
			// TODO create a method to collect stats
			return true;
		case R.id.pvhmEnd:
			// TODO perform some checks before ending presentation
			Intent launchview = new Intent(this, HostEnd.class);
			startActivity(launchview);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// This avoids touchscreen events flooding the main thread

		synchronized (event) {
			try {
				// Waits 500ms.
				event.wait(1);
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

					
					if (deltaX < 0){
						// Swiped left
						hostedPresentation.nextSlide();
						updateSlide();
					}else if(deltaX > 0){
						// Swiped right
						hostedPresentation.previousSlide();
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

	public void updateSlide() {
		ImageView slide = (ImageView) findViewById(R.id.phvSlide);
		TextView title = (TextView) findViewById(R.id.phvTitle);

		title.setText(hostedPresentation.getTitle());
		slide.setImageBitmap(BitmapFactory
				.decodeFile(hostedPresentation.getPath()
						+ hostedPresentation
								.getCurrentSlideFile().getFileName()));
		
		APIContact.updateSlideNumber(NimpresObjects.presenterName, NimpresObjects.presenterPassword, hostedPresentation.getPresentationID(), "test", hostedPresentation.getCurrentSlide());
	}
}
