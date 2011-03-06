/**
 * Project:			Nimpres Android Client
 * File name: 		NimpresSettings.java
 * Date modified:	2011-03-06
 * Description:		Static settings shared throughout app
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
package android.nimpres.client.settings;

public class NimpresSettings {
	
	/*
	 * API Addresses
	 */
	
	public static final String API_BASE_URL = "http://droidshare.testing.mattixtech.net/api/";
	public static final String API_LOGIN = "login";
	public static final String API_SLIDE = "get_slide_num";
	public static final String API_CREATE_ACCOUNT = "create_account";
	public static final String API_CREATE_PRESENTATION = "create_presentation";
	public static final String API_UPDATE_PRESENTATION = "update_presentation";
	public static final String API_DELETE_PRESENTATION = "delete_presentation";
	public static final String API_LIST_PRESENTATIONs = "list_presentations";
	
	/*
	 * API Message
	 */
	
	public static final String API_RESPONSE_POSITIVE = "OK";
	public static final String API_RESPONSE_NEGATIVE = "FAIL";
	
	/*
     * Timers:
     */
    public static final double HELLO_TIMER = 1; //Send hello every X seconds
    public static final double DEAD_TIMER = 5; //Wait X seconds before removing peer
	
	/*
     * Ports
     */
    public static final int SERVER_FILE_PORT = 3333;
    public static final int SERVER_PEER_PORT = 2222;
	
    
    /*
     * Settings
     */
    public static final String METAFILE_NAME = "meta-inf.xml";
	public static final String PEER_BROADCAST_ADDRESS = "255.255.255.255";
	public static final int SERVER_QUE_SIZE = 32;
	
	/*
     * Messages
     */
	
	/*
	 * UDP
	 */
	public static final String MSG_PRESENTATION_STATUS = "PS";
	
	/*
	 * TCP
	 */
	public static final String MSG_REQUEST_FILE_TRANSFER = "REQ";
	public static final String MSG_RESPONSE_FILE_TRANSFER = "RESP";
	public static final String MSG_RESPONSE_INVALID_REQ = "INV";
}
