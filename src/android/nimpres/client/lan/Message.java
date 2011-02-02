/**
 * Project:			Nimpres Android Client
 * File name: 		Message.java
 * Date modified:	2011-02-02
 * Description:		Static methods for manipulating TCP messages
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
package android.nimpres.client.lan;

import java.io.DataInputStream;
import java.io.DataOutputStream;
/*This class provides static methods for manipulating messages*/


public class Message {


    public static void sendMessage(DataOutputStream out,String type,byte[] data){
        try{
            int messageLength = type.length() + data.length + "#$".length(); //Record total length of message including type,data,and control characters
            out.writeInt(messageLength); //Send over the length first
            String messageHead = "#"+type+"$";
            out.write(messageHead.getBytes()); //Send the type
            out.write(data); //Send the data
            out.flush();
        }catch(Exception e){
            System.out.println("error"+e.getMessage());
            e.printStackTrace();
        }
    }

    /*Same as above only sending a simple type with blank data*/
    public static void sendMessage(DataOutputStream out,String type){
        try{
            byte[] data = {0};            
            int messageLength = type.length() + data.length + "#$".length(); //Record total length of message including type,data,and control characters
            String messageHead = "#"+type+"$";
            out.writeInt(messageLength); //Send over the length first
            
            out.write(messageHead.getBytes()); //Send the type
            out.write(data); //Send the data
            out.flush();
        }catch(Exception e){
            System.out.println("error"+e.getMessage());
            e.printStackTrace();
        }
    }

    /*Waits for a message and returns it as a byte array*/
    public static byte[] getMessage(DataInputStream in){
        try{
            int length = in.readInt(); //Read the expected length of the messsage
            byte[] buff = new byte[length]; //Create a buffer to store the message
            in.readFully(buff);
            //in.read(buff,0, length); //Read the entire message into the buffer (#<type>$<data>)
            return buff;
        }catch(Exception e){
            System.out.println("error"+e.getMessage());
            e.printStackTrace();            
        }

        byte[] empty = {0};
        return empty; //If there was an error we will return a byte array containing a single 0
    }

    /*Checks specified message for specific type*/
    public static boolean hasType(byte[] message, String type){
        String strMsg = new String(message);
        if(strMsg.indexOf(type)>0)
            return true;
        else
            return false;
    }

    /*Checks message for specific data*/
    public static boolean hasData(byte[] message, byte[] data){
        String strMsg = new String(message);
        int dataStartLoc = strMsg.indexOf("$")+1;

        byte[] dataTest = new byte[message.length - dataStartLoc -1];
        int count=0;

        for(int i=dataStartLoc;i<message.length;i++)
            dataTest[count++] = message[i];

        if(dataTest == data)
            return true;
        else
            return false;
    }

    /*Returns message type as a string*/
    public static String parseType(byte[] message){
        String strMsg = new String(message);

        return strMsg.substring(strMsg.indexOf("#")+1,strMsg.indexOf("$"));
    }

    /*Returns message data as a byte array*/
    public static byte[] parseBinData(byte[] message){
        String strMsg = new String(message);
        int dataLoc = strMsg.indexOf("$")+1;
        String dataPortion = strMsg.substring(dataLoc);

        byte[] data = new byte[dataPortion.length()];
        int count=0;
        for(int i=dataLoc;i<message.length;i++)
            data[count++] = message[i];

        return data;
    }

    /*Returns message data as a string*/
    public static String parseStrData(byte[] message){
        String strMsg = new String(message);
        int dataLoc = strMsg.indexOf("$")+1;
        return strMsg.substring(dataLoc);
    }

    /*Returns message contents as a string*/
    public static String mkString(byte[] message){
        String strMsg = new String(message);
        return strMsg;
    }

}
