import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa obsługująca komunikację z pojedynczym klientem w osobnym wątku.
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BT<String, String> tree;

    public ClientHandler(Socket socket, BT<String, String> tree) {
        this.socket = socket;
        this.tree = tree;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;
            // Pętla czyta wiadomości od klienta linia po linii, dopóki połączenie jest otwarte
            while ((inputLine = in.readLine()) != null) {
                String response = handleCommand(inputLine);
                out.println(response); // Odesłanie odpowiedzi do klienta
            }
        } catch (IOException e) {
            System.out.println("Klient rozłączony lub błąd komunikacji: " + socket.getRemoteSocketAddress());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Nie udało się zamknąć gniazda: " + e.getMessage());
            }
        }
    }

    /**
     * Analizuje komendę tekstową od klienta i wywołuje odpowiednią akcję na drzewie.
     */
    private String handleCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "ERROR: Pusta komenda";
        }

        // Podział na maksymalnie 3 części: [KOMENDA] [KLUCZ] [WARTOŚĆ_LUB_RESZTA]
        String[] parts = input.trim().split("\\s+", 3);
        String command = parts[0].toUpperCase();

        try {
            switch (command) {
                case "ADD":
                    if (parts.length < 3) return "ERROR: Składnia: ADD [klucz] [wartość]";
                    tree.insert(parts[1], parts[2]);
                    return "OK: Dodano/Zaktualizowano pomyślnie";

                case "GET":
                    if (parts.length < 2) return "ERROR: Składnia: GET [klucz]";
                    String value = tree.search(parts[1]);
                    return (value != null) ? "VALUE: " + value : "NOT_FOUND: Brak klucza";

                case "DELETE":
                    if (parts.length < 2) return "ERROR: Składnia: DELETE [klucz]";
                    tree.delete(parts[1]);
                    return "OK: Usunięto (jeśli istniał)";

                case "SHOW":
                    // Pobieramy ładne drzewo i zamieniamy prawdziwe entery na znacznik <BR>
                    String structure = tree.toPrettyString().replace("\n", "<BR>");
                    return "TREE: <BR>" + structure;

                default:
                    return "ERROR: Nieznana komenda. Dostępne: ADD, GET, DELETE, SHOW";
            }
        } catch (Exception e) {
            return "ERROR: Błąd podczas wykonywania operacji: " + e.getMessage();
        }
    }
}