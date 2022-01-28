package bbdd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Cliente {
    
    public static BufferedReader getFlujo(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bfr = new BufferedReader(isr);
        return bfr;
    }
    
    public static void main(String[] args) {
        Socket socket = new Socket();
        InetSocketAddress direccion = new InetSocketAddress("localhost", 5555);
        
        try {
            socket.connect(direccion);

            while (true) {
                System.out.println("insert, select or delete data:");
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                String command = reader.readLine();

                switch (command) {
                    case "insert" -> {
                        System.out.println("id:");
                        String id = reader.readLine();
                        System.out.println("name:");
                        String name = reader.readLine();
                        System.out.println("surname:");
                        String surname = reader.readLine();
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        pw.print(command + "\n");
                        pw.print(id + "\n");
                        pw.print(name + "\n");
                        pw.print(surname + "\n");
                        pw.flush();
                        System.out.println(getDataFromServer(socket));
                    }
                    case "select" -> {
                        System.out.println("id:");
                        String id = reader.readLine();
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        pw.print(command + "\n");
                        pw.print(id + "\n");
                        pw.flush();
                        System.out.println(getDataFromServer(socket));
                    }
                    case "delete" -> {
                        System.out.println("id:");
                        String id = reader.readLine();
                        PrintWriter pw = new PrintWriter(socket.getOutputStream());
                        pw.print(command + "\n");
                        pw.print(id + "\n");
                        pw.flush();
                        System.out.println(getDataFromServer(socket));
                    }
                    default -> System.out.println("Command not recognized, try again.");
                }

            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    private static String getDataFromServer(Socket socket) throws IOException {
        BufferedReader bfr = Cliente.getFlujo(socket.getInputStream());
        return ("The result from server is: " + bfr.readLine());
    }
}
