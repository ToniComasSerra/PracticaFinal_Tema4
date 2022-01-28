package chat;

import java.net.*;
import java.io.*;
import org.json.JSONObject;

public class ClientWorker implements Runnable {
    private final Socket client;

    public ClientWorker(Socket c) {
        client = c;
    }

    private String writeOnFile(String nameFile1, String nameFile2, Message m) throws IOException {
        File chat = new File(nameFile1 + "_server");
        if (chat.exists()) {
            writeFile(m.getText(), chat);
            return nameFile1 + "_server";
        } else {
            chat = new File(nameFile2 + "_server");
            if (chat.exists()) {
                writeFile(m.getText(), chat);
            } else {
                writeFile(m.getText(), chat);
            }
            return nameFile2 + "_server";
        }
    }

    private void writeFile(String m, File f) throws IOException {
        try (FileWriter fr = new FileWriter(f, true)) {
            fr.write(m + "\n");
        }
    }

    public void sendFile(String file, Socket s) throws IOException {
        try (DataOutputStream dos = new DataOutputStream(s.getOutputStream())) {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            
            int read;
            while ((read = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, read);
            }
            fis.close();
            dos.flush();
        }
    }

    @Override
    public void run() {
        String doc;
        
        try {
            while (true) {
                try (client) {
                    BufferedReader bf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String JsonString = bf.readLine();
                    
                    JSONObject jsonObject = new JSONObject(JsonString);
                    Message m = new Message((String) jsonObject.get("dniOrigin"), (String) jsonObject.get("dniDestiny"), (String) jsonObject.get("text"));
                    
                    doc = writeOnFile(m.getUserOrigin() + m.getUserDestiny(),m.getUserDestiny() + m.getUserOrigin(), m);
                    sendFile(doc, client);
                }
            }
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
