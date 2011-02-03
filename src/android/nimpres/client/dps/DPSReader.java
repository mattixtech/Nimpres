/**
 * Project:			Nimpres Android Client
 * File name: 		DPSReader.java
 * Date modified:	2011-02-02
 * Description:		Reads the dps xml metafile to create a presentation
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
package android.nimpres.client.dps;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.presentation.Slide;
import android.nimpres.client.settings.NimpresSettings;
import android.util.Log;

/*This class offers methods to read a DSP file once it has been extracted*/

public class DPSReader {

	/**
	 * 
	 * @param dpsPath
	 * @return
	 */
	public static Presentation makePresentation(String dpsPath){
		Presentation presCreated = null;
		File metaFile = new File(dpsPath +File.separator+NimpresSettings.METAFILE_NAME);
		if(metaFile.exists()){
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			
			try{
				Log.d("DPSReader","parsing metafile");
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(metaFile);
				doc.getDocumentElement().normalize();
				Node presRootNode = doc.getElementsByTagName("presentation").item(0);
				Element presElement = (Element) presRootNode;
				int numberSlides = Integer.parseInt(presElement.getElementsByTagName("number_slides").item(0).getTextContent());
					Log.d("DPSReader","found number_slides:"+numberSlides);
				String title = presElement.getElementsByTagName("presentation_title").item(0).getTextContent();
					Log.d("DPSReader","found presentation_title:"+title);
				String owner = presElement.getElementsByTagName("presentation_owner").item(0).getTextContent();
					Log.d("DPSReader","found presentation_owner:"+owner);
				int timestamp = Integer.parseInt(presElement.getElementsByTagName("presentation_timestamp").item(0).getTextContent());
					Log.d("DPSReader","found presentation_timestamp:"+timestamp);
				
				presCreated = new Presentation();
				presCreated.setCurrentSlide(0);
				presCreated.setNumSlides(numberSlides);
				presCreated.setTitle(title);
				presCreated.setTimestamp(timestamp);
				presCreated.setOwner(owner);
				
				Slide[] slides = new Slide[numberSlides];
				
				NodeList slideElements = presElement.getElementsByTagName("slide");
				
				for(int i=0;i<slideElements.getLength();i++){
					Element thisSlide = (Element) slideElements.item(i);
					int thisSlideNumber = Integer.parseInt(thisSlide.getAttribute("number"));
					Log.d("DPSReader","found slide #:"+thisSlideNumber);
					String thisSlideTitle = thisSlide.getElementsByTagName("title").item(0).getTextContent();
					Log.d("DPSReader","found slide title:"+thisSlideTitle);
					String thisSlideFile = thisSlide.getElementsByTagName("file").item(0).getTextContent();
					Log.d("DPSReader","found slide file:"+thisSlideFile);
					String thisSlideNotes = thisSlide.getElementsByTagName("notes").item(0).getTextContent();
					Log.d("DPSReader","found slide notes:"+thisSlideNotes);
					
					slides[thisSlideNumber-1] = new Slide(thisSlideFile,thisSlideNumber,thisSlideNotes,thisSlideTitle);
				}
				
				presCreated.setSlideFiles(slides);				
			}catch(Exception e){
				Log.d("DPSReader","Exception:"+e);
			}
			
			return presCreated;
		}else{
			//File did not exist
			Log.d("DPSReader","file not found:"+metaFile.getPath());
			return null;
		}
	}
}
