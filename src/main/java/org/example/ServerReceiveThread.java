package org.example;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ServerReceiveThread extends Thread {
    private Socket socket;
    private String savePath;

    public ServerReceiveThread(Socket socket, String savePath) {
        this.socket = socket;
        this.savePath = savePath;
    }

    public void run() {
        try (InputStream is = socket.getInputStream();
             FileOutputStream fos = new FileOutputStream(savePath);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            System.out.println("Plik został zapisany w: " + savePath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
