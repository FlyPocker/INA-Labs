import java.awt.Color;
import java.util.Random;

/**
 * Klasa reprezentująca pojedyncze pole symulacji.
 * Działa jako niezależny wątek, aktualizując swój kolor losowo lub na podstawie sąsiadów.
 */
public class Field implements Runnable {
    private Color currentColor;
    private final int gridX;
    private final int gridY;
    private final double randomChangeProbability;
    private final int baseDelayMs;
    private final Random randomGenerator;
    private final Simulation simulationWindow;

    private boolean isRunning = true;
    private boolean isPaused = false;
    private final Object pauseLock = new Object();
    private final Object colorLock = new Object();
    
    private Thread workerThread;

    /**
     * Inicjalizuje nowe pole siatki z losowym kolorem początkowym.
     * * @param gridX Koordynata X na siatce
     * @param gridY Koordynata Y na siatce
     * @param randomChangeProbability Prawdopodobieństwo zmiany koloru na całkowicie losowy
     * @param baseDelayMs Bazowy czas opóźnienia w milisekundach
     * @param randomGenerator Współdzielony generator liczb pseudolosowych
     * @param simulationWindow Referencja do głównego okna symulacji
     */
    public Field(int gridX, int gridY, double randomChangeProbability, int baseDelayMs, 
                 Random randomGenerator, Simulation simulationWindow) {
        this.gridX = gridX;
        this.gridY = gridY;
        this.randomChangeProbability = randomChangeProbability;
        this.baseDelayMs = baseDelayMs;
        this.randomGenerator = randomGenerator;
        this.simulationWindow = simulationWindow;
        
        this.currentColor = new Color(
            randomGenerator.nextInt(256), 
            randomGenerator.nextInt(256), 
            randomGenerator.nextInt(256)
        );
    }

    /**
     * Przypisuje instancję wątku obsługującego to pole, umożliwiając jego przerwanie.
     * * @param thread Wątek przypisany do działania tego pola
     */
    public void setWorkerThread(Thread thread) {
        this.workerThread = thread;
    }

    /**
     * Przełącza stan wstrzymania wątku.
     * W przypadku wstrzymywania, natychmiast przerywa trwające usypianie wątku.
     */
    public void togglePause() {
        synchronized (pauseLock) {
            isPaused = !isPaused;
            if (isPaused) {
                if (workerThread != null) {
                    workerThread.interrupt();
                }
            } else {
                pauseLock.notifyAll();
            }
        }
    }

    /**
     * Zwraca aktualny stan wstrzymania wątku.
     * * @return Prawda, jeśli pole jest aktualnie wstrzymane
     */
    public boolean isPaused() {
        synchronized (pauseLock) {
            return isPaused;
        }
    }

    /**
     * Bezpiecznie odczytuje aktualny kolor pola, wykorzystując semafor blokujący.
     * * @return Aktualny kolor pola
     */
    public Color getColorSafely() {
        synchronized (colorLock) {
            return this.currentColor;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                synchronized (pauseLock) {
                    while (isPaused) {
                        pauseLock.wait();
                    }
                }

                int currentDelay = (int) (baseDelayMs * 0.5 + randomGenerator.nextDouble() * baseDelayMs);
                Thread.sleep(currentDelay);

                if (randomGenerator.nextDouble() <= randomChangeProbability) {
                    changeToRandomColor();
                } else {
                    updateColorFromNeighbors();
                }
            } catch (InterruptedException e) {
                // Wątek wybudzony celowo (np. przez togglePause).
                // Pętla kontynuuje działanie i w razie potrzeby zatrzyma się na pauseLock.wait().
            }
        }
    }

    /**
     * Zmienia kolor pola na losowy z zachowaniem synchronizacji.
     */
    private void changeToRandomColor() {
        synchronized (colorLock) { 
            this.currentColor = new Color(
                randomGenerator.nextInt(256), 
                randomGenerator.nextInt(256), 
                randomGenerator.nextInt(256)
            );
        }
    }

    /**
     * Aktualizuje kolor pola na podstawie średniej wartości kolorów aktywnych sąsiadów.
     */
    private void updateColorFromNeighbors() {
        Color averageColor = simulationWindow.calculateAverageColor(this.gridX, this.gridY);
        
        if (averageColor != null) {
            synchronized (colorLock) {
                this.currentColor = averageColor;
            }
        }
    }
}