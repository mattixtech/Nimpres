/**
 * Project:			Nimpres Android Client
 * File name: 		NimpresObjects.java
 * Date modified:	2011-03-30
 * Description:		Provides a static reference to many of the currently used objectss
 * 
 * License:			Copyright (c) 2010 (Matthew Brooks, Jordan Emmons, William Kong)
					
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

package com.nimpres.android;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;

import com.nimpres.android.dps.DPS;
import com.nimpres.android.presentation.PeerStatus;
import com.nimpres.android.presentation.Presentation;

public class NimpresObjects {
	public static DPS currentDPS;
	public static Presentation currentPresentation;
	public static String hostedPresentationFileName = "";
	public static Context ctx;
	public static Handler messagingThread = new Handler();
	
	public static String loadType = "";
	public static boolean finishedLoading = false;
	
	public static String presentationTitle = "";
	public static int presentationID = 0;
	public static String presentationPassword = "";
	
	public static boolean hostOnInternet = false;
	
	public static boolean currentlyViewing = false;
	public static boolean currentlyPresenting = false;

	public static String presenterName = "test";			//TODO this should be loaded from local config
	public static String presenterPassword = "test1234";  	//TODO this should be loaded from local config
	
	
	public static String updateSource = "";	//This will be either UPDATE_SOURCE_LAN or UPDATE_SOURCE_INTERNET
	public static ArrayList<PeerStatus> peerPresentations = new ArrayList<PeerStatus>(); //LAN peers	
	public static ArrayList<PeerStatus> internetPresentations = new ArrayList<PeerStatus>(); //Internet peers
	
}
