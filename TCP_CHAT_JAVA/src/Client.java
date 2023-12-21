import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket client ;
    private BufferedReader bufferedReader; // recieve msg
    private BufferedWriter bufferedWriter; // send msg
    private String  username;

    public Client(Socket client , String username) {
        try{
            this.client = client;
            this.bufferedWriter =  new BufferedWriter( new OutputStreamWriter(client.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(client.getInputStream()));
            this. username = username;
        } catch(IOException e) {
            close( client , bufferedReader , bufferedWriter);
        }
    }

    public void sendMsg() {

        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            while (client.isConnected()) {
                String msgToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + msgToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

        } catch(IOException e) {

            close( client , bufferedReader , bufferedWriter);
        }
    }

    public void close (Socket socket , BufferedReader bufferedReader , BufferedWriter bufferedWriter) {

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
            e.printStackTrace();
        }

    }

    public void listenToMsg() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (client.isConnected()) {
                        String msgFromOthers = bufferedReader.readLine();
                        if (msgFromOthers != null) {
                            System.out.println(msgFromOthers);
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    close(client, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please choose a username");
        String username = scanner.nextLine();
        Socket socket = new Socket( "localhost" , 3000);
        Client client = new Client(socket, username);
        client.listenToMsg();
        client.sendMsg();


    }

}
