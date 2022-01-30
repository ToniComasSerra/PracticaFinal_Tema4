package chat;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class Cliente {
    public static BufferedReader getFlujo(InputStream is) {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader bfr = new BufferedReader(isr);
        return bfr;
    }

    public static String ask(String in) throws IOException {
        System.out.println(in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }

    private static JSONObject jsonConstruct(String dniEmisor, String dniReceptor, String txt) {
        JSONObject json = new JSONObject();
        json.put("dniOrigin", dniEmisor);
        json.put("dniDestiny", dniReceptor);
        json.put("text", txt);
        return json;
    }

    private static void sendToServerJson(Socket socket, JSONObject json) throws IOException {
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        pw.print(json + "\n");
        pw.flush();
    }

    private static void readFile(String dniOrigin, String dniDestiny) throws FileNotFoundException, IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(dniOrigin + dniDestiny + "_client"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

    private static void saveFile(Socket clientSock, String f) throws IOException {
        DataInputStream dis = new DataInputStream(clientSock.getInputStream());
        FileOutputStream fos = new FileOutputStream(f + "_client");
        byte[] buffer = new byte[4096];
        int filesize = 15123; // Send file size in separate msg
        int read = 0;
        int remaining = filesize;
        read = dis.read(buffer, 0, Math.min(buffer.length, remaining));
        while (read > 0) {
            System.out.println("read " + read + " bytes.");
            fos.write(buffer, 0, read);
            read = dis.read(buffer, 0, Math.min(buffer.length, remaining));
        }
        fos.flush();
    }

    public static void main(String[] args) throws IOException {
        InetSocketAddress direccion = new InetSocketAddress("localhost", 5555);
        JSONObject json;

        String dniOrigin = ask("Indica tu DNI:");
        String dniDestiny = ask("\nIndica el DNI del receptor:");

        while (true) {
            try (Socket socket = new Socket()) {
                socket.connect(direccion);
                String txt = ask("\nEscribe el mensaje a enviar: \n");
                
                Mnsg m = new Mnsg(dniOrigin, dniDestiny, txt);
                json = jsonConstruct(m.getUserOrigin(), m.getUserDestiny(), m.getText());
                sendToServerJson(socket, json);
                saveFile(socket, dniOrigin + dniDestiny);
            }
            System.out.println("\nContenido actual de la conversación: \n");
            System.out.println("---------------------------------------");
            readFile(dniOrigin, dniDestiny);
            System.out.println("--------------------------------------- \n");
        }
    }
}
