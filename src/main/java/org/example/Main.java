package org.example;

import java.util.Scanner;

public class Main {
    private static final int PORT = 12345;
    private volatile boolean running = true;
    private ServerThread serverThread;

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        serverThread = new ServerThread(PORT);
        serverThread.start();

        while (running) {
            System.out.println("Menu:");
            System.out.println("1. Wyślij");
            System.out.println("2. Akceptuj");
            System.out.println("3. Anuluj");
            System.out.print("Wybierz opcję: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Konsumuje nową linię

            switch (option) {
                case 1:
                    System.out.print("Podaj ścieżkę do pliku: ");
                    String filePath = scanner.nextLine();
                    System.out.print("Podaj adres IP: ");
                    String ip = scanner.nextLine();
                    new ClientSendThread(ip, PORT, filePath).start();
                    break;
                case 2:
                    System.out.print("Podaj ścieżkę do zapisu pliku: ");
                    String savePath = scanner.nextLine();
                    serverThread.acceptConnection(savePath);
                    break;
                case 3:
                    serverThread.rejectConnection();
                    break;
                default:
                    System.out.println("Nieprawidłowa opcja. Spróbuj ponownie.");
            }
        }

        scanner.close();
    }
}