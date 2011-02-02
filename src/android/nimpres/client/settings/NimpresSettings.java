package android.nimpres.client.settings;

public class NimpresSettings {
	
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
	public static final String PEER_BROADCAST_ADDRESS = "255.255.255.255";
	
	
	/*
     * Messages
     */
	public static final String MSG_PRESENTATION_STATUS = "PS:";
}
