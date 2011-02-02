package android.nimpres;

import android.app.Activity;
import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.wlan.LANAdvertiser;
import android.os.Bundle;


public class NimpresClient extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        testWLAN();
        
        
    }
    
    
    public static void testWLAN(){
    	Presentation Pres = new Presentation();
    	Pres.setPresentationName("Test");
        Thread LANAdvert = new Thread(new LANAdvertiser(Pres));
        LANAdvert.start();
    }
}