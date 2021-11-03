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

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        new Thread(() -> {
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
                    if (clients <= 3){
                    	new Thread(new Player(clientSocket, "multi server")).start();
                    }
                    
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Lost connection to server socket");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("something went wrong");
            }

        }).start();
    }
}
