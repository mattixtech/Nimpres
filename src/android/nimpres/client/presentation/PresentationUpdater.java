package android.nimpres.client.presentation;
import java.util.TimerTask;
import android.nimpres.client.web.APIContact;
import android.util.Log;

public class PresentationUpdater extends TimerTask {
	   public void run() {
		   int slideNum = APIContact.getSlideNumber("2", "test");
			Log.d("NimpresClient", "slide # " + slideNum);
	   }
}
