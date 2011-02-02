package android.nimpres.client.presentation;

public class Presentation {
	private String presentationName = "";
	private int numSlides = 0;
	private int currentSlide = 0;
	private String[] slideFiles;
	
	public String getPresentationName() {
		return presentationName;
	}
	
	public void setPresentationName(String name){
		this.presentationName = name;
	}

	public int getCurrentSlide() {
		return currentSlide;
	}
	
	public void setCurrentSlide(int slideNum){
		currentSlide = slideNum;
	}
	
	public String getCurrentSlideFile(){
		return slideFiles[currentSlide];
	}
	
	public void nextSlide(){
		if(currentSlide < (numSlides-1))
			currentSlide++;
	}
	
	public void previousSlide(){
		if(currentSlide>0)
			currentSlide--;
	}

}
