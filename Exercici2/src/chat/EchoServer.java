package chat;

import java.io.IOException;
import java.net.ServerSocket;

public class EchoServer {
    public static void main(String[] args) {
        int PortNumber = 5555;

        try {
            ServerSocket serverSocket = new ServerSocket(PortNumber);
            while (true) {
                ClientWorker w;
                System.out.println("Waitting connection");
                w = new ClientWorker(serverSocket.accept());
                System.out.println("Client connected");
                Thread t = new Thread(w);
                t.start();
                System.out.println("New Thread working");
            }
        } catch (IOException e) {
            System.err.println("Error on Echo Server v2");
        }
    }
}
