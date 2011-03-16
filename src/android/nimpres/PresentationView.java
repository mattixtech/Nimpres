package android.nimpres;

import android.app.Activity;
import android.nimpres.R;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.nimpres.NimpresClient;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.nimpres.client.dps.DPS;
import android.nimpres.client.lan.DPSServer;
import android.nimpres.client.lan.LANAdvertiser;
import android.nimpres.client.lan.LANListener;
import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.settings.NimpresSettings;
import android.nimpres.client.utilities.Utilities;
import android.nimpres.client.web.APIContact;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PresentationView extends Activity {

	static Context ctx;
	private Handler mHandler = new Handler();


	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.presentation_viewer);
		NimpresObjects.currentDPS = new DPS(
				"http://presentations.nimpres.com/presentation_demo.dps",
				"internet", "", "", "dps_down", ctx);
		NimpresObjects.currentPresentation = NimpresObjects.currentDPS.getDpsPres();
		updateSlide();
		mHandler.removeCallbacks(viewerUpdateTask);
		mHandler.postDelayed(viewerUpdateTask, 100);
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

	// TODO move the UI methods out of the NimpresClient class
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pv_menu, menu);
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
						NimpresObjects.currentPresentation.nextSlide();
						updateSlide();
					} else { // swiped left
						// change to previous slide
						NimpresObjects.currentPresentation.previousSlide();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.pvmBack:
			NimpresObjects.currentPresentation.previousSlide();
			updateSlide();
			return true;
		case R.id.pvmNext:
			NimpresObjects.currentPresentation.nextSlide();
			updateSlide();
			return true;
		case R.id.pvmLeave:
			// NimpresObjects.currentPresentation.setPaused(true);
			// setContentView(R.layout.end_presentation);
			leave();
			return true;
		case R.id.pvmPause:
			NimpresObjects.currentPresentation.setPaused(true);
			return true;
		case R.id.pvmResume:
			NimpresObjects.currentPresentation.setPaused(false);
			return true;
		case R.id.pvmJump:
			NimpresObjects.currentPresentation.setPaused(true);
			// prompt for slide number
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
				if (Utilities.isOnline(ctx)) { // Check to make sure that the
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
