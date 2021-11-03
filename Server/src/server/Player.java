package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class Player implements Runnable {
    Socket clientSocket;
    String serverName;
    int playerid;
    int score;
    int playerLives;
    int blockPosition;

    public Player(Socket s, String n) {
        this.clientSocket = s;
        this.serverName = n;
        //Add player ID to constructor

    }
    
    public void run() {
        try {

            boolean connected = true;
            System.out.println("Connection to client at: " + new Date());

            //Data coming from clients
            DataInputStream dip = new DataInputStream(clientSocket.getInputStream());
            //Data going to clients
            DataOutputStream dop = new DataOutputStream(clientSocket.getOutputStream());

            while (connected) {
            	//Stuff that is calculated for the player happens here
            	//Receive information about where the block landed from client
            	blockPosition = dip.readInt();
            	//Calculate crookedness of the tower? Maybe?
            	//
            	//Calculate score after block fell down
            	//
            	//Send score to the client
            	dop.writeInt(score);
            	//Calculate if the player lost a life
            	//
            	//Send amount of lives back to player
            	dop.writeInt(playerLives);
               
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lost connection to server socket");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("something went wrong");
        }
    }
}