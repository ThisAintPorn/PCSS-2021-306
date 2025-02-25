package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class Player implements Runnable {
    private Socket clientSocket;
    private String serverName;
	private int playerId;
	private int score;
	private int playerLives;
	private int lastBlockPos;
	private Server server;
    //int p1score, p2score, p3score, p1lives, p2lives, p3lives;
	private boolean firstTimeId = true;
	private boolean receiveBool = false;
	private boolean putBlock = false;
	private boolean waitForStart = true;

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
            		
            		if (waitForStart) {
            			System.out.println("Player "+ playerId +" is: READY");
            			if (server.getClients() == 3) {
            				dop.writeBoolean(true);
            				dop.writeBoolean(false);
            				waitForStart = false;
            			} else {
            				dop.writeBoolean(false);
            				dop.writeBoolean(true);
            			}
            		}
            		
            		dop.flush();
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
            			dop.writeInt(2);
            			dop.writeInt(server.getP2LastBlock());
            			server.setP2Send(false);
            			
            		}else if (playerId == 1 && server.getP3Send() == true) {
            			dop.writeInt(3);
            			dop.writeInt(server.getP3LastBlock());
            			server.setP3Send(false);
            			
            		}else if (playerId == 2 && server.getP1Send() == true) {
            			dop.writeInt(1);
            			dop.writeInt(server.getP1LastBlock());
            			server.setP1Send(false);
            			
            		}else if (playerId == 2 && server.getP3Send() == true) {
            			dop.writeInt(3);
            			dop.writeInt(server.getP3LastBlock());
            			server.setP3Send(false);
            			
            		}else if (playerId == 3 && server.getP1Send() == true) {
            			dop.writeInt(1);
            			dop.writeInt(server.getP1LastBlock());
            			server.setP1Send(false);
            			
            		}else if (playerId == 3 && server.getP2Send() == true) {
            			dop.writeInt(2);
            			dop.writeInt(server.getP2LastBlock());
            			server.setP2Send(false);
            			
            		}else {
            			dop.writeInt(0);
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
                	dop.flush();
            	
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
}