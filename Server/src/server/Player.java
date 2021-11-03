package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Date;

public class Player implements Runnable {
    Socket clientSocket;
    String serverName;

    public Player(Socket s, String n) {
        this.clientSocket = s;
        this.serverName = n;

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
            	//Shit that is calculated for the player happens here
            	//
            	//
               
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