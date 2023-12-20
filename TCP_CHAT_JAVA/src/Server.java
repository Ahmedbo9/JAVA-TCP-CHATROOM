import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private ArrayList <ConnectionHandler> connexions ;
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(8000);
            Socket client = serverSocket.accept();
            ConnectionHandler handler =  new ConnectionHandler(client);
            connexions.add(handler);
        } catch (IOException e) {
            //TODO HANDEL ERROR
        }

    }
}

class ConnectionHandler implements  Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;

    private String username ;

    public ConnectionHandler ( Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(client.getOutputStream() ,true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("Please select a username");
            username = in.readLine();
            System.out.println(username + "is connected to server");


        } catch (IOException e){

            //TODO

        }

    }
}
