// ssh -o StrictHostKeyChecking=no lin114-03.cise.ufl.edu "cd PtPApp/src ; java PeerProgram"
import java.io.*;
import java.util.*;
class startall{


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
	public static int fileSize = 3195696;
	public static int pieceSize = 32768;
	public static RandomAccessFile raf = null;
	public static byte[][] newBytes;
	public static byte[][] newBytes2;
	public static int noOfBits = (fileSize%pieceSize==0)?(fileSize/pieceSize):(fileSize/pieceSize+1);
	public static Hashtable<Integer, BitSet> neighborBitSets = new Hashtable<Integer, BitSet>();
	public static Hashtable<Integer, Integer> requestedPieces = new Hashtable<Integer, Integer>();
	public static int fcount = 0;
	//public static Hashtable<Integer, Controlclient> clientConnections = new Hashtable<Integer, Controlclient>();



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
			// TODO: handle exception
		}
	}
  public static class RunProcesses extends Thread{
	int peer_ID;
	String hostname;
	public RunProcesses(String hostname, int peer_ID){
		this.hostname = hostname;
		this.peer_ID = peer_ID;
	}
	@Override
	public void run(){
	try{
        String path = System.getProperty("user.dir");
                        System.out.println(path);
	
                String s = "ssh -o StrictHostKeyChecking=no "+hostname+" cd PtPApp/src; java PeerProgram "+peer_ID;
                //String s = "java PeerProgram";
                //String s = "ls";
                System.out.println("Starting "+hostname);
                System.out.println("ssh -o StrictHostKeyChecking=no balaji@"+hostname+" java PeerProgram");
//              Process p = Runtime.getRuntime().exec("ls");
//              Process p = Runtime.getRuntime().exec("ssh -o StrictHostKeyChecking=no "+peer.hostname+" \"cd PtPApp/src ; java PeerProgram\"");

                Runtime r = Runtime.getRuntime();
                Process p = r.exec(s);
                //p.waitFor();
                BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
                BufferedReader b2 = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line = "";

                while ((line = b.readLine()) != null) {
                  System.out.println(line);
		  if(line.equals("All done.")){//"Written to file."))//||line.equals("All done.")){
			fcount++;
		  }
		  if(fcount>=6){
			System.out.println("Program terminating.");
			System.exit(0);
		  }
                }
                while ((line = b2.readLine()) != null) {
                  //System.out.println(line);
                }

	}
catch(Exception e){}
	}
 }

  public static void main(String args[]){
	try{
	readPeerInfofile();
       	Iterator<PeerInfo> it = Peers.iterator();
	String path = System.getProperty("user.dir");
			System.out.println(path);
			
	while(it.hasNext()){
		PeerInfo peer = it.next();
		new RunProcesses(peer.hostname, peer.peer_ID).start();
		//break;
	} 
  }
  catch(Exception e){
	}
}
}
