import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ClientGUI {
    private final Client client;
    private JFrame frame;
    private JComboBox<String> typeBox;
    private JTextField keyField;
    private JTextArea outputArea;
    private JPanel treeGraphicPanel;
    private GNode parsedRoot = null;

    public ClientGUI() {
        this.client = new Client();
        initializeGUI();
        tryConnect();
    }

    private void initializeGUI() {
        frame = new JFrame("Klient Drzewa BT");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(950, 650);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 15, 0, 15));
        
        inputPanel.add(new JLabel("Typ klucza:"));
        typeBox = new JComboBox<>(new String[]{"Integer", "Double", "String", "Point"});
        inputPanel.add(typeBox);

        inputPanel.add(new JLabel("Klucz (np. 5 lub 1,2 dla Point):"));
        keyField = new JTextField();
        inputPanel.add(keyField);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("ADD");
        JButton getButton = new JButton("GET");
        JButton deleteButton = new JButton("DELETE");
        JButton showButton = new JButton("SHOW");

        buttonPanel.add(addButton);
        buttonPanel.add(getButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(showButton);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.add(inputPanel, BorderLayout.CENTER);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        outputArea.setBackground(new Color(245, 245, 245));
        JScrollPane logScrollPane = new JScrollPane(outputArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder("Logi serwera"));

        treeGraphicPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (parsedRoot != null) {
                    drawTree(g2, parsedRoot, getWidth() / 2, 40, getWidth() / 4);
                } else {
                    g2.setColor(Color.GRAY);
                    g2.drawString("Drzewo puste lub brak danych. Użyj komendy SHOW.", 20, 30);
                }
            }

            private void drawTree(Graphics2D g, GNode node, int x, int y, int xOffset) {
                if (node == null) return;
                
                int radius = 20; 
                int levelHeight = 60; 

                g.setColor(new Color(150, 150, 150));
                g.setStroke(new BasicStroke(2));      
                
                if (node.left != null) {
                    g.drawLine(x, y, x - xOffset, y + levelHeight);
                    drawTree(g, node.left, x - xOffset, y + levelHeight, xOffset / 2);
                }
                if (node.right != null) {
                    g.drawLine(x, y, x + xOffset, y + levelHeight);
                    drawTree(g, node.right, x + xOffset, y + levelHeight, xOffset / 2);
                }

                g.setColor(new Color(225, 240, 255));
                g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                g.setColor(new Color(41, 128, 185));  
                g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 13));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(node.key, x - fm.stringWidth(node.key) / 2, y + fm.getAscent() / 2 - 2);
            }
        };
        treeGraphicPanel.setBackground(Color.WHITE);
        treeGraphicPanel.setBorder(BorderFactory.createTitledBorder("Wizualizacja (Dynamiczna)"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logScrollPane, treeGraphicPanel);
        splitPane.setDividerLocation(350);
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        frame.add(topContainer, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> executeCommand("ADD " + typeBox.getSelectedItem() + " " + keyField.getText()));
        getButton.addActionListener(e -> executeCommand("GET " + typeBox.getSelectedItem() + " " + keyField.getText()));
        deleteButton.addActionListener(e -> executeCommand("DELETE " + typeBox.getSelectedItem() + " " + keyField.getText()));
        showButton.addActionListener(e -> executeCommand("SHOW " + typeBox.getSelectedItem()));

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
            outputArea.append("BŁĄD: Brak połączenia. Uruchom najpierw serwer!\n\n");
        }
    }

    private void executeCommand(String command) {
        if (command.trim().isEmpty()) return;
        
        String response = client.sendCommand(command);
        
        outputArea.append("Klient: " + command + "\n");
        outputArea.append("Serwer: " + response + "\n\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());

        if (command.trim().toUpperCase().startsWith("SHOW") && response.startsWith("TREE:")) {
            String serializedData = response.substring(5).trim();
            parsedRoot = parseSerializedTree(serializedData);
            treeGraphicPanel.repaint();
        }
    }

    // Zaktualizowany system parsowania oparty na nawiasach kwadratowych
    private GNode parseSerializedTree(String text) {
        if (text == null || text.equals("[]") || text.isEmpty()) return null;
        return parseNode(new StringBuilder(text));
    }

    private GNode parseNode(StringBuilder sb) {
        if (sb.length() == 0 || sb.charAt(0) != '[') return null;
        sb.deleteCharAt(0); // usuń '['
        
        if (sb.charAt(0) == ']') {
            sb.deleteCharAt(0); // usuń ']' (pusty węzeł)
            return null;
        }
        
        // Wyciągamy klucz aż do napotkania nawiasu KWADRATOWEGO. 
        // Okrągłe nawiasy od punktów np. (1,2) zostaną zignorowane jako część klucza.
        int i = 0;
        while (i < sb.length() && sb.charAt(i) != '[' && sb.charAt(i) != ']') {
            i++;
        }
        String key = sb.substring(0, i);
        sb.delete(0, i);
        
        GNode node = new GNode(key);
        node.left = parseNode(sb);
        node.right = parseNode(sb);
        
        if (sb.length() > 0 && sb.charAt(0) == ']') {
            sb.deleteCharAt(0); // usuń kończący ']' dla tego węzła
        }
        
        return node;
    }

    private static class GNode {
        String key;
        GNode left, right;
        GNode(String key) { this.key = key; }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.frame.setVisible(true);
        });
    }
}