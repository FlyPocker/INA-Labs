import java.awt.*;
import java.io.IOException;
import javax.swing.*;

/**
 * Graficzny interfejs użytkownika łączący się z serwerem drzewa binarnego.
 */
public class ClientGUI {
    private final Client client;
    private JFrame frame;
    private JTextField keyField;
    private JTextField valueField;
    private JTextArea outputArea;

    public ClientGUI() {
        this.client = new Client();
        initializeGUI();
        tryConnect();
    }

    private void initializeGUI() {
        frame = new JFrame("Klient Drzewa BT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Panel wprowadzania danych (Góra)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel(" Klucz:"));
        keyField = new JTextField();
        inputPanel.add(keyField);
        inputPanel.add(new JLabel(" Wartość:"));
        valueField = new JTextField();
        inputPanel.add(valueField);

        // Panel przycisków akcji
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("ADD");
        JButton getButton = new JButton("GET");
        JButton deleteButton = new JButton("DELETE");
        JButton showButton = new JButton("SHOW");

        buttonPanel.add(addButton);
        buttonPanel.add(getButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showButton);

        // Połączenie paneli górnych
        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        // Główny obszar tekstowy na logi i strukturę drzewa
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(outputArea);

        frame.add(topContainer, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Przypisanie akcji do przycisków
        addButton.addActionListener(e -> execute("ADD " + keyField.getText() + " " + valueField.getText()));
        getButton.addActionListener(e -> execute("GET " + keyField.getText()));
        deleteButton.addActionListener(e -> execute("DELETE " + keyField.getText()));
        showButton.addActionListener(e -> execute("SHOW"));

        // Rozłączenie z serwerem przy zamknięciu okna krzyżykiem
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                client.disconnect();
            }
        });
    }

    private void tryConnect() {
        try {
            client.connect("localhost", 1234);
            outputArea.append("Połączono pomyślnie z serwerem (localhost:1234)\n\n");
        } catch (IOException e) {
            outputArea.append("BŁĄD: Brak połączenia. Uruchom najpierw Server.java!\n\n");
        }
    }

    private void execute(String command) {
        if (command.trim().isEmpty()) return;
        
        String response = client.sendCommand(command);
        
        // ZAMIANA: Odtwarzamy entery z powrotem z tekstu sieciowego
        response = response.replace("<BR>", "\n");
        
        outputArea.append("Klient: " + command + "\n");
        outputArea.append("Serwer: " + response + "\n\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.frame.setVisible(true);
        });
    }
}