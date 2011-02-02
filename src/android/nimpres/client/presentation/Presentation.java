/**
 * Project:			Nimpres Android Client
 * File name: 		Presentation.java
 * Date modified:	2011-02-02
 * Description:		Represents a presentation
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
package android.nimpres.client.presentation;

public class Presentation {
	private String presentationName = "";
	private String owner = "";
	private int timestamp = 0;
	
	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the timestamp
	 */
	public int getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	private int numSlides = 0;
	private int currentSlide = 0;
	private Slide[] slideFiles;
	
	
	/**
	 * @return the presentationName
	 */
	public String getPresentationName() {
		return presentationName;
	}

	/**
	 * @param presentationName the presentationName to set
	 */
	public void setPresentationName(String presentationName) {
		this.presentationName = presentationName;
	}

	/**
	 * @return the numSlides
	 */
	public int getNumSlides() {
		return numSlides;
	}

	/**
	 * @param numSlides the numSlides to set
	 */
	public void setNumSlides(int numSlides) {
		this.numSlides = numSlides;
	}

	/**
	 * @return the currentSlide
	 */
	public int getCurrentSlide() {
		return currentSlide;
	}

	/**
	 * @param currentSlide the currentSlide to set
	 */
	public void setCurrentSlide(int currentSlide) {
		this.currentSlide = currentSlide;
	}

	/**
	 * @return the slideFiles
	 */
	public Slide[] getSlideFiles() {
		return slideFiles;
	}

	/**
	 * @param slideFiles the slideFiles to set
	 */
	public void setSlideFiles(Slide[] slideFiles) {
		this.slideFiles = slideFiles;
	}

	public String getCurrentSlideFile(){
		return slideFiles[currentSlide].getFileName();
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
