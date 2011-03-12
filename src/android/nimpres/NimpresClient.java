/**
 * Project:			Nimpres Android Client
 * File name: 		NimpresClient.java
 * Date modified:	2011-03-012
 * Description:		Android entrypoint for Nimpres app
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
package android.nimpres;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
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
import android.widget.ImageView;
import android.widget.TextView;

public class NimpresClient extends Activity {

	DPS testDPS = null;
	Presentation testPres = null;
	Context ctx;
	private Handler mHandler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Use this object if you need to pass Context to something
		ctx = this.getApplicationContext();

		/*
		 * Testing Code Below
		 */

		// Set to main view
		setContentView(R.layout.presentation_viewer);
		// testSlideNum();
		testPresentation();
		/* If testing code please make a method below and call it here */
		// testLoginAPI();
		// testLANAdvertising();
		// testLANListening();
		// testDPSDownload(ctx);
		// testDPSHosting("tmpdps_down.dps", ctx);
		// testLANAdvertising();
		// testLANListening();
		//testDPSDownload(ctx);
		// testDPSHosting("tmpdps_down.dps",ctx);

		// Exit the app after performing test
		// this.finish();

		/*
		 * End of testing code
		 */
	}

	//TODO move the UI methods out of the NimpresClient class
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
						testPres.nextSlide();
						updateSlide();
					} else // swiped down
					{	//change to previous slide
						testPres.previousSlide();
						updateSlide();
					}

					// swiped right
					if (deltaX > 0) {
						// change to next slide
						testPres.nextSlide();
						updateSlide();
					} else { //swiped left
						// change to previous slide
						testPres.previousSlide();
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
			testPres.previousSlide();
			updateSlide();
			return true;
		case R.id.pvmNext:
			testPres.nextSlide();
			updateSlide();
			return true;
		case R.id.pvmLeave:
			testPres.setPaused(true);
			setContentView(R.layout.end_presentation);
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

	/*
	 * Testing methods
	 */

	/**
	 * This task is responsible for updating the image displayed during viewing
	 */
	private Runnable viewerUpdateTask = new Runnable() {
		public void run() {
			if (!testPres.isPaused()) { 		//Check to make sure the user has not paused the presentation
				if(Utilities.isOnline(ctx)){ 	//Check to make sure that the device is connected to the network					
					//TODO change to get the correct slide number for the current presentation rather then hard coded
					int slideNum = APIContact.getSlideNumber("2", "test");
					//Make sure slide was not negative (error code -1)
					if(slideNum >= 0)
						testPres.setCurrentSlide(slideNum); //Update slide number of presentation
				}
				else
					Log.d("NimpresClient","internet connection not present, api contact cancelled");				
				updateSlide();
			}
			mHandler.postDelayed(this, NimpresSettings.API_PULL_DELAY); //Add this task to the queue again, calls itself over and over...
		}
	};

	public void testPresentation() {
		setContentView(R.layout.presentation_viewer);		
		testDPS = new DPS("http://presentations.nimpres.com/presentation_demo.dps", "internet","", "", "dps_down", ctx);
		testPres = testDPS.getDpsPres();
		updateSlide();
		mHandler.removeCallbacks(viewerUpdateTask);
		mHandler.postDelayed(viewerUpdateTask, 100);
	}

	public void updateSlide() {
		ImageView slide = (ImageView) findViewById(R.id.pvSlide);
		TextView title = (TextView) findViewById(R.id.pvTitle);
		TextView slideTitle = (TextView) findViewById(R.id.pvSlideTitle);
		TextView slideNotes = (TextView) findViewById(R.id.pvNotes);
		title.setText(testPres.getTitle());
		slideTitle.setText(testPres.getCurrentSlideFile().getSlideTitle());
		slideNotes.setText(testPres.getCurrentSlideFile().getSlideComments());
		slide.setImageBitmap(BitmapFactory.decodeFile(testPres.getPath()+testPres.getCurrentSlideFile().getFileName()));
	}

	public static void testLoginAPI() {
		// Test login API
		APIContact.validateLogin("Jordan", "testing");

	}

	public static void testSlideNum() {
		int slideNum = APIContact.getSlideNumber("2", "test");
		Log.d("NimpresClient", "slide # " + slideNum);
	}

	public static void testLANAdvertising() {
		Presentation Pres = new Presentation();
		Pres.setTitle("Test");
		Thread LANAdvert = new Thread(new LANAdvertiser(Pres));
		LANAdvert.start();
	}

	public static void testLANListening() {
		Thread LANListen = new Thread(new LANListener());
		LANListen.start();
	}

	public static void testDPSHosting(String fileToServe, Context ctx) {
		Thread dpsServer = new Thread(new DPSServer(fileToServe, ctx));
		dpsServer.start();
	}

	public static void testDPSDownload(Context ctx) {
		DPS lanDPS = new DPS("192.168.1.4", "lan", "123", "pass",
				"testing_dps", ctx);
		// DPS testInternetDPS = new DPS("http://mattixtech.net/filez/test.dps",
		// "internet", "", "", "dps_download", ctx);
		Log.d("NimpresClient", "DPS fully created");
		Log.d("NimpresClient", "DPS presentation title:"
				+ lanDPS.getDpsPres().getTitle());
		Log.d("NimpresClient", "DPS path:" + lanDPS.getDpsPath());
		/*
		 * String folder =
		 * DPSGet.DownloadFromURL("http://mattixtech.net/filez/cars.dps",
		 * "cars.dps", "testing_dps", ctx);
		 * Log.d("NimpresClient","downloaded dps to:"+folder); DPS testDPS = new
		 * DPS(folder); Log.d("NimpresClient","DPS fully created");
		 * Log.d("NimpresClient"
		 * ,"DPS presentation title:"+testDPS.getDpsPres().getTitle());
		 */
	}
}
