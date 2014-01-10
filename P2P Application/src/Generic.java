import java.io.Serializable;


public class Generic implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5075847218484325042L;
	public int message_length;
	public byte message_type;
	
	public Generic(int message_length, byte message_type)
	{
		this.message_length = message_length;
		this.message_type = message_type;
	}

}
