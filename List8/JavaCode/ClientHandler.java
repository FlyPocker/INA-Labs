import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final BT<Integer, String> intTree;
    private final BT<Double, String> doubleTree;
    private final BT<String, String> stringTree;
    private final BT<Point2D, String> pointTree; 

    public ClientHandler(Socket socket, BT<Integer, String> intTree, BT<Double, String> doubleTree, BT<String, String> stringTree, BT<Point2D, String> pointTree) {
        this.socket = socket;
        this.intTree = intTree;
        this.doubleTree = doubleTree;
        this.stringTree = stringTree;
        this.pointTree = pointTree;
    }

    @Override
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String response = handleCommand(inputLine);
                out.println(response);
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

    private String handleCommand(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "ERROR: Pusta komenda";
        }

        String[] initialParts = input.trim().split("\\s+", 2);
        String command = initialParts[0].toUpperCase();
        String rest = initialParts.length > 1 ? initialParts[1] : "";

        try {
            switch (command) {
                case "ADD": {
                    String[] parts = rest.split("\\s+", 3);
                    if (parts.length < 3) return "ERROR: Składnia: ADD [typ] [klucz] [wartość]";
                    String type = parts[0].toUpperCase();
                    String keyStr = parts[1];
                    String value = parts[2];

                    if (type.equals("INTEGER")) {
                        intTree.insert(Integer.parseInt(keyStr), value);
                    } else if (type.equals("DOUBLE")) {
                        doubleTree.insert(Double.parseDouble(keyStr), value);
                    } else if (type.equals("POINT")) {
                        String[] coords = keyStr.split(",");
                        if (coords.length != 2) throw new NumberFormatException();
                        Point2D pt = new Point2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        pointTree.insert(pt, value);
                    } else {
                        stringTree.insert(keyStr, value);
                    }
                    return "OK: Dodano/Zaktualizowano pomyślnie na drzewie " + type;
                }

                case "GET": {
                    String[] parts = rest.split("\\s+", 2);
                    if (parts.length < 2) return "ERROR: Składnia: GET [typ] [klucz]";
                    String type = parts[0].toUpperCase();
                    String keyStr = parts[1];

                    String value;
                    if (type.equals("INTEGER")) {
                        value = intTree.search(Integer.parseInt(keyStr));
                    } else if (type.equals("DOUBLE")) {
                        value = doubleTree.search(Double.parseDouble(keyStr));
                    } else if (type.equals("POINT")) {
                        String[] coords = keyStr.split(",");
                        if (coords.length != 2) throw new NumberFormatException();
                        Point2D pt = new Point2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        value = pointTree.search(pt);
                    } else {
                        value = stringTree.search(keyStr);
                    }
                    return (value != null) ? "VALUE: " + value : "NOT_FOUND: Brak klucza na drzewie " + type;
                }

                case "DELETE": {
                    String[] parts = rest.split("\\s+", 2);
                    if (parts.length < 2) return "ERROR: Składnia: DELETE [typ] [klucz]";
                    String type = parts[0].toUpperCase();
                    String keyStr = parts[1];

                    if (type.equals("INTEGER")) {
                        intTree.delete(Integer.parseInt(keyStr));
                    } else if (type.equals("DOUBLE")) {
                        doubleTree.delete(Double.parseDouble(keyStr));
                    } else if (type.equals("POINT")) {
                        String[] coords = keyStr.split(",");
                        if (coords.length != 2) throw new NumberFormatException();
                        Point2D pt = new Point2D(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
                        pointTree.delete(pt);
                    } else {
                        stringTree.delete(keyStr);
                    }
                    return "OK: Usunięto z drzewa " + type + " (jeśli istniał)";
                }

                case "SHOW": {
                    String type = rest.trim().toUpperCase();
                    if (type.isEmpty()) return "ERROR: Składnia: SHOW [typ]";

                    String structure;
                    if (type.equals("INTEGER")) {
                        structure = intTree.toReadableString();
                    } else if (type.equals("DOUBLE")) {
                        structure = doubleTree.toReadableString();
                    } else if (type.equals("POINT")) {
                        structure = pointTree.toReadableString();
                    } else {
                        structure = stringTree.toReadableString();
                    }
                    return "TREE: <BR>" + structure.replace("\n", "<BR>");
                }

                default:
                    return "ERROR: Nieznana komenda. Dostępne: ADD, GET, DELETE, SHOW";
            }
        } catch (NumberFormatException e) {
            return "ERROR: Błąd formatu klucza! Użyj formatu x,y dla POINT.";
        } catch (Exception e) {
            return "ERROR: Błąd podczas operacji: " + e.getMessage();
        }
    }
}