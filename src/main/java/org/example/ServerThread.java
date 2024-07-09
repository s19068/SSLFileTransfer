package org.example;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private Socket pendingSocket;
    private String pendingFileName;

    public ServerThread(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            while (true) {
                pendingSocket = serverSocket.accept();
                DataInputStream dis = new DataInputStream(pendingSocket.getInputStream());
                pendingFileName = dis.readUTF();
                String clientIp = pendingSocket.getInetAddress().getHostAddress();
                System.out.println(clientIp + " chce wysłać plik: " + pendingFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void acceptConnection(String savePath) {
        if (pendingSocket != null) {
            new ServerReceiveThread(pendingSocket, savePath).start();
            pendingSocket = null;
            pendingFileName = null;
        } else {
            System.out.println("Brak oczekujących połączeń.");
        }
    }

    public synchronized void rejectConnection() {
        if (pendingSocket != null) {
            try {
                pendingSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pendingSocket = null;
            pendingFileName = null;
            System.out.println("Połączenie zostało odrzucone.");
        } else {
            System.out.println("Brak oczekujących połączeń.");
        }
    }
}