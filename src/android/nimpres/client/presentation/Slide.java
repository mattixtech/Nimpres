/**
 * Project:			Nimpres Android Client
 * File name: 		Slide.java
 * Date modified:	2011-02-02
 * Description:		Represents a slide in a presentation
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

public class Slide {
	private String fileName = "";
	private int slideNumber = 0;
	private String slideComments = "";
	private String slideTitle = "";
	
	
	public Slide(String fileName, int slideNumber, String slideComments, String slideTitle){
		this.fileName = fileName;
		this.slideNumber = slideNumber;
		this.slideComments = slideComments;
		this.slideTitle = slideTitle;
	}


	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}


	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	/**
	 * @return the slideNumber
	 */
	public int getSlideNumber() {
		return slideNumber;
	}


	/**
	 * @param slideNumber the slideNumber to set
	 */
	public void setSlideNumber(int slideNumber) {
		this.slideNumber = slideNumber;
	}


	/**
	 * @return the slideComments
	 */
	public String getSlideComments() {
		return slideComments;
	}


	/**
	 * @param slideComments the slideComments to set
	 */
	public void setSlideComments(String slideComments) {
		this.slideComments = slideComments;
	}


	/**
	 * @return the slideTitle
	 */
	public String getSlideTitle() {
		return slideTitle;
	}


	/**
	 * @param slideTitle the slideTitle to set
	 */
	public void setSlideTitle(String slideTitle) {
		this.slideTitle = slideTitle;
	}
}
