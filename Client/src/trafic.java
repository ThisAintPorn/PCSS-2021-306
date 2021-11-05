import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class trafic {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		boolean connect = true;
		int lastBlockCenterX;
		int blockPosition;
		boolean firstTimeId = true;
		int playerId = 0;
		int score = 0, playerLives = 3;
		int p1score, p2score, p3score, p1lives, p2lives, p3lives;
		System.out.println("Enter ip address for example 192.168.1.1");
		String ipAddress = input.next();
		System.out.println("The ip address is " + ipAddress);
		System.out.println("Enter port as an integer, for example 2345");
		int port = input.nextInt();
		System.out.println("The port is " + port);
		input.close();
		
		
		try {
			//A socket to connect to the server
			Socket connectToServer = new Socket(ipAddress, port);
			
			DataInputStream dip = new DataInputStream(connectToServer.getInputStream());
		
			DataOutputStream dop = new DataOutputStream(connectToServer.getOutputStream());

			while (connect) {
				
				//The first time the connection happens, the client receives the player ID
				if (firstTimeId == true) {
					playerId = dip.readInt();
					firstTimeId = dip.readBoolean();
				}
				
				//Send score to the server
				dop.writeInt(score);
				
				//Send lives to the server
				dop.writeInt(playerLives);
				
				//Block position
				//INSERT HERE YO
				
				//Receive other players' data from server
				if(playerId == 1) {
					p2score = dip.readInt();
					p2lives = dip.readInt();
					p3score = dip.readInt();
					p3lives = dip.readInt();
            	}else if(playerId == 2) {
            		p1score = dip.readInt();
					p1lives = dip.readInt();
					p3score = dip.readInt();
					p3lives = dip.readInt();
            	}else if(playerId == 3) {
            		p1score = dip.readInt();
					p1lives = dip.readInt();
					p2score = dip.readInt();
					p2lives = dip.readInt();
            	}
				
			}
			connectToServer.close();
		}
		catch (IOException ex) {
		System.out.println(ex.toString() + '\n');
		}
	}
}
		
