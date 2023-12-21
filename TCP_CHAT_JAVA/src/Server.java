import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final ServerSocket serverSocket;

    public Server( ServerSocket serverSocket) {

        this.serverSocket = serverSocket;

    }

    public void startServer() {
        try {
            System.out.println("Server is running");

            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                System.out.println("A new Client has connected");
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void closeServer() throws IOException {
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(3000);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}