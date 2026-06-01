import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Główna klasa serwera sieciowego obsługującego drzewo binarne.
 */
public class Server {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        // Wspólne drzewo dla wszystkich klientów (Klucz: String, Wartość: String)
        BT<String, String> sharedTree = new BT<>();

        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenia na porcie " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Blokuje działanie i czeka, aż klient się połączy
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient połączony: " + clientSocket.getRemoteSocketAddress());

                // Tworzenie i uruchomienie nowego wątku dla klienta
                ClientHandler handler = new ClientHandler(clientSocket, sharedTree);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }
}