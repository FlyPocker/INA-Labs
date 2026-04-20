import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// W JavaFX klasa główna zawsze dziedziczy po 'Application'
public class Main extends Application {
    
    // Zmienne zachowane z Twojego oryginalnego kodu
    TextField inputField;
    Button generateBtn;
    TextArea textArea;
    Label inputLabel;

    // Metoda start() zastępuje to, co wcześniej robiliśmy w konstruktorze Frame
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Trójkąt Pascala ASCII");

        // BorderPane to odpowiednik BorderLayout z AWT
        BorderPane root = new BorderPane();
        
        // Sposób tworzenia czcionek w FX jest nieco inny
        Font bigFont = Font.font("SansSerif", 24);

        // HBox to kontener układający elementy w poziomie (odpowiednik Panel z FlowLayout)
        // 10 to odstęp (spacing) między elementami
        HBox topPanel = new HBox(10);
        topPanel.setAlignment(Pos.CENTER);
        topPanel.setPadding(new Insets(10)); // Marginesy zewnętrzne panelu

        inputLabel = new Label("Liczba wierszy:");
        inputLabel.setFont(bigFont);
        topPanel.getChildren().add(inputLabel); // W FX dodajemy elementy do "dzieci" kontenera

        inputField = new TextField("10");
        inputField.setPrefColumnCount(5); // Zastępuje parametr wpisywany w konstruktorze AWT
        inputField.setFont(bigFont);
        topPanel.getChildren().add(inputField);

        generateBtn = new Button("Generuj Trójkąt");
        generateBtn.setFont(bigFont);
        topPanel.getChildren().add(generateBtn);

        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));

        // Zmiana nr 1: JavaFX w pełni wspiera wyrażenia Lambda (skrócone zapisy akcji). 
        // Zamiast tworzyć anonimową klasę ActionListener, wystarczy ta jedna krótka linijka:
        generateBtn.setOnAction(e -> actionGenerate());

        root.setTop(topPanel);
        root.setCenter(textArea);

        // Zmiana nr 2: W FX to obiekt Scene określa początkowy rozmiar zawartości (800x600)
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show(); // Odpowiednik setVisible(true) z AWT
    }

    public void actionGenerate() {
        int rows;
        try {
            rows = Integer.parseInt(inputField.getText().trim());
        } catch (NumberFormatException ex) {
            textArea.setText("Proszę podać poprawną liczbę.");
            return;
        }

        if (rows < 1) return;

        // Logika budowania trójkąta pozostaje całkowicie bez zmian!
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows - i; j++) {
                sb.append("   ");
            }
            int number = 1;
            for (int j = 0; j <= i; j++) {
                sb.append(String.format("%-6d", number));
                number = number * (i - j) / (j + 1);
            }
            sb.append("\n");
        }

        String resText = sb.toString();
    
        adjustFontSize(rows);    
        textArea.setText(resText);
    }

    private void adjustFontSize(int rows) {
        int maxLineSize = rows * 6;
        
        // Zmiana nr 3: W FX wymiary są liczbami zmiennoprzecinkowymi (double), a nie int
        double areaWidth = textArea.getWidth() - 40;
        double areaHeight = textArea.getHeight() - 40;

        double refSize = 10.0; 
        Font testFont = Font.font("Monospaced", FontWeight.BOLD, refSize);

        StringBuilder tempLine = new StringBuilder();
        for(int i = 0; i < maxLineSize; i++) tempLine.append("A");

        // Zmiana nr 4: JavaFX NIE MA klasy FontMetrics. 
        // Zamiast tego tworzymy niewidzialny węzeł tekstowy (Text) i sprawdzamy,
        // ile fizycznie zająłby na ekranie z ustawioną testową czcionką.
        Text textNode = new Text(tempLine.toString());
        textNode.setFont(testFont);

        double testWidth = textNode.getLayoutBounds().getWidth();
        double testHeight = textNode.getLayoutBounds().getHeight() * rows;

        if (testWidth == 0 || testHeight == 0) return;

        double propWidth = areaWidth / testWidth;
        double propHeight = areaHeight / testHeight;

        double propMax = Math.min(propWidth, propHeight);

        double targetSize = (refSize * propMax) * 0.95;

        // Aplikujemy docelowy rozmiar upewniając się, że nie ustawimy ujemnej czcionki
        if (targetSize > 1) {
            textArea.setFont(Font.font("Monospaced", FontWeight.BOLD, targetSize));
        }
    }

    // Klasa Main w JavaFX tylko odpala metodę launch()
    public static void Main(String[] args) {
        launch(args);
    }
}