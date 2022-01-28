package bbdd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Servidor {
    public static void main(String[] args) throws IOException {
        int puertoDestino = 5555;

        File bbdd = new File("bbdd.txt");
        bbdd.createNewFile();

        try {
            ServerSocket serverSocket = new ServerSocket(puertoDestino);
            Socket socket = serverSocket.accept();

            while (true) {

                System.out.println("Client conected!");

                //Read From Stream
                BufferedReader b = readClient(socket);
                String comand = b.readLine();

                if (null == comand) {
                    sendToClient(socket, "Comand not recognized, try again");
                } else switch (comand) {
                    case "select" -> {
                        //Write In Stream
                        String id = b.readLine();
                        String construct = findElement(id);
                        sendToClient(socket, construct);
                    }
                    case "insert" -> {
                        //Write In Stream
                        String id = b.readLine();
                        String name = b.readLine();
                        String surname = b.readLine();
                        String construct = id + "\t" + name + "\t" + surname + "\n";
                        Files.write(Paths.get("bbdd.txt"), construct.getBytes(), StandardOpenOption.APPEND);
                        sendToClient(socket, "Inserted data on BBDD");
                    }
                    case "delete" -> {
                        //Write In Stream
                        String id = b.readLine();
                        boolean r = deleteElement(id);
                        if (r = true) {
                            sendToClient(socket, "Deleted data on BBDD");
                        } else {
                            sendToClient(socket, "NOTHING has been deleted on BBDD");
                        }
                    }
                    default -> sendToClient(socket, "Comand not recognized, try again");
                }
            }

        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    private static BufferedReader readClient(Socket s) throws IOException {
        InputStream is = s.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bf = new BufferedReader(isr);
        return bf;
    }

    private static void sendToClient(Socket s, String string) throws IOException {
        OutputStream os = s.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.write(string + "\n");
        pw.flush();
    }

    private static String findElement(String id) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("bbdd.txt"));
        String construct;

        while (((construct = br.readLine()) != null)) {
            String arr[] = construct.split("\t");
            String idFile = arr[0];
            if (id.equals(idFile)) {
                break;
            }
        }
        
        if(construct == null){
            return "Element not found.";
        }

        return construct;
    }

    private static boolean deleteElement(String id) throws FileNotFoundException, IOException {
        File inputFile = new File("bbdd.txt");
        File tempFile = new File("tmp.txt");

        boolean r;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile)); 
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            String construct;
            r = false;
            while ((construct = reader.readLine()) != null) {
                String arr[] = construct.split("\t");
                String idFile = arr[0];
                if (id.equals(idFile)) {
                    r = true;
                } else {
                    writer.write(construct + "\n");
                }
            }
            inputFile.delete();
            tempFile.renameTo(inputFile);
        }
        return r;
    }
}
