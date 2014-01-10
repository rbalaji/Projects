
public class PeerInfo{

	int peer_ID;
	String hostname;
	int socket;
	int hasData;
	int noOfPacketsReceived;
	public PeerInfo(int peer_ID, String hostname, int socket, int hasData){
		this.peer_ID = peer_ID;
		this.hostname = hostname;
		this.socket = socket;
		this.hasData = hasData;
		noOfPacketsReceived = 0;
	}
	public PeerInfo(PeerInfo pi){
		this.peer_ID = pi.peer_ID;
		this.hostname = pi.hostname;
		this.socket = pi.socket;
		this.hasData = pi.hasData;
		this.noOfPacketsReceived = pi.noOfPacketsReceived;
	}
	public void print(){
		System.out.println(peer_ID);
		System.out.println(hostname);
		System.out.println(socket);
		System.out.println(hasData);
	}
	public PeerInfo searchbyID(int peer_ID){
		if(this.peer_ID == peer_ID){
			return this;
			//return new PeerInfo(this);
		}
		return null;
	}
}
