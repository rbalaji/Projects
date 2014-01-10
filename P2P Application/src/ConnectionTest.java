import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.BitSet;
import java.util.Iterator;


public class ConnectionTest {

	public static class concurr_Control extends Thread{
		int peer_ID;
		int portNumber;
		int neighborFlag = 0;
		public concurr_Control(int pNumber, int peer_ID){
			portNumber = pNumber;
			this.peer_ID = peer_ID;
		}
		@Override
		public void run(){
			// TODO Auto-generated method stub
	         try {
				ServerSocket welcomeSocket;
				welcomeSocket = new ServerSocket(portNumber);
				 welcomeSocket.setSoTimeout(10000);
				 while(true) 
				 {
					 int i;
				    Socket connectionSocket;
				    connectionSocket = welcomeSocket.accept();
				    System.out.println(connectionSocket.getInetAddress());
				    System.out.println(connectionSocket.getPort());
				    //BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				    ObjectInputStream ois;
				    ois = new ObjectInputStream(connectionSocket.getInputStream());
				    ObjectOutputStream oos;
				    oos = new ObjectOutputStream(connectionSocket.getOutputStream());
				    Object o;
				    o = ois.readObject();
				    System.out.println(o.toString());
				    o = ois.readObject();
				    System.out.println(o.toString());
				    o = ois.readObject();
				    System.out.println(o.toString());
				    
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static class Controlclient extends Thread{
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
				//newBytes2 = new byte[noOfBits][(int) pieceSize];
				Socket client;
				client = new Socket(ipAddress, targetPort);
				ObjectOutputStream out;
				out = new ObjectOutputStream(client.getOutputStream());
				ObjectInputStream ois;
				ois = new ObjectInputStream(client.getInputStream());
				//BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				String a = "Hello"+peer_ID;
				out.writeObject(a);
				out.writeObject(a+"Second");
				//}
				//ObjectInputStream ob = new ObjectInputStream(client.getInputStream());
				//String rec = in.readLine();
				//System.out.println("From "+targetPort+" control: "+rec);
				client.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new concurr_Control(6002, 1001).start();
		new Controlclient("10.136.48.226", 6002, 1002).start();
		new Controlclient("10.136.48.226", 6002, 1003).start();
	}
}
