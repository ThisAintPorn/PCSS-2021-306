package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		int port = 2345;
		
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			//How to accept connection from one client
			//Socket connectToClient = serverSocket.accept();
			//Create new thread for each accepted client
			
			Player p1 = new Player("player1");
			Player p2 = new Player("player2");
			Player p3 = new Player("player3");
			
			while (true) {
				//Example on how to handle a player class' object's (p1) dataoutput stream (dop), with the player's position variable
				p1.dop.readInt(p1.position);
			}
			
		}
		catch (IOException ex) {
			System.out.println(ex.toString() + '\n');
		}
	}
	
	
}
