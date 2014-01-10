import java.io.Serializable;


public class Handshake implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3413099479808111862L;

	final String handshake_header = "CEN5501C2008SPRING";
	
	final String zero_bits = "0000000000";
	
	int peer_ID;
	
	
	public Handshake(int peer_ID) {
		this.peer_ID = peer_ID;
	}
	
	

}
