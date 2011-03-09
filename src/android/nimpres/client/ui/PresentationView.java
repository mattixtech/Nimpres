package android.nimpres.client.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

public class PresentationView extends View{
	
	

	public PresentationView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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


					// swipped right
					if (deltaX > 0) {
						// change to next slide
//						testPres.nextSlide();
//						updateSlide();
					} else {
						// change to previous slide
//						testPres.previousSlide();
//						updateSlide();
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
	
}
