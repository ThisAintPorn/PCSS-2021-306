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
    int lastBlockPos;
    Server server;
    //int p1score, p2score, p3score, p1lives, p2lives, p3lives;
    boolean firstTimeId = true;
    boolean receiveBool = false;
    boolean putBlock = false;

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
            	
            	//Sends the id of the player to the client the first time the connection happens
            	if (firstTimeId) {
            		dop.writeInt(playerId);
            		//dop.writeBoolean(false);
            		firstTimeId = false;
            	}
            	
            	
            		//Receives other players info about corresponding scores and lives (and probably block positions?)
                	//playerReceiver();
            		boolean start = true;
            		if (start) {
            			dop.writeBoolean(true);
            			dop.writeBoolean(false);
            			System.out.println("staaart");
            			start = false;
            		}
            		
                	//Receive score from the client
                	score = dip.readInt();
         
                	//Receive amount of lives from client
                	playerLives = dip.readInt();
                	
                	//Receive block position
                	receiveBool = dip.readBoolean();
                	if(receiveBool) {
                		lastBlockPos = dip.readInt();
                		receiveBool = false;
                		if(playerId == 1) {
                			server.setP1Send(true);
                		}else if (playerId == 2) {
                			server.setP2Send(true);
                		}else if (playerId == 3) {
                			server.setP3Send(true);
                		}
                		
                	}
                	//Send block position
                	if(playerId == 1 && server.getP2Send() == true) {
            			
            		}else if (playerId == 1 && server.getP3Send() == true) {
            			
            		}else if (playerId == 2 && server.getP1Send() == true) {
            			
            		}else if (playerId == 2 && server.getP3Send() == true) {
            			
            		}else if (playerId == 3 && server.getP1Send() == true) {
            			
            		}else if (playerId == 3 && server.getP2Send() == true) {
            			
            		}else {
            			//dop.writeInt(0);
            		}
                	
                	//Selects player and sets the corresponding lives and score to the one in the object.
                	playerSelector();
                	
                	
                	
                	//Send block position
                	/*if (putBlock) {
                		if(playerId == 1) {
                    		dop.writeInt(server.getP2LastBlock());
                    		dop.writeInt(server.getP3LastBlock());
                    	}else if(playerId == 2) {
                    		dop.writeInt(server.getP1LastBlock());
                    		dop.writeInt(server.getP3LastBlock());
                    	}else if(playerId == 3) {
                    		dop.writeInt(server.getP1LastBlock());
                    		dop.writeInt(server.getP2LastBlock());
                    	}
                	}*/
                	
                	
                	//Send other players' data to client
                	if(playerId == 1) {
                		dop.writeInt(server.getP2Score());
                		dop.writeInt(server.getP2Lives());
                		dop.writeInt(server.getP3Score());
                		dop.writeInt(server.getP3Lives());
                	}else if(playerId == 2) {
                		dop.writeInt(server.getP1Score());
                		dop.writeInt(server.getP1Lives());
                		dop.writeInt(server.getP3Score());
                		dop.writeInt(server.getP3Lives());
                	}else if(playerId == 3) {
                		dop.writeInt(server.getP1Score());
                		dop.writeInt(server.getP1Lives());
                		dop.writeInt(server.getP2Score());
                		dop.writeInt(server.getP2Lives());
                	}
            	
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
    		server.setP1LastBlock(lastBlockPos);
    	}else if(playerId==2) {
    		server.setP2Score(score);
    		server.setP2Lives(playerLives);
    		server.setP2LastBlock(lastBlockPos);
    	}else if(playerId==3) {
    		server.setP3Score(score);
    		server.setP3Lives(playerLives);
    		server.setP3LastBlock(lastBlockPos);
    	}
    }
    /*
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
    */
}