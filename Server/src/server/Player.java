package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class Player implements Runnable {
    Socket clientSocket;
    String serverName;
    int playerId;
    int score;
    int playerLives;
    int blockPosition;
    Server server;
    int p1score, p2score, p3score, p1lives, p2lives, p3lives;

    public Player(Socket s, String n, int id, Server serv) {
        this.clientSocket = s;
        this.serverName = n;
        //Add player ID to constructor
        this.playerId = id;
        this.server = serv;

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
            	//Selects player and sets the corresponding lives and score to the one in the object.
            	playerSelector();
            	
            	//Receives other players info about corresponding scores and lives (and probably block positions?)
            	playerReceiver();

            	//Receive score from the client
            	score = dip.readInt();
     
            	//Receive amount of lives back to player
            	playerLives = dip.readInt();
            	
            	//Receive information about where the block landed from client
            	blockPosition = dip.readInt();
            	
            	
            	
               
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lost connection to server socket");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("something went wrong");
        }
    }
    
    public void playerSelector() {
    	if(playerId == 1) {
    		server.setP1Score(score);
    		server.setP1Lives(playerLives);
    	}else if(playerId==2) {
    		server.setP2Score(score);
    		server.setP2Lives(playerLives);
    	}else if(playerId==3) {
    		server.setP3Score(score);
    		server.setP3Lives(playerLives);
    	}
    }
    
    public void playerReceiver() {
    	if(playerId == 1) {
    		p2score = server.getP2Score();
    		p2lives = server.getP2Lives();
    		p3score = server.getP3Score();
    		p3lives = server.getP3Lives();
    	}else if(playerId==2) {
    		p1score = server.getP1Score();
    		p1lives = server.getP1Lives();
    		p3score = server.getP3Score();
    		p3lives = server.getP3Lives();
    	}else if(playerId==3) {
    		p2score = server.getP2Score();
    		p2lives = server.getP2Lives();
    		p1score = server.getP1Score();
    		p1lives = server.getP1Lives();
    	}
    }
}