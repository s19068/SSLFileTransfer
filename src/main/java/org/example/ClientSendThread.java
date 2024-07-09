package org.example;

import java.io.*;
import java.net.Socket;

class ClientSendThread extends Thread {
    private String serverAddress;
    private int port;
    private String filePath;

    public ClientSendThread(String serverAddress, int port, String filePath) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.filePath = filePath;
    }

    public void run() {
        try (Socket socket = new Socket(serverAddress, port);
             FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis);
             OutputStream os = socket.getOutputStream();
             DataOutputStream dos = new DataOutputStream(os)) {

            File file = new File(filePath);
            dos.writeUTF(file.getName());

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            System.out.println("Plik " + filePath + " został wysłany do " + serverAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

