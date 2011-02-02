package android.nimpres.client.lan;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.nimpres.client.presentation.Presentation;
import android.nimpres.client.settings.NimpresSettings;
import android.os.SystemClock;
import android.util.Log;

public class LANAdvertiser implements Runnable{

	private Presentation Pres;
	private boolean isStopped = false;
	private DatagramSocket outputSocket;
    private DatagramPacket pkt;
	
    public LANAdvertiser(Presentation Pres){
    	this.Pres = Pres;
    }
    
	public void run(){
		initMessage();
		byte[] buff = new byte[1024];
        buff = (NimpresSettings.MSG_PRESENTATION_STATUS+";"+Pres.getPresentationName()+";"+Pres.getCurrentSlide()).getBytes();
        
        long startTime = SystemClock.uptimeMillis();
        long thisTime = 0;
        
        try{
        	outputSocket = new DatagramSocket();
        	pkt = new DatagramPacket(buff,buff.length,InetAddress.getByName(NimpresSettings.PEER_BROADCAST_ADDRESS),NimpresSettings.SERVER_PEER_PORT);
        	while(!isStopped()){                
                try{
                    Thread.sleep(1);
                }catch(Exception e){}
                
                thisTime = SystemClock.uptimeMillis();
                if((thisTime-startTime) > (1000*NimpresSettings.HELLO_TIMER))
                {
                    outputSocket.send(pkt);
                    Log.d("LANAdvertiser:"," sent presentation status message");
                    /*Loop through current list of all peers to make sure we have heard a HELLO from them recently*/
                    /*for(int i = 0;i<GBManager.numPeers();i++){
                        if((thisTime - GBManager.getPeerTime(GBManager.getPeerByIndex(i))) > 1000*GrabBoxProtocol.DEAD_TIMER){
                            System.out.println("GrabBox removed peer due to timeout: "+GBManager.getPeerByIndex(i));
                            GBManager.removePeer(GBManager.getPeerByIndex(i));                            
                        }              }*/      
                   
                    startTime = SystemClock.uptimeMillis();
                }
            }
        }catch(Exception e){
        	 Log.d("LANAdvertiser:"," Exception: "+e.toString());
        }
	}
	
	public static void initMessage(){
		Log.d("LANAdvertiser:","init");
	}
	
	public boolean isStopped(){
        return isStopped;
    }
	
	public void stop(){
        isStopped = true;
    }
}
