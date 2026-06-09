package JavaCode;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        // Trzy współdzielone drzewa dla różnych typów kluczy
        BT<Integer, String> intTree = new BT<>();
        BT<Double, String> doubleTree = new BT<>();
        BT<String, String> stringTree = new BT<>();

        System.out.println("Serwer uruchomiony. Oczekiwanie na połączenia na porcie " + PORT + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nowy klient połączony: " + clientSocket.getRemoteSocketAddress());

                // Przekazujemy wszystkie trzy drzewa do handlera
                ClientHandler handler = new ClientHandler(clientSocket, intTree, doubleTree, stringTree);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("Błąd serwera: " + e.getMessage());
        }
    }
}