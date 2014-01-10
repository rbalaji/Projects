import java.io.Serializable;


public class Have_Request extends Generic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8905523480198922138L;
	public int piece_index_field;
	public int type;
	
	/*
	 * "type" values:
	 * 1 for Have
	 * 2 for Request
	 */
	
	public Have_Request(int message_length,byte message_type, int piece_index_field, int type)
	{
		super(message_length, message_type);
		this.piece_index_field = piece_index_field;
		this.type = type;
	}
}
