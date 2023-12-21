import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clients = new ArrayList<>();

    private Socket client ;
    private BufferedReader bufferedReader; // recieve msg
    private BufferedWriter bufferedWriter; // send msg
    private String  username;
    public ClientHandler(Socket client) {

        try{
            this.client = client;
            this.bufferedWriter =  new BufferedWriter( new OutputStreamWriter(client.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(client.getInputStream()));
            this. username = bufferedReader.readLine();
            clients.add(this);
            broadCastMsg ("INFO : "+ username + " is connected" );
        } catch(IOException e) {
            close( client , bufferedReader , bufferedWriter);
        }

    }

    @Override
    //listen to message , seperated thread
    public void run() {
        String msg;
         while (client.isConnected()) {

             try {
                 msg = bufferedReader.readLine(); //blocking operation
                 broadCastMsg(msg);

             } catch (IOException e) {
                 close (client , bufferedReader , bufferedWriter);
                 break;
             }
         }


    }

    public void broadCastMsg( String msg) {

        for (ClientHandler clientHandler : clients) {

                try {
                    if ( !clientHandler.username.equals(this.username) ) {
                        clientHandler.bufferedWriter.write(msg);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush(); }
                } catch (IOException e) {
                    close (client , bufferedReader , bufferedWriter);
                }


            }
        }


    public void removeClient() {
        clients.remove(this);
        broadCastMsg("INFO : " + this.username + " has left the chat" );
    }

    public void close (Socket socket , BufferedReader bufferedReader , BufferedWriter bufferedWriter) {

        removeClient();
        try {
            if (socket!=null) {
            socket.close();
            }
            if( bufferedReader!=null) {
            bufferedReader.close();}

            if (bufferedWriter!=null) {
            bufferedWriter.close();
            }
        } catch (IOException e) {
             System.out.println(e);
        }

    }
}
