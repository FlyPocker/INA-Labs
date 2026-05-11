import java.io.File;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Główna klasa edytora graficznego.
 * Łączy wszystkie moduły (UI, logikę, obsługę plików) w działającą aplikację.
 */
public class GraphicEditor extends Application {

    private Pane drawingPane;
    private Rectangle clipRect;
    private DrawingController drawingController;

    /**
     * Punkt startowy aplikacji JavaFX.
     * Inicjalizuje okno i łączy poszczególne komponenty edytora.
     * 
     * @param primaryStage Główne okno aplikacji
     */
    @Override
    public void start(Stage primaryStage) {
        BorderPane mainPane = new BorderPane();
        
        // --- KONFIGURACJA PLANSZY ---
        drawingPane = new Pane();
        drawingPane.setStyle("-fx-background-color: black;");
        
        clipRect = new Rectangle();
        clipRect.setId("clipMask"); 
        clipRect.widthProperty().bind(drawingPane.widthProperty());
        clipRect.heightProperty().bind(drawingPane.heightProperty());
        drawingPane.setClip(clipRect);

        // --- INICJALIZACJA LOGIKI RYSOWANIA ---
        drawingController = new DrawingController(drawingPane, clipRect);

        // --- TWORZENIE INTERFEJSU (z podpięciem akcji) ---
        VBox topPanel = UserInterface.createTopPanel(
            tool -> drawingController.setCurrentTool(tool),
            () -> handleSave(primaryStage),
            () -> handleLoad(primaryStage)
        );

        mainPane.setTop(topPanel);
        mainPane.setCenter(drawingPane);
        
        Scene scene = new Scene(mainPane, 1000, 700);
        drawingController.setupKeyHandlers(scene);

        primaryStage.setTitle("Edytor Figur");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Wywołuje okno zapisu i przekazuje dane do FileManager.
     */
    private void handleSave(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki Edytora", "*.txt"));
        File file = fc.showSaveDialog(stage);
        if (file != null) {
            FileManager.saveToFile(file, drawingPane, drawingController.getSelectionMarker());
        }
    }

    /**
     * Wywołuje okno odczytu i przekazuje plik do FileManager.
     */
    private void handleLoad(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki Edytora", "*.txt"));
        File file = fc.showOpenDialog(stage);
        if (file != null) {
            FileManager.loadFromFile(file, drawingPane);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}