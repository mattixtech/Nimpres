/**
 * Project:			Nimpres Android Client
 * File name: 		NimpresClient.java
 * Date modified:	2011-03-06
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

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.nimpres.client.dps.DPS;
import android.nimpres.client.lan.DPSServer;
import android.nimpres.client.lan.LANAdvertiser;
import android.nimpres.client.lan.LANListener;
import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.presentation.PresentationUpdater;
import android.nimpres.client.web.APIContact;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class NimpresClient extends Activity {

	DPS testDPS = null;
	Presentation testPres = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Use this object if you need to pass Context to something
		Context ctx = this.getApplicationContext();

		/*
		 * Testing Code Below
		 */
		TimerTask task  = new PresentationUpdater();
		new Timer().scheduleAtFixedRate(task, 100, 2000);

		// Set to main view
		setContentView(R.layout.main);
		//testSlideNum();
		testPresentation(ctx);
		/* If testing code please make a method below and call it here */
		// testLoginAPI();
		// testLANAdvertising();
		// testLANListening();
		// testDPSDownload(ctx);
		// testDPSHosting("test.dps",ctx);

		// Exit the app after performing test
		// this.finish();

		/*
		 * End of testing code
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.pv_menu, menu);
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
			testPres.nextSlide();
			updateSlide();
			return true;
		case R.id.pvmPause:
			testPres.nextSlide();
			updateSlide();
			return true;
		case R.id.pvmResume:
			testPres.nextSlide();
			updateSlide();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * Testing methods
	 */

	public void testPresentation(Context ctx) {
		setContentView(R.layout.presentation_viewer);
		TextView title = (TextView) findViewById(R.id.pvTitle);

		testDPS = new DPS("http://mattixtech.net/filez/test.dps", "internet",
				"", "", "dps_download", ctx);
		testPres = testDPS.getDpsPres();

		Log.d("NimpresClient", "DPS fully created");
		Log.d("NimpresClient", "Slide is #" + testPres.getCurrentSlide());
		Log.d("NimpresClient", "Path is: " + testPres.getPath()
				+ testPres.getCurrentSlideFile().getFileName());

		title.setText(testPres.getTitle());
		updateSlide();

	}

	public void updateSlide() {
		ImageView slide = (ImageView) findViewById(R.id.pvSlide);
		Log.d("NimpresClient", "Slide is #" + testPres.getCurrentSlide());
		Log.d("NimpresClient", "Path is: " + testPres.getPath()
				+ testPres.getCurrentSlideFile().getFileName());
		slide.setImageBitmap(BitmapFactory.decodeFile(testPres.getPath()
				+ testPres.getCurrentSlideFile().getFileName()));
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
		// DPS testInternetDPS = new
		// DPS("192.168.1.1","lan","","","testing_dps",ctx);
		DPS testInternetDPS = new DPS("http://mattixtech.net/filez/test.dps",
				"internet", "", "", "dps_download", ctx);
		Log.d("NimpresClient", "DPS fully created");
		Log.d("NimpresClient", "DPS presentation title:"
				+ testInternetDPS.getDpsPres().getTitle());
		Log.d("NimpresClient", "DPS path:" + testInternetDPS.getDpsPath());
		/*
		 * String folder =
		 * DPSGet.DownloadFromURL("http://mattixtech.net/filez/test.dps",
		 * "test.dps", "testing_dps", ctx);
		 * Log.d("NimpresClient","downloaded dps to:"+folder); DPS testDPS = new
		 * DPS(folder); Log.d("NimpresClient","DPS fully created");
		 * Log.d("NimpresClient"
		 * ,"DPS presentation title:"+testDPS.getDpsPres().getTitle());
		 */
	}
}