import java.io.Serializable;


public class Done implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3053131974682594922L;
	int peer_ID;
	public Done(int peer_ID){
		this.peer_ID = peer_ID;
	}
}
