import java.io.Serializable;


public class Piece extends Generic implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -431188630422103898L;
	byte[] byte_array;
	int filePiece_index;
	
	public Piece(int message_length, byte message_type, byte[] byte_array, int filePiece_index)
	{
		super(message_length, message_type);
		this.byte_array = byte_array;
		this.filePiece_index = filePiece_index;
		
	}

}
