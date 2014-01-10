import java.io.*;
import java.net.*;
/*
 * TIMEOUT HAS BEEN SET FOR THE SERVER SOCKET PORT TO 10 SECONDS
 */
/*class msg implements Serializable
{
	int a; 
	String name;
}*/

class Peer2 
{
	public static class concurr_Control extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
	         try {
				ServerSocket welcomeSocket = new ServerSocket(7002);
				 welcomeSocket.setSoTimeout(10000);
				 while(true) 
				 {
				    Socket connectionSocket = welcomeSocket.accept();
				    System.out.println(connectionSocket.getInetAddress());
				    BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				    String str1 = in.readLine();
				    System.out.println(str1+" received from peer1.");
				    DataOutputStream dout = new DataOutputStream(connectionSocket.getOutputStream());
				    dout.writeBytes("Acknowledged\n");
				 }
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
   public static void main(String argv[]) throws Exception
      {
         msg m = new msg();
         concurr_Control s1 = new concurr_Control();
         s1.start();
 		Socket client = new Socket("10.136.100.245", 7001);
 		DataOutputStream out = new DataOutputStream(client.getOutputStream());
 		BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
 		String sentence = "Handshake";
 		out.writeBytes(sentence+'\n');
 		//ObjectInputStream ob = new ObjectInputStream(client.getInputStream());
 		String rec = in.readLine();
 		System.out.println("From peer1 control: "+rec);
 		client.close();
      }
}
