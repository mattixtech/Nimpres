package android.nimpres.client.presentation;
import java.util.TimerTask;

import android.graphics.BitmapFactory;
import android.nimpres.client.web.APIContact;
import android.util.Log;
import android.widget.ImageView;

public class PresentationUpdater extends TimerTask {
	
	Presentation pres = null;
	ImageView img = null;
	
	public PresentationUpdater(Presentation pres, ImageView img){
		this.pres = pres;
		this.img = img;
	}
	   public void run() {
		   int slideNum = APIContact.getSlideNumber("2", "test");
			Log.d("NimpresClient", "slide # " + slideNum);
			
			if(pres.getCurrentSlide() != slideNum){
				pres.setCurrentSlide(slideNum);
				
				img.post(

					    new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								img.setImageBitmap(BitmapFactory.decodeFile(pres.getPath()+pres.getCurrentSlideFile().getFileName()));
							}
					    	
					        
					    } 
				);				
				
			}
	   }
}
