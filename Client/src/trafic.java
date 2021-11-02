import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class trafic {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		boolean connect = true;
		int position = 50;
		System.out.println("Enter ip address in quotation marks, for example \"192.168.1.1\" ");
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
			//Send position of the next block to the server
			dop.writeInt(position);
			dop.flush();
			//Receive crookedness from server? Maybe not?
			
			//Get player ID and position player on screen depending on their player ID
			int playerid = dip.readInt();
			//
			//
			//After the server calculates the score and remaining lives, send that back to client and display it
			int score = dip.readInt();
			int lives = dip.readInt();
			
			if (lives <= 0){
					gameover();
			}
			
			}
			connectToServer.close();
		}
		catch (IOException ex) {
		System.out.println(ex.toString() + '\n');
		}
	}
	static void gameover() {
		//Ends the game for the player?
	}
}
		
