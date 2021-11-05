package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server {
	static int port = 2345;
    double rate, amount;
    int years,clients;
    static int p1score, p2score, p3score, p1lives, p2lives, p3lives;
 

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        new Thread(() -> {
            Server server = new Server();
        	server.clientThread();
        }).start();
    }
    
    public void clientThread() {
    	try {

            ServerSocket server = new ServerSocket(port);
            System.out.println("Server started at: " + new Date());

            int clients = 0;

            while (true) {


                Socket clientSocket = server.accept();
                clients++;
                System.out.println("Connection to client at: " + new Date());


                System.out.println("Processing client "+clients+" at "+new Date());


                InetAddress inetAddress = clientSocket.getInetAddress();
                System.out.println("Client "+clients+" has name: "+ inetAddress.getHostName()+" and address: "+inetAddress.getHostAddress());

                //New thread is made with a Player constructor, that passes client socket and server name on
                if (clients < 3){
                	new Thread(new Player(clientSocket, "multi server", clients, this)).start();
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
    
    public static void setP1Score(int s) {
    	p1score = s;
    }
    
    public static void setP1Lives(int l) {
    	p1lives = l;
    }
    
    public int getP1Score() {
    	return p1score;
    }
    
    public int getP1Lives() {
    	return p1lives;
    }
    
    public static void setP2Score(int s) {
    	p2score = s;
    }
    
    public static void setP2Lives(int l) {
    	p2lives = l;
    }
    
    public int getP2Score() {
    	return p2score;
    }
    
    public int getP2Lives() {
    	return p2lives;
    }
    
    public static void setP3Score(int s) {
    	p3score = s;
    }
    
    public static void setP3Lives(int l) {
    	p3lives = l;
    }
    
    public int getP3Score() {
    	return p3score;
    }
    
    public int getP3Lives() {
    	return p3lives;
    }
}
