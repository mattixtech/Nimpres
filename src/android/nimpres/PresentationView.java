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

	DPS testDPS = null;
	Presentation testPres = null;

	@Override
	public void onCreate(Bundle created) {
		super.onCreate(created);
		setContentView(R.layout.presentation_viewer);

		// setup button listener

//		Button leaveButton = (Button) findViewById(R.id.pvmLeave);
//		leaveButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				Intent myIntent = new Intent(view.getContext(),NimpresClient.class);
//				startActivityForResult(myIntent, 0);

//			}
//		});
	}

	// TODO move the UI methods out of the NimpresClient class

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pv_menu, menu);
		return true;
	}

	private float initialX = 0;
	private float initialY = 0;
	private float deltaX = 0;
	private float deltaY = 0;

	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { // This avoids
	 * touchscreen events flooding the main thread
	 * 
	 * synchronized (event) { try { // Waits 16ms. event.wait(16);
	 * 
	 * // when user touches the screen if (event.getAction() ==
	 * MotionEvent.ACTION_DOWN) { // reset deltaX and deltaY deltaX = deltaY =
	 * 0;
	 * 
	 * // get initial positions initialX = event.getRawX(); initialY =
	 * event.getRawY(); }
	 * 
	 * // when screen is released if (event.getAction() ==
	 * MotionEvent.ACTION_UP) { deltaX = event.getRawX() - initialX; deltaY =
	 * event.getRawY() - initialY;
	 * 
	 * // swiped up if (deltaY < 0) { // change to next slide
	 * testPres.nextSlide(); updateSlide(); } else // swiped down { // change to
	 * previous slide testPres.previousSlide(); updateSlide(); }
	 * 
	 * // swiped right if (deltaX > 0) { // change to next slide
	 * testPres.nextSlide(); updateSlide(); } else { // swiped left // change to
	 * previous slide testPres.previousSlide(); updateSlide(); }
	 * 
	 * return true; } }
	 * 
	 * catch (InterruptedException e) { return true; } } return true; }
	 */

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.pvmBack:
			testPres.previousSlide();
			updateSlide();
			return true;
		case R.id.pvmNext:
			testPres.nextSlide();
			updateSlide();
			return true;
		case R.id.pvmLeave:
		//	testPres.setPaused(true);
//			setContentView(R.layout.end_presentation);
			leave();
			return true;
		case R.id.pvmPause:
			testPres.setPaused(true);
			return true;
		case R.id.pvmResume:
			testPres.setPaused(false);
			return true;
		case R.id.pvmJump:
			testPres.setPaused(true);
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
		title.setText(testPres.getTitle());
		slideTitle.setText(testPres.getCurrentSlideFile().getSlideTitle());
		slideNotes.setText(testPres.getCurrentSlideFile().getSlideComments());
		slide.setImageBitmap(BitmapFactory.decodeFile(testPres.getPath()
				+ testPres.getCurrentSlideFile().getFileName()));
	}
}
