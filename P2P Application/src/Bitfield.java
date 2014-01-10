import java.io.Serializable;
import java.util.BitSet;


public class Bitfield extends Generic implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2532165147177297473L;
	/**
	 * 
	 */
	public BitSet bitfield;
	Bitfield(int message_length, byte message_type, BitSet bitfield){
		super(message_length, message_type);
		this.bitfield = bitfield;
	}
}
