import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.*;

/**
 * Główne okno symulacji zarządzające siatką wątków i interfejsem graficznym.
 */
public class Simulation extends JFrame {
    private int gridRows = 10; 
    private int gridColumns = 15;
    private final int baseDelayMs = 1000;
    private final double randomChangeProbability = 0.3; 
    
    private Field[][] displayGrid;
    private final JPanel boardPanel;
    private final Random randomGenerator = new Random();
    
    private final Object gridStructureLock = new Object();

    /**
     * Inicjalizuje interfejs graficzny oraz początkową siatkę wątków.
     */
    public Simulation() {
        setTitle("Symulacja Wielowątkowa");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        displayGrid = new Field[gridRows][gridColumns];

        boardPanel = createBoardPanel();
        setupMouseInteractions();
        add(boardPanel, BorderLayout.CENTER);

        JPanel controlPanel = createControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        initializeThreads(0, gridRows, 0, gridColumns);

        Timer renderTimer = new Timer(33, e -> boardPanel.repaint());
        renderTimer.start();
    }

    /**
     * Tworzy panel graficzny odpowiedzialny za renderowanie siatki pól.
     * * @return Skonfigurowany obiekt JPanel
     */
    private JPanel createBoardPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                synchronized (gridStructureLock) {
                    int cellWidth = getWidth() / gridColumns;
                    int cellHeight = getHeight() / gridRows;

                    for (int i = 0; i < gridRows; i++) {
                        for (int j = 0; j < gridColumns; j++) {
                            Field currentField = displayGrid[i][j];
                            if (currentField != null) {
                                g.setColor(currentField.getColorSafely());
                                g.fillRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                                
                                g.setColor(Color.BLACK);
                                g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);

                                // Wizualny znacznik wstrzymania (X)
                                if (currentField.isPaused()) {
                                    g.setColor(Color.WHITE);
                                    g.drawLine(j * cellWidth, i * cellHeight, (j + 1) * cellWidth, (i + 1) * cellHeight);
                                    g.drawLine(j * cellWidth, (i + 1) * cellHeight, (j + 1) * cellWidth, i * cellHeight);
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * Rejestruje nasłuchiwacze kliknięć myszy na siatce, umożliwiając wstrzymywanie pól.
     */
    private void setupMouseInteractions() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                synchronized (gridStructureLock) {
                    int cellWidth = boardPanel.getWidth() / gridColumns;
                    int cellHeight = boardPanel.getHeight() / gridRows;
                    
                    int clickedCol = event.getX() / cellWidth;
                    int clickedRow = event.getY() / cellHeight;

                    if (clickedCol >= 0 && clickedCol < gridColumns && clickedRow >= 0 && clickedRow < gridRows) {
                        Field selectedField = displayGrid[clickedRow][clickedCol];
                        if (selectedField != null) {
                            selectedField.togglePause(); 
                        }
                    }
                }
            }
        });
    }

    /**
     * Tworzy panel dolny z przyciskami do dynamicznego rozszerzania siatki.
     * * @return Skonfigurowany panel sterujący
     */
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        JButton addColumnBtn = new JButton("Dodaj Kolumnę");
        JButton addRowBtn = new JButton("Dodaj Wiersz");

        addColumnBtn.addActionListener(e -> addColumn());
        addRowBtn.addActionListener(e -> addRow());

        panel.add(addColumnBtn);
        panel.add(addRowBtn);
        return panel;
    }

    /**
     * Inicjalizuje obiekty pól i przypisuje im działające wątki w określonych granicach siatki.
     * * @param startRow Początkowy wiersz do inicjalizacji
     * @param endRow Końcowy wiersz do inicjalizacji
     * @param startCol Początkowa kolumna do inicjalizacji
     * @param endCol Końcowa kolumna do inicjalizacji
     */
    private void initializeThreads(int startRow, int endRow, int startCol, int endCol) {
        for (int i = startRow; i < endRow; i++) {
            for (int j = startCol; j < endCol; j++) {
                Field newField = new Field(j, i, randomChangeProbability, baseDelayMs, randomGenerator, this);
                displayGrid[i][j] = newField;
                
                Thread fieldThread = new Thread(newField);
                fieldThread.setName("Watek-" + i + "-" + j);
                newField.setWorkerThread(fieldThread);
                
                fieldThread.start(); 
            }
        }
    }

    /**
     * Oblicza średni kolor aktywnych i niepauzowanych sąsiadów dla podanego pola.
     * Implementuje logikę struktury torusa (zapętlenie krawędzi).
     * * @param targetX Koordynat X pola docelowego
     * @param targetY Koordynat Y pola docelowego
     * @return Średni obiekt Color lub null, gdy brak aktywnych sąsiadów
     */
    public Color calculateAverageColor(int targetX, int targetY) {
        int r = 0, g = 0, b = 0;
        int activeNeighborsCount = 0;
        int[][] cardinalDirections = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};

        synchronized (gridStructureLock) {
            for (int[] direction : cardinalDirections) {
                int neighborX = (targetX + direction[0] + gridColumns) % gridColumns;
                int neighborY = (targetY + direction[1] + gridRows) % gridRows;

                Field neighbor = displayGrid[neighborY][neighborX];

                if (neighbor != null && !neighbor.isPaused()) {
                    Color neighborColor = neighbor.getColorSafely();
                    r += neighborColor.getRed();
                    g += neighborColor.getGreen();
                    b += neighborColor.getBlue();
                    activeNeighborsCount++;
                }
            }
        }

        if (activeNeighborsCount == 0) {
            return null;
        }

        return new Color(r / activeNeighborsCount, g / activeNeighborsCount, b / activeNeighborsCount);
    }

    /**
     * Dynamicznie rozszerza siatkę o jedną kolumnę w prawo, zachowując stan istniejących wątków.
     */
    private void addColumn() {
        synchronized (gridStructureLock) {
            int newColumnsCount = gridColumns + 1;
            Field[][] newGrid = new Field[gridRows][newColumnsCount];

            for (int i = 0; i < gridRows; i++) {
                System.arraycopy(displayGrid[i], 0, newGrid[i], 0, gridColumns);
            }
            
            displayGrid = newGrid;
            initializeThreads(0, gridRows, gridColumns, newColumnsCount);
            gridColumns = newColumnsCount;
        }
    }

    /**
     * Dynamicznie rozszerza siatkę o jeden wiersz w dół, zachowując stan istniejących wątków.
     */
    private void addRow() {
        synchronized (gridStructureLock) {
            int newRowsCount = gridRows + 1;
            Field[][] newGrid = new Field[newRowsCount][gridColumns];

            for (int i = 0; i < gridRows; i++) {
                System.arraycopy(displayGrid[i], 0, newGrid[i], 0, gridColumns);
            }
            
            displayGrid = newGrid;
            initializeThreads(gridRows, newRowsCount, 0, gridColumns);
            gridRows = newRowsCount;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Simulation().setVisible(true);
        });
    }
}