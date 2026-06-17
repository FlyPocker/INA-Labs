import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BT<Integer> intTree;
    private final BT<Double> doubleTree;
    private final BT<String> stringTree;
    private final BT<Point2D> pointTree; 

    public ClientHandler(Socket socket, BT<Integer> intTree, BT<Double> doubleTree, BT<String> stringTree, BT<Point2D> pointTree) {
        this.socket = socket;
        this.intTree = intTree;
        this.doubleTree = doubleTree;
        this.stringTree = stringTree;
        this.pointTree = pointTree;
    }

    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                String response = processCommand(inputLine);
                writer.println(response);
            }
        } catch (IOException e) {
            System.out.println("Klient rozłączony: " + socket.getRemoteSocketAddress());
        } finally {
            try {
                if (socket != null && !socket.isClosed()) socket.close();
            } catch (IOException ignored) {}
        }
    }

    private String processCommand(String input) {
        if (input == null || input.trim().isEmpty()) return "ERROR: Pusta komenda";

        String[] arguments = input.trim().split("\\s+", 2);
        String command = arguments[0].toUpperCase();
        String payload = arguments.length > 1 ? arguments[1] : "";

        try {
            switch (command) {
                case "ADD": return handleAdd(payload);
                case "GET": return handleGet(payload);
                case "DELETE": return handleDelete(payload);
                case "SHOW": return handleShow(payload);
                default: return "ERROR: Nieznana komenda. Dostępne: ADD, GET, DELETE, SHOW";
            }
        } catch (NumberFormatException e) {
            return "ERROR: Błąd formatu klucza! Użyj formatu x,y dla POINT.";
        } catch (Exception e) {
            return "ERROR: Operacja nie powiodła się: " + e.getMessage();
        }
    }

    private String handleAdd(String payload) {
        String[] parts = payload.split("\\s+", 2);
        if (parts.length < 2) return "ERROR: Składnia: ADD [typ] [klucz]";
        
        String type = parts[0].toUpperCase();
        String keyStr = parts[1];

        switch (type) {
            case "INTEGER": intTree.insert(Integer.parseInt(keyStr)); break;
            case "DOUBLE": doubleTree.insert(Double.parseDouble(keyStr)); break;
            case "POINT": pointTree.insert(parsePoint(keyStr)); break;
            default: stringTree.insert(keyStr); break;
        }
        return "OK: Dodano do drzewa " + type;
    }

    private String handleGet(String payload) {
        String[] parts = payload.split("\\s+", 2);
        if (parts.length < 2) return "ERROR: Składnia: GET [typ] [klucz]";
        
        String type = parts[0].toUpperCase();
        String keyStr = parts[1];
        boolean found;

        switch (type) {
            case "INTEGER": found = intTree.search(Integer.parseInt(keyStr)); break;
            case "DOUBLE": found = doubleTree.search(Double.parseDouble(keyStr)); break;
            case "POINT": found = pointTree.search(parsePoint(keyStr)); break;
            default: found = stringTree.search(keyStr); break;
        }
        return found ? "FOUND: Klucz " + keyStr + " istnieje." : "NOT_FOUND: Brak klucza.";
    }

    private String handleDelete(String payload) {
        String[] parts = payload.split("\\s+", 2);
        if (parts.length < 2) return "ERROR: Składnia: DELETE [typ] [klucz]";
        
        String type = parts[0].toUpperCase();
        String keyStr = parts[1];

        switch (type) {
            case "INTEGER": intTree.delete(Integer.parseInt(keyStr)); break;
            case "DOUBLE": doubleTree.delete(Double.parseDouble(keyStr)); break;
            case "POINT": pointTree.delete(parsePoint(keyStr)); break;
            default: stringTree.delete(keyStr); break;
        }
        return "OK: Usunięto z drzewa " + type;
    }

    private String handleShow(String payload) {
        String type = payload.trim().toUpperCase();
        if (type.isEmpty()) return "ERROR: Składnia: SHOW [typ]";

        String structure;
        switch (type) {
            case "INTEGER": structure = intTree.serialize(); break;
            case "DOUBLE": structure = doubleTree.serialize(); break;
            case "POINT": structure = pointTree.serialize(); break;
            default: structure = stringTree.serialize(); break;
        }
        return "TREE: " + structure;
    }

    private Point2D parsePoint(String keyStr) {
        String[] coords = keyStr.split(",");
        if (coords.length != 2) throw new NumberFormatException();
        return new Point2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
    }
}