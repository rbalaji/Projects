import java.io.Serializable;


public class PeerIdentifier implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7108865994969667598L;
	public int peer_ID;
	public PeerIdentifier(int peer_ID){
		this.peer_ID = peer_ID;
	}
}
