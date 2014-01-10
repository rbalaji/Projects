import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


class msg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8414368057492807162L;
	int a;
	String name;
}


public class PeerProgram {

	public static int papaBear = 0;
	public static int Neighbors[];
	public static BitSet bitset = new BitSet();
	public static int mypeer_ID = 1001;
	public static Bitfield bf;
	public static ArrayList<PeerInfo> Peers = new ArrayList<PeerInfo>();
	public static ArrayList<PeerInfo> Peers2 = new ArrayList<PeerInfo>();
	public static ArrayList<PeerInfo> otherPeers = new ArrayList<PeerInfo>();
    public static ArrayList<Integer> zeroIndices = new ArrayList<Integer>();
	public static int noOfPeers=0;
	public static int numberOfPreferredNeighbors;
	public static int unchokingInterval;
	public static int optimisticUnchokingInterval;
	public static String main_fileName;
	public static int fileSize;// = 3195696;
	public static int pieceSize;// = 32768;
	public static RandomAccessFile raf = null;
	public static byte[][] newBytes;
	public static byte[][] newBytes2;
	public static int noOfBits;
	public static Hashtable<Integer, BitSet> neighborBitSets = new Hashtable<Integer, BitSet>();
	public static Hashtable<Integer, Integer> requestedPieces = new Hashtable<Integer, Integer>();
	public static Hashtable<Integer, Controlclient> clientConnections = new Hashtable<Integer, Controlclient>();
	public static Hashtable<Integer, Integer> finishers = new Hashtable<Integer, Integer>();
	
	public static void sendPacket(PeerInfo peerinfo, String msgtype, ObjectOutputStream out){
		try {
			if(msgtype.equals("Choke")){
				String ip = peerinfo.hostname;
				int port = peerinfo.socket;
				//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				Generic chokeMsg = new Generic(1, (byte)0);
				out.writeObject(chokeMsg);
			}
			else if(msgtype.equals("Unchoke")){
				String ip = peerinfo.hostname;
				int port = peerinfo.socket;
				//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				Generic chokeMsg = new Generic(1, (byte)1);
				out.writeObject(chokeMsg);
			}
			else if(msgtype.equals("Interested")){
				String ip = peerinfo.hostname;
				int port = peerinfo.socket;
				//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				Generic chokeMsg = new Generic(1, (byte)2);
				out.writeObject(chokeMsg);
			}
			else if(msgtype.equals("Uninterested")){
				String ip = peerinfo.hostname;
				int port = peerinfo.socket;
				//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				Generic chokeMsg = new Generic(1, (byte)3);
				out.writeObject(chokeMsg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public static void createOtherPeers(){
		Peers2 = (ArrayList<PeerInfo>) Peers.clone();
		Iterator<PeerInfo> it;
		it = Peers2.iterator();
		PeerInfo pi;
		pi = Peers.get(0);
		while(it.hasNext()){
			pi = it.next();
			if(pi.peer_ID == mypeer_ID){
				break;
			}
		}
		Peers2.remove(Peers2.indexOf(pi));
		otherPeers = Peers2;
	}
	
	public static void initialNeighbors(){
/*		Random randomGen = new Random();
		int temp = -1;
		//Iterator<PeerInfo> it = Peers.iterator();
		for(int i = 0; i<=numberOfPreferredNeighbors; i++){
			while(temp!=peer_ID){
				int index = randomGen.nextInt(Peers.size());
				PeerInfo pi = Peers.get(index);
				temp = pi.peer_ID;
			}
			for(int )
		}
*/	
		Neighbors = new int[numberOfPreferredNeighbors+1];
		Peers2 = (ArrayList<PeerInfo>) Peers.clone();
		Iterator<PeerInfo> it;
		it = Peers2.iterator();
		PeerInfo pi;
		pi = Peers.get(0);
		while(it.hasNext()){
			pi = it.next();
			if(pi.peer_ID == mypeer_ID){
				break;
			}
		}
		Peers2.remove(Peers2.indexOf(pi));
		otherPeers = Peers2;
		Collections.shuffle(Peers2);
		//System.out.println(numberOfPreferredNeighbors);
		for(int i = 0; i<=numberOfPreferredNeighbors; i++){
			Neighbors[i] = Peers2.get(i).peer_ID;
			System.out.println("My neighbor: "+Neighbors[i]);
			
			
			try {
				Socket client = new Socket(Peers2.get(i).hostname, Peers2.get(i).socket);
				ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
				PeerIdentifier peerIdent = new PeerIdentifier(mypeer_ID);
				out.writeObject(peerIdent);
				System.out.println("Sending unchoke to "+Peers2.get(i));
				sendPacket(Peers2.get(i), "Unchoke", out);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
	}
	public static void readPeerInfofile(){
		try {
			String peerinfofilename;
			peerinfofilename = "PeerInfo.cfg";
			BufferedReader peerInfo;
			peerInfo = new BufferedReader(new FileReader(peerinfofilename));
			String line;
			String[] line_split;
			
			while((line = peerInfo.readLine())!=null){
			
				line_split = line.split(" ");
				//if(line.l)
				if(line.length()>0){
					PeerInfo temp = new PeerInfo(Integer.parseInt(line_split[0]), line_split[1], Integer.parseInt(line_split[2]), Integer.parseInt(line_split[3]));
					System.out.println(line_split[0]+" "+line_split[1]);
					Peers.add(temp);
				noOfPeers++;
				}
			}
			peerInfo.close();
			System.out.println(noOfPeers+" "+Peers.size());
		} catch (Exception e) {
			//e.printStackTrace();
			// TODO: handle exception
		}
	}
	public static void readCommonfile()
	{
		try
		{
			String peerCommonFileName;
			peerCommonFileName = "Common.cfg";
			FileReader peerCommon;
			peerCommon = new FileReader(peerCommonFileName);
			BufferedReader peerCommonFile;
			peerCommonFile = new BufferedReader(peerCommon);
			
			String line;
			String[] line_split;
			
			while((line = peerCommonFile.readLine())!=null){
			
				line_split = line.split(" ");
				
				if(line_split[0].equalsIgnoreCase("NumberOfPreferredNeighbors"))
				{
					numberOfPreferredNeighbors = Integer.parseInt(line_split[1]);
				}
				else if(line_split[0].equalsIgnoreCase("UnchokingInterval"))
				{
					unchokingInterval = Integer.parseInt(line_split[1]);
				}
				else if(line_split[0].equalsIgnoreCase("OptimisticUnchokingInterval"))
				{
					optimisticUnchokingInterval = Integer.parseInt(line_split[1]);
				}
				else if(line_split[0].equalsIgnoreCase("FileName"))
				{
					main_fileName = line_split[1];
				}
				else if(line_split[0].equalsIgnoreCase("FileSize"))
				{
					fileSize = Integer.parseInt(line_split[1]);
				}
				else if(line_split[0].equalsIgnoreCase("PieceSize"))
				{
					pieceSize = Integer.parseInt(line_split[1]);
				}
				
			}
			noOfBits  = (fileSize%pieceSize==0)?(fileSize/pieceSize):(fileSize/pieceSize+1);
			System.out.println("Read common file:");
			System.out.println(numberOfPreferredNeighbors);
			System.out.println(unchokingInterval);
			System.out.println(optimisticUnchokingInterval);
			System.out.println("Main file name: "+main_fileName);
			System.out.println(fileSize);
			System.out.println(pieceSize);
			System.out.println("No of pieces: "+noOfBits);
			System.out.println((fileSize%pieceSize==0)?(fileSize/pieceSize):(fileSize/pieceSize+1));
			System.out.println("Done reading.");
		}
		catch(FileNotFoundException e )
		{
			
		}
		catch(IOException e )
		{
			
		}
	}
	
	public static void create_field(){
		newBytes = new byte[noOfBits][(int) pieceSize];
		if(fileSize%pieceSize!=0){
			newBytes[noOfBits-1] = new byte[fileSize%pieceSize];
		}
	}

	public static void create_chunks(){
		int i;
		try {
			newBytes = new byte[noOfBits][(int) pieceSize];
			if(fileSize%pieceSize!=0){
				newBytes[noOfBits-1] = new byte[fileSize%pieceSize];
			}
			raf = new RandomAccessFile("peer_1000/"+main_fileName, "rw");
			//RandomAccessFile newChunk[] = new RandomAccessFile[noOfBits];
			
			for(i =0; i < noOfBits-1; i++)
			{
				
				raf.seek(i*pieceSize);
				raf.read(newBytes[i], 0, (int) pieceSize);
				
				RandomAccessFile newChunk = new RandomAccessFile(i+main_fileName , "rw");
				System.out.println("created chunk: " + i);
				newChunk.seek(0);
				newChunk.write(newBytes[i]);
				newChunk.close();
			}
			if(fileSize%pieceSize!=0){
				raf.seek(i*pieceSize);
				//byte[] newBytes2 = new byte[(int) fileSize%pieceSize];
				raf.read(newBytes[i], 0, (int) fileSize%pieceSize);
				System.out.println("created chunk: " + i);
			}
			else{
				raf.seek(i*pieceSize);
				//byte[] newBytes2 = new byte[(int) fileSize%pieceSize];
				raf.read(newBytes[i], 0, (int) pieceSize);
				System.out.println("created chunk: " + i);
			}
			raf.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	public static class PIComparator implements Comparator<PeerInfo>{

		@Override
		public int compare(PeerInfo arg0, PeerInfo arg1) {
			// TODO Auto-generated method stub
			if(arg0.noOfPacketsReceived>arg1.noOfPacketsReceived){
				return 1;
			}
			else if(arg0.noOfPacketsReceived==arg1.noOfPacketsReceived){
				return 0;
			}
			return -1;
		}
		
	}
	public static class NeighborUpdate {

		Timer timer;
		public NeighborUpdate(int seconds, boolean flag){
			timer = new Timer();
			timer.schedule(new RemindTask(seconds, flag), seconds*1000, seconds*1000);
		}
		public class RemindTask extends TimerTask{
			int seconds;
			boolean flag;
			int neighborFlag = 0;
	    	public RemindTask(int seconds, boolean flag)
	    	{
	    		this.seconds = seconds;
	    		this.flag = flag;
	    	}
			public void run(){
				int allOK = 0;
				System.out.println("My peer id: "+mypeer_ID);
				System.out.println("I have "+bitset.cardinality());
				if(bitset.cardinality()>=noOfBits){
					finishers.put(mypeer_ID, mypeer_ID);
					System.out.println("I have been added.");
				}
/*				System.out.println(neighborBitSets.size());
				Enumeration<Integer> nb = neighborBitSets.keys();
				while(nb.hasMoreElements()){
					int npeer = nb.nextElement();
					BitSet nBitset = neighborBitSets.get(npeer);
					System.out.print(nBitset.cardinality());
					for(int s = 0; s < noOfBits; s++){
						if(nBitset.get(s) == false){
							allOK = 1;
						}
					}
				}
				System.out.println();
				if(allOK == 0 && neighborBitSets.size()>=noOfPeers){
					System.out.println("All received all.");
					System.exit(0);
				}
*/
				System.out.println("Finished: "+finishers.size());
				if(finishers.size()>=Peers.size()-1){

									try{
                                                                                Iterator<PeerInfo> iterateF = Peers2.iterator();
                                                                                while(iterateF.hasNext()){
                                                                                        PeerInfo tempInfo = iterateF.next();
                                                                                        Socket tempConnection = new Socket(tempInfo.hostname, tempInfo.socket);
                                                                                        //PeerIdentifier piden = new PeerIdentifier(mypeer_ID);
                                                                                        ObjectOutputStream tempout = new ObjectOutputStream(tempConnection.getOutputStream());
                                                                                        //tempout.writeObject(piden);
                                                                                        //Have_Request hr = new Have_Request(5, (byte)4, receivedPiece.filePiece_index, 1);
                                                                                        Done d = new Done(mypeer_ID);
                                                                                        tempout.writeObject(d);
                                                                                        tempout.writeObject(d);
                                                                                        tempout.writeObject(d);
                                                                                        tempout.writeObject(d);
                                                                                        //tempout.writeObject(piden);
                                                                                        //tempout.writeObject(hr);
                                                                                        tempConnection.close();
                                                                                }
									}
									catch(Exception e){
									}


					System.out.println("All done.");
					System.exit(0);
				}
				else{
					Enumeration enumer = finishers.keys();
					while(enumer.hasMoreElements()){
						System.out.println("Finished peers: "+enumer.nextElement());
					}
				}
				int oldNeighbors[];
				oldNeighbors= new int[numberOfPreferredNeighbors+1];
				System.arraycopy(Neighbors, 0, oldNeighbors, 0, numberOfPreferredNeighbors+1);
				Collections.sort(otherPeers, new PIComparator());
				Iterator<PeerInfo> it;
				it = otherPeers.iterator();
				while(it.hasNext()){
					PeerInfo pi = it.next();
					System.out.println(pi.peer_ID);
				}
				int j = 0;
				for(int i = otherPeers.size()-1; i>=otherPeers.size()-numberOfPreferredNeighbors; i--){
					Neighbors[j] = otherPeers.get(i).peer_ID;
					j++;
				}
				Random randGen;
				randGen = new Random();
				int newRange;
				newRange = otherPeers.size()-numberOfPreferredNeighbors-1;
				int rand;
				rand = randGen.nextInt(1);//newRange);
				PeerInfo pi;
				pi = otherPeers.get(rand);
				Neighbors[j] = pi.peer_ID;
				otherPeers = Peers2;
				Hashtable<Integer, Integer> chokeUnchokeList = new Hashtable<Integer, Integer>();
				
				/*
				 * Old neighbors that are not in new neighbors, put in +1
				 * New neighbors that are not in old neighbors, put in -1
				 */
				for(int i = 0; i <= numberOfPreferredNeighbors; i++){
					if(chokeUnchokeList.containsKey(oldNeighbors[i])){
						int count = chokeUnchokeList.get(oldNeighbors[i]);
						chokeUnchokeList.put(oldNeighbors[i], count+1);
					}
					else{
						chokeUnchokeList.put(oldNeighbors[i], 1);
					}
					if(chokeUnchokeList.containsKey(Neighbors[i])){
						int count = chokeUnchokeList.get(Neighbors[i]);
						chokeUnchokeList.put(Neighbors[i], count-1);
					}
					else{
						chokeUnchokeList.put(Neighbors[i], -1);
					}
				}
				Enumeration<Integer> iterator = chokeUnchokeList.keys();
				while(iterator.hasMoreElements()){
					int temp = iterator.nextElement();
					int count = chokeUnchokeList.get(temp);
					/*
					 * If count is +1 (>0), we have to send choke.
					 * If count is -1 (<0), we have to send unchoke. 
					 */
					if(count>0){
						Iterator<PeerInfo> iter = otherPeers.iterator();
						while(iter.hasNext()){
							PeerInfo peerinfo = iter.next();
							if(peerinfo.peer_ID == temp){
								try {
									Socket client = new Socket(peerinfo.hostname, peerinfo.socket);
									ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
									PeerIdentifier peerIdent = new PeerIdentifier(mypeer_ID);
									out.writeObject(peerIdent);
									sendPacket(peerinfo, "Choke", out);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
								//Socket client = new Socket(ipAddress, targetPort);
								//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());								
							}
						}
					}
					else if(count<0){
						Iterator<PeerInfo> iter = otherPeers.iterator();
						while(iter.hasNext()){
							PeerInfo peerinfo = iter.next();
							if(peerinfo.peer_ID == temp){
								try {
									Socket client = new Socket(peerinfo.hostname, peerinfo.socket);
									ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
									PeerIdentifier peerIdent = new PeerIdentifier(mypeer_ID);
									out.writeObject(peerIdent);
									sendPacket(peerinfo, "Unchoke", out);
								} catch (UnknownHostException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
								}
								//Socket client = new Socket(ipAddress, targetPort);
								//ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());								
							}
						}
					}
				}
			}
		}
	}
	
	public static class eachConnection extends Thread{
		int peer_ID;
		int portNumber;
		int neighborFlag = 0;
		Socket connectionSocket;
		public eachConnection(Socket connectionSocket, int portNumber, int peer_ID){
			this.connectionSocket = connectionSocket;
			this.portNumber = portNumber;
			this.peer_ID = peer_ID;
		}
		
		@Override
		public void run(){
			
			 try {
				while(true) 
				 {
					 int i;
				    System.out.println(connectionSocket.getInetAddress());
				    //BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				    ObjectInputStream ois;
				    ois = new ObjectInputStream(connectionSocket.getInputStream());
				    ObjectOutputStream oos;
				    oos = new ObjectOutputStream(connectionSocket.getOutputStream());
				    Object o;
				    int currentNeighbor = 0;
				    
				    while(true){
				    
				    o = ois.readObject();
				    if(o instanceof Handshake){
				    	Handshake h2;
				    	h2 = (Handshake)o;
				    	currentNeighbor = h2.peer_ID;
				    	System.out.println("Handshake received from "+h2.peer_ID+" by "+peer_ID);
				    	/*
				    	 * Preferred neighbor check
				    	 */
				    	for(i = 0; i<=numberOfPreferredNeighbors; i++){
				    		if(Neighbors[i] == h2.peer_ID){
				    			neighborFlag = 1;
				    			break;
				    		}
				    	}
				    	if(neighborFlag == 0){
				    		System.out.println("Nope, not a neighbor.");
				    		//If not a neighbor, send a choke packet.
				    		Iterator<PeerInfo> it = Peers.iterator();
				    		while(it.hasNext()){
				    			PeerInfo temp = it.next();
				    			if(temp.peer_ID == h2.peer_ID){
				    				System.out.println("Choking "+temp.peer_ID);
				    				sendPacket(temp, "Choke", oos);
				    			}
				    		}
				    		
				    	}
				    	else{
				    		
				    		//If it is a neighbor, send handshake and bitfield
				    		neighborFlag = 0;
					    	Handshake h3 = new Handshake(peer_ID);
					    	oos.writeObject(h3);
				    	}
				    	
				    	
/*				    	Piece p[] = new Piece[noOfBits];
				    	for(i = 0; i<noOfBits-1; i++){
				    		//Piece will have 1+4+pieceSize. The 4 is the size of the piece index value.
				    		p[i] = new Piece(1+4+pieceSize, (byte)7, newBytes[i], i);
				    	}
				    	if(fileSize%pieceSize!=0){
				    		p[i] = new Piece(1+4+fileSize%pieceSize, (byte)7, newBytes[i], i);
				    	}
				    	else{
				    		p[i] = new Piece(1+4+pieceSize, (byte)7, newBytes[i], i);
				    	}
				    	for(i = 0; i<noOfBits; i++){
				    		oos.writeObject(p[i]);
				    	}
*/				    	//if(h2.peer_ID == )
				    }
				    else if(o instanceof Bitfield){
				    	Bitfield received = (Bitfield)o;
				    	//System.out.println("Bitfield received "+received.bitfield.length());
				    	System.out.println("Bitfield received "+received.bitfield.length());
				    	neighborBitSets.put(currentNeighbor, received.bitfield);
				    	if(bitset.isEmpty()){
				    		Bitfield bcurrent = new Bitfield(1+(int)Math.ceil(bitset.size()/8), (byte)5, bitset);
					    	oos.writeObject(bcurrent);
				    	}
				    	else{
				    		//Server sends bitfield only if it has at least one true field.
				    		Bitfield bcurrent = new Bitfield(1+(int)Math.ceil(bitset.size()/8), (byte)5, bitset);
					    	oos.writeObject(bcurrent);
				    	}
				    }
				    else if(o instanceof PeerIdentifier){
				    	PeerIdentifier peerIdentity = (PeerIdentifier)o;
				    	Object o2 = ois.readObject();
				    	if(o2 instanceof Have_Request){
				    	Have_Request received = (Have_Request)o2;
				    	if(received.type == 1){
				    		//  If it is a Have message\
				    		System.out.println("Peer "+peerIdentity.peer_ID+" has received the piece "+received.piece_index_field);
				    		BitSet peerBitSet = neighborBitSets.get(peerIdentity.peer_ID);
				    		peerBitSet.set(received.piece_index_field);
				    		neighborBitSets.put(peerIdentity.peer_ID, peerBitSet);
				    	}
				    	else{
				    		//  If it is a Request message
					    	for(i = 0; i<=numberOfPreferredNeighbors; i++){
					    		if(Neighbors[i] == peerIdentity.peer_ID){
					    			neighborFlag = 1;
					    			break;
					    		}
					    	}
				    		if(neighborFlag == 0){
					    		System.out.println("Nope, not a neighbor.");
					    		//If not a neighbor, send a choke packet.
					    		Iterator<PeerInfo> it = Peers.iterator();
					    		while(it.hasNext()){
					    			PeerInfo temp = it.next();
					    			if(temp.peer_ID == peerIdentity.peer_ID){
					    				System.out.println("Choking "+temp.peer_ID);
					    				sendPacket(temp, "Choke", oos);
					    			}
					    		}
				    		}
				    		else{
				    			Piece piece = new Piece(1+4+pieceSize, (byte)7, newBytes[received.piece_index_field], received.piece_index_field);
				    			oos.writeObject(piece);
				    		}
				    	}
				    }
				    	else if(o2 instanceof Generic){
					    	Generic received = (Generic)o2;
					    	if(received.message_type == 0){
					    		System.out.println("Got a choke from "+peerIdentity.peer_ID);
					    		Enumeration<Integer> iterate = clientConnections.keys();
					    		while(iterate.hasMoreElements()){
					    			int requiredPeerID = iterate.nextElement();
					    			Controlclient cc = clientConnections.get(requiredPeerID);
					    			if(requiredPeerID == peerIdentity.peer_ID){
					    				cc.runThread = false;
					    				break;
					    			}
					    		}
					    		//break;
					    	}
					    	else if(received.message_type == 1){
					    		System.out.println("Got an unchoke from "+peerIdentity.peer_ID);
							int gotAll = 1;
							for(int n = 0; n < noOfBits; n++){
								if(bitset.get(n) == false){
								gotAll = 0;
								break;
								}
							}
							if(gotAll == 0){
					    		Enumeration<Integer> iterate = clientConnections.keys();
					    		System.out.println(clientConnections.size());
					    		while(iterate.hasMoreElements()){
					    			int requiredPeerID = iterate.nextElement();
					    			Controlclient cc = clientConnections.get(requiredPeerID);
					    			if(requiredPeerID == peerIdentity.peer_ID){
					    				cc.runThread = true;
					    				System.out.println("Waking up connection with "+requiredPeerID);
					    				break;
					    			}
					    		}
							}

					    		//break;
					    	}
				    	}
				    }
				    else if(o instanceof Generic){
				    	Generic received = (Generic)o;
				    	if(received.message_type == 2){
				    		System.out.println("Client interested.");
				    		//break;
				    	}
				    	else if(received.message_type == 3){
				    		//System.out.println("Client uninterested.");
				    		//System.exit(0);
				    		//break;
				    	}
				    }
				    else if(o instanceof Done){
				    	Done recei = (Done)o;
					finishers.put(recei.peer_ID, recei.peer_ID);
				    }
				    //System.out.println("Control at "+portNumber+" received "+str1+" from client.");
				    
				    }
				 }
			} catch (ClassNotFoundException e) {

				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
	}
	
	
	public static class concurr_Control extends Thread{
		int peer_ID;
		int portNumber;
		public concurr_Control(int pNumber, int peer_ID){
			portNumber = pNumber;
			this.peer_ID = peer_ID;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
	         try {
				ServerSocket welcomeSocket;
				welcomeSocket = new ServerSocket(portNumber, 50);
				//welcomeSocket.setReuseAddress(true);
		                //welcomeSocket.bind(new InetSocketAddress(portNumber));
				System.out.println("Server at port: "+portNumber+" "+InetAddress.getLocalHost().getHostName());
				 //welcomeSocket.setSoTimeout(30000);
				 if(papaBear == 1){
					 create_chunks();
				 }
				 while(true){
				    Socket connectionSocket;
				    connectionSocket = welcomeSocket.accept();
				    new eachConnection(connectionSocket, portNumber, peer_ID).start();
				 }

			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			
		}
		
	}
	
	public static class Controlclient extends Thread{
		volatile boolean runThread = false;
		int bitCounter=0, i =0;
		//int nOB = (fileSize%pieceSize!=0)? noOfBits+1:noOfBits;
		int peer_ID;
		String ipAddress;
		int targetPort;
		public Controlclient(String ip, int tPort, int peer_ID){
			ipAddress = ip;
			targetPort = tPort;
			this.peer_ID = peer_ID;
		}
		public void run(){
			try {
				//System.out.println(nOB);
				int currentServer = 0;
				newBytes2 = new byte[noOfBits][(int) pieceSize];
				//obinst.close();
				//obinst.
				//BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				
				
				while(true){
				if(runThread){
					System.out.println("Set to true.");
				Socket client;
				client = new Socket(ipAddress, targetPort);
				//System.out.println(client.);
				//System.out.println();
				ObjectOutputStream out;
				out = new ObjectOutputStream(client.getOutputStream());
				ObjectInputStream obinst = new ObjectInputStream(client.getInputStream());
				
				Handshake h1 = new Handshake(mypeer_ID);
				//String sentence = "Handshake";
				out.writeObject(h1);
				System.out.println("Handshake message started by "+mypeer_ID);
				Object o;
				
				while(true){
				
				o = obinst.readObject();
				if(o instanceof Handshake){
					Handshake h4 = (Handshake)o;
					currentServer = h4.peer_ID;
					System.out.println("Handshake done with "+h4.peer_ID);
			    	Bitfield current = new Bitfield(1+(int)Math.ceil(bitset.size()/8), (byte)5, bitset);
			    	out.writeObject(current);
			    	//obinst.close();
			    	//obinst = new ObjectInputStream(client.getInputStream());
			    	//obinst.
			    	
				}
			    	
			    else if(o instanceof Bitfield){
			    		int nextFlag = 0;		//This flag is to check if this client needs to make another call to the server
			    		Bitfield in = (Bitfield)o;
			    		neighborBitSets.put(currentServer, in.bitfield);
			    		System.out.println("Bitfield received from "+currentServer);
			    		
			    		while(nextFlag == 0){
			    		BitSet received = neighborBitSets.get(currentServer);
			    		System.out.println("Server "+currentServer+" has : "+received.toString());
					System.out.println("I, "+mypeer_ID+" have "+bitset.toString());
					System.out.println(noOfBits+" needed, have "+bitset.cardinality());
			    		BitSet bitset2 = (BitSet)bitset.clone();
			    		bitset2.xor(received);
			    		bitset2.and(received);
			    		System.out.println("I want: "+bitset2.toString());
			    		if(!bitset2.isEmpty()){
			    			Iterator<PeerInfo> it = Peers.iterator();
			    			while(it.hasNext()){
			    				PeerInfo temp = it.next();
			    				if(temp.peer_ID == currentServer){
			    					System.out.println("Sending interested.");
			    					sendPacket(temp, "Interested", out);
			    					int chosenPiece;
			    					ArrayList<Integer> toBeReq = new ArrayList<Integer>();
			    					for(int i = 0; i < noOfBits; i++){
			    						if(bitset2.get(i) == true){
			    							toBeReq.add(i);
			    						}
			    					}
			    					while(true){
			    						//PeerIdentifier
			    						Random rand = new Random();
			    						chosenPiece  = toBeReq.get(rand.nextInt(toBeReq.size()));
			    						System.out.println("Checking for "+chosenPiece);
			    						//if(!requestedPieces.containsKey(chosenPiece)){
			    							requestedPieces.put(chosenPiece, 1);
			    							System.out.println("Requesting for "+chosenPiece);
			    							break;
			    						//}
			    					}
			    					PeerIdentifier pi = new PeerIdentifier(mypeer_ID);
			    					out.writeObject(pi);
			    					Have_Request request = new Have_Request(10, (byte)6, chosenPiece, 2);
			    					out.writeObject(request);
			    					Object o2 = obinst.readObject();
			    					if(o2 instanceof Piece){
			    						Piece receivedPiece = (Piece)o2;
			    						bitset.set(receivedPiece.filePiece_index);
			    						newBytes[receivedPiece.filePiece_index] = receivedPiece.byte_array;
			    						System.out.println("Received piece "+ receivedPiece.filePiece_index+" from server "+currentServer);
			    						System.out.println("Received "+bitset.cardinality());
			    						
			    						
			    						Iterator<PeerInfo> iterate = Peers2.iterator();
			    						while(iterate.hasNext()){
			    							PeerInfo tempInfo = iterate.next();
			    							Socket tempConnection = new Socket(tempInfo.hostname, tempInfo.socket);
			    							PeerIdentifier piden = new PeerIdentifier(mypeer_ID);
			    							ObjectOutputStream tempout = new ObjectOutputStream(tempConnection.getOutputStream());
			    							tempout.writeObject(piden);
			    							Have_Request hr = new Have_Request(5, (byte)4, receivedPiece.filePiece_index, 1);
			    							tempout.writeObject(hr);
										tempout.writeObject(piden);
										tempout.writeObject(hr);
			    							tempConnection.close();
			    						}
			    						
			    						int allFlag = 0; 
			    						/*This flag, in the following loop, will check whether this peer has received all pieces.
			    						 * If it has, it is supposed to terminate this and all other download connections. 
			    						 */
			    						
			    						for(int i = 0; i<noOfBits; i++){
			    							if(bitset.get(i) == false){
			    								allFlag = 1;
			    							}
			    						}
			    						if(allFlag == 0){		//If it has all the pieces
			    							nextFlag = 1;
			    							System.out.println("Received all pieces.");
										int rn = 0;
	                                                                        Iterator<PeerInfo> iterateF = Peers2.iterator();
        	                                                                while(iterateF.hasNext()){
                	                                                                PeerInfo tempInfo = iterateF.next();
                        	                                                        Socket tempConnection = new Socket(tempInfo.hostname, tempInfo.socket);
                                	                                                //PeerIdentifier piden = new PeerIdentifier(mypeer_ID);
                                        	                                        ObjectOutputStream tempout = new ObjectOutputStream(tempConnection.getOutputStream());
                                                	                                //tempout.writeObject(piden);
                                                        	                        //Have_Request hr = new Have_Request(5, (byte)4, receivedPiece.filePiece_index, 1);
											Done d = new Done(mypeer_ID);
                                                                                        tempout.writeObject(d);
                                                                                        tempout.writeObject(d);
                                                                                        tempout.writeObject(d);
                                                                	                tempout.writeObject(d);
                                          	                                        //tempout.writeObject(piden);
                                                	                                //tempout.writeObject(hr);
                                                         	                        tempConnection.close();
                                                                        	}

/*
										File fileTemp = new File("peer_"+mypeer_ID+"/"+main_fileName);
										if (fileTemp.exists()){
										    fileTemp.delete();
										}
										//fileTemp.close();
*/
										RandomAccessFile raf2 = new RandomAccessFile("peer_"+mypeer_ID+"/"+main_fileName,"rw");
										while(rn<noOfBits){
											raf2.write(newBytes[rn]);
											rn++;
										}
										raf2.close();
			    							//client.close();
			    							runThread = false;
										System.out.println("Written to file.");
			    							//System.exit(0);
			    							break;
			    						}
			    						/*
			    						 * Here send Have message
			    						 */
			    						//nextFlag = 1;
			    					}
			    					else if(o2 instanceof Generic){
			    						Generic receivedGeneric = (Generic)o2;
			    						if(receivedGeneric.message_type == 0){
			    							requestedPieces.remove(chosenPiece);
			    							nextFlag = 1;
			    							runThread = false;
			    						}
			    					}
			    					break;
			    				}
			    			}
			    		}
			    		else{
			    			Iterator<PeerInfo> it = Peers.iterator();
			    			while(it.hasNext()){
			    				PeerInfo temp = it.next();
			    				if(temp.peer_ID == currentServer){
			    					//System.out.println("Sending not interested "+mypeer_ID+" "+finishers.size());
			    					//System.out.println("My bitset: "+bitset);
			    					sendPacket(temp, "Uninterested", out);

                                                                        if(bitset.cardinality()>=noOfBits){               //If it has all the pieces
                                                                                nextFlag = 1;
                                                                                System.out.println("Received all pieces.");
                                                                                int rn = 0;
                                                                                Iterator<PeerInfo> iterateF = Peers2.iterator();
                                                                                while(iterateF.hasNext()){
                                                                                        PeerInfo tempInfo = iterateF.next();
                                                                                        Socket tempConnection = new Socket(tempInfo.hostname, tempInfo.socket);
                                                                                        //PeerIdentifier piden = new PeerIdentifier(mypeer_ID);
                                                                                        ObjectOutputStream tempout = new ObjectOutputStream(tempConnection.getOutputStream());
                                                                                        //tempout.writeObject(piden);
                                                                                        //Have_Request hr = new Have_Request(5, (byte)4, receivedPiece.filePiece_index, 1);
                                                                                        Done d = new Done(mypeer_ID);
                                                                                        tempout.writeObject(d);
                                                                                        //tempout.writeObject(piden);
                                                                                        //tempout.writeObject(hr);
                                                                                        tempConnection.close();
                                                                                }
/*

                                                                                RandomAccessFile raf2 = new RandomAccessFile("outputs/"+mypeer_ID+main_fileName,"rw");
                                                                                while(rn<noOfBits){
                                                                                        raf2.write(newBytes[rn]);
                                                                                        rn++;
                                                                                }
                                                                                raf2.close();*/
                                                                                //client.close();
                                                                                runThread = false;
                                                                                //System.exit(0);
                                                                                //break;
                                                                        }



			    					//System.exit(0);
			    					break;
			    				}
			    			}
			    		}
			    		
			    		}
			    	}
				else if(o instanceof Generic){
					Generic received = (Generic)o;
					System.out.println("Received a message of type "+received.message_type);
					if(received.message_type == 0){
						break;
					}
				}
			    	if(runThread == false){
			    		break;
			    	}
				}
				client.close();
				}
				else{
					//If runThread is false
					
				}
				}
			    	/*The following is a code where there are only two peers and this is the client.
			    	 * It will just receive all the pieces from the server assuming that the server has everything.
			    	 * Need to change this. 
			    	 */
			    	
			    	
/*			    	while(bitCounter<noOfBits){
						o = ois.readObject();
						//RandomAccessFile raf2 = new RandomAccessFile("received.dat", "rw");
						if(o instanceof Piece){
							Piece rec = (Piece)o;
							if(rec.filePiece_index == noOfBits-1 && fileSize%pieceSize!=0){
								newBytes2[rec.filePiece_index] = new byte[fileSize%pieceSize];
							}
							newBytes2[rec.filePiece_index] = rec.byte_array;
							//byte[] rec_bytes = rec.byte_array;
							//raf2.wr
						}
						bitCounter++;
					}
					System.out.println(bitCounter);
					RandomAccessFile raf2;
					raf2 = new RandomAccessFile("received.dat", "rw");
					while(i<noOfBits){
						raf2.write(newBytes2[i]);
						i++;
					}
					raf2.close();
*/
			    	
				
				
				//}
				//ObjectInputStream ob = new ObjectInputStream(client.getInputStream());
				//String rec = in.readLine();
				//System.out.println("From "+targetPort+" control: "+rec);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		//System.out.println(args[0]);
		//System.exit(0);
		readCommonfile();
                create_field();
		readPeerInfofile();
                Iterator<PeerInfo> it2 = Peers.iterator();
/*
                while(it2.hasNext()){
                        PeerInfo pi = it2.next();
			if(pi.hostname.equals(InetAddress.getLocalHost().getCanonicalHostName())){
				mypeer_ID = pi.peer_ID;
				System.out.println("My peer ID set to "+mypeer_ID);
			}
			else{
				//System.out.println(pi.hostname" unmatched");
			}
		}
*/
		mypeer_ID = Integer.parseInt(args[0]);


/*                                                                                File fileTemp = new File("peer_"+mypeer_ID+"/"+main_fileName);
                                                                                if (fileTemp.exists()){
                                                                                    fileTemp.delete();
                                                                                }
*/

		Iterator<PeerInfo> it = Peers.iterator();
		while(it.hasNext()){
			PeerInfo pi = it.next();
			if(pi.hasData == 1 && pi.peer_ID == mypeer_ID){
				for(int i = 0; i<noOfBits; i++){
					bitset.set(i);
				}
				papaBear = 1;
			}
			else if(pi.peer_ID == mypeer_ID){
				for(int i = 0; i<noOfBits; i++){
					bitset.set(i,false);
				}
			}
		}
		if(papaBear!=1){
		                                                                                File fileTemp = new File("peer_"+mypeer_ID+"/"+main_fileName);
                                                                                if (fileTemp.exists()){
                                                                                    fileTemp.delete();
                                                                                }


		}
/*
		if(InetAddress.getLocalHost().getHostName().equals("lin114-03.cise.ufl.edu")){
			System.out.println("I'm papaBear.");
			papaBear = 1;
		}
		else{
			//byte[] ip = {(byte)128, (byte)227, (byte)248, (byte)169};
			System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
		}
*/
		Iterator<PeerInfo> it3 = Peers.iterator();
                while(it3.hasNext()){
                        PeerInfo pi = it3.next();
			if(pi.peer_ID == mypeer_ID){
				System.out.println("My server started at "+pi.peer_ID);
				new concurr_Control(pi.socket, pi.peer_ID).start();
			}
		}
		//System.out.println(InetAddress.getByName("tushar-Lenovo-IdeaPad-Y470.local").getHostAddress());
		Thread.sleep(10000);
		int n=1;
		//String ip = "10.136.20.13";
		System.out.println(Peers.size());
		createOtherPeers();
		 Iterator<PeerInfo> iterator = otherPeers.iterator();
		 while(iterator.hasNext()){
			 PeerInfo peerinfo = iterator.next();
			 Controlclient cc = new Controlclient(InetAddress.getByName(peerinfo.hostname).getHostAddress(), peerinfo.socket, peerinfo.peer_ID);
			 cc.start();
			 System.out.println("Connected to peer "+peerinfo.peer_ID+" at port "+peerinfo.socket);
			 clientConnections.put(peerinfo.peer_ID, cc);
		 }
		 Thread.sleep(1000);
		 initialNeighbors();
		 new NeighborUpdate(5, true);
		//new Controlclient(Peers.get(0).hostname, Peers.get(0).socket, Peers.get(0).peer_ID).start();
		//concurr_Control s[] = new concurr_Control[n];
/*		for(int i = 0; i<n; i++){
			s[i] = new concurr_Control(7001, 1000);
			s[i].start();
		}
		
		Controlclient c[] = new Controlclient[n];
		for(int i = 0; i<n; i++){
			c[i] = new Controlclient(ip, 7001, 1001);
			c[i].start();
		}
*/		
	}
}
