
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Klasa obsługująca połączenie sieciowe TCP z serwerem bazy BT.
 */
public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    /**
     * Nawiązuje połączenie z serwerem o podanym adresie i porcie.
     */
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Wysyła komendę tekstową do serwera i zwraca odebraną odpowiedź.
     */
    public String sendCommand(String command) {
        try {
            out.println(command);
            return in.readLine();
        } catch (IOException e) {
            return "ERROR: Błąd transmisji danych: " + e.getMessage();
        }
    }

    /**
     * Bezpiecznie zamyka strumienie i połączenie z serwerem.
     */
    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.err.println("Błąd podczas rozłączania: " + e.getMessage());
        }
    }
}