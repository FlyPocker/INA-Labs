import java.awt.*;
import java.io.IOException;
import javax.swing.*;

public class ClientGUI {
    private final Client client;
    private JFrame frame;
    private JComboBox<String> typeBox;
    private JTextField keyField;
    private JTextField valueField;
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
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        
        inputPanel.add(new JLabel(" Typ klucza:"));
        typeBox = new JComboBox<>(new String[]{"Integer", "Double", "String", "Point"});
        inputPanel.add(typeBox);

        inputPanel.add(new JLabel(" Klucz (dla Point x,y):"));
        keyField = new JTextField();
        inputPanel.add(keyField);
        
        inputPanel.add(new JLabel(" Wartość:"));
        valueField = new JTextField();
        inputPanel.add(valueField);

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
        topContainer.add(inputPanel, BorderLayout.NORTH);
        topContainer.add(buttonPanel, BorderLayout.SOUTH);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        JScrollPane logScrollPane = new JScrollPane(outputArea);

        treeGraphicPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (parsedRoot != null) {
                    drawTree(g2, parsedRoot, getWidth() / 2, 40, getWidth() / 4);
                }
            }

            private void drawTree(Graphics2D g, GNode node, int x, int y, int xOffset) {
                if (node == null) return;
                
                int radius = 18; 
                int levelHeight = 50; 

                g.setColor(Color.GRAY);
                g.setStroke(new BasicStroke(2));      
                
                if (node.left != null) {
                    g.drawLine(x, y, x - xOffset, y + levelHeight);
                    drawTree(g, node.left, x - xOffset, y + levelHeight, xOffset / 2);
                }
                if (node.right != null) {
                    g.drawLine(x, y, x + xOffset, y + levelHeight);
                    drawTree(g, node.right, x + xOffset, y + levelHeight, xOffset / 2);
                }

                g.setColor(new Color(230, 245, 255));
                g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
                g.setColor(new Color(70, 130, 180));  
                g.drawOval(x - radius, y - radius, radius * 2, radius * 2);

                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(node.key, x - fm.stringWidth(node.key) / 2, y + fm.getAscent() / 2 - 2);
            }
        };
        treeGraphicPanel.setBackground(Color.WHITE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, logScrollPane, treeGraphicPanel);
        splitPane.setDividerLocation(350);

        frame.add(topContainer, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> execute("ADD " + typeBox.getSelectedItem() + " " + keyField.getText() + " " + valueField.getText()));
        getButton.addActionListener(e -> execute("GET " + typeBox.getSelectedItem() + " " + keyField.getText()));
        deleteButton.addActionListener(e -> execute("DELETE " + typeBox.getSelectedItem() + " " + keyField.getText()));
        showButton.addActionListener(e -> execute("SHOW " + typeBox.getSelectedItem()));

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

    private void execute(String command) {
        if (command.trim().isEmpty()) return;
        
        String response = client.sendCommand(command);
        response = response.replace("<BR>", "\n");
        
        outputArea.append("Klient: " + command + "\n");
        outputArea.append("Serwer: " + response + "\n\n");

        if (command.trim().toUpperCase().startsWith("SHOW") && response.startsWith("TREE:")) {
            String treeText = response.substring(response.indexOf("\n") + 1);
            parsedRoot = parseTreeText(treeText);
            treeGraphicPanel.repaint();
        }
    }

    private GNode parseTreeText(String text) {
        String[] lines = text.split("\n");
        GNode root = null;
        GNode[] lastNodeAtDepth = new GNode[100]; 
        
        for (String line : lines) {
            if (line.trim().isEmpty() || line.contains("Drzewo jest puste")) continue;
            
            int firstBracket = line.indexOf("[");
            int colon = line.indexOf(":", firstBracket);
            if (firstBracket == -1 || colon == -1) continue;
            
            String key = line.substring(firstBracket + 1, colon);
            GNode node = new GNode(key);
            
            int indicatorIdx = Math.max(line.indexOf("L:"), line.indexOf("R:"));
            
            if (indicatorIdx == -1) { 
                root = node;
                lastNodeAtDepth[0] = node;
            } else {
                int depth = indicatorIdx / 4; 
                GNode parent = lastNodeAtDepth[depth - 1];
                
                if (parent != null) {
                    if (line.contains("L:")) parent.left = node;
                    else parent.right = node;
                }
                lastNodeAtDepth[depth] = node;
            }
        }
        return root;
    }

    private static class GNode {
        String key;
        GNode left, right;
        GNode(String key) { this.key = key; }
    }

    public static void main(String[] args) {
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 15));
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 15));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 15));
        UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 15));

        SwingUtilities.invokeLater(() -> {
            ClientGUI gui = new ClientGUI();
            gui.frame.setVisible(true);
        });
    }
}