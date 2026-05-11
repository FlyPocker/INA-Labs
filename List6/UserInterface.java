import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.util.function.Consumer;

/**
 * Klasa odpowiedzialna za budowę interfejsu użytkownika.
 * Tworzy pasek narzędzi, pasek statusu z instrukcjami oraz okno informacyjne.
 */
public class UserInterface {

    /**
     * Buduje górny panel aplikacji zawierający przyciski i podpowiedzi.
     * 
     * @param onToolChange Akcja wywoływana przy zmianie narzędzia
     * @param onSave Akcja zapisu projektu
     * @param onLoad Akcja wczytania projektu
     * @return Gotowy kontener VBox z interfejsem
     */
    public static VBox createTopPanel(Consumer<ToolType> onToolChange, Runnable onSave, Runnable onLoad) {
        
        HBox toolBar = new HBox(10);
        toolBar.setStyle("-fx-padding: 10px; -fx-background-color: #404040;");
        toolBar.setAlignment(Pos.CENTER);
        
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-padding: 5px; -fx-background-color: #303030");

        Label statusLabel = new Label(getToolInstruction(ToolType.RECTANGLE));
        statusLabel.setTextFill(Color.WHITE);
        statusLabel.setStyle("-fx-font-family: 'Verdana'; -fx-font-weight: bold; -fx-font-size: 14px;");
        
        String btnStyle = "-fx-font-size: 14px; -fx-padding: 5px 10px; -fx-cursor: hand;";
        
        Button btnRect = new Button("Prostokat");
        btnRect.setStyle(btnStyle);
        btnRect.setOnAction(e -> {
            onToolChange.accept(ToolType.RECTANGLE);
            statusLabel.setText(getToolInstruction(ToolType.RECTANGLE));
        });
        
        Button btnCirc = new Button("Okrag");
        btnCirc.setStyle(btnStyle);
        btnCirc.setOnAction(e -> {
            onToolChange.accept(ToolType.CIRCLE);
            statusLabel.setText(getToolInstruction(ToolType.CIRCLE));
        });
        
        Button btnPoly = new Button("Wielokat");
        btnPoly.setStyle(btnStyle);
        btnPoly.setOnAction(e -> {
            onToolChange.accept(ToolType.POLYGON);
            statusLabel.setText(getToolInstruction(ToolType.POLYGON));
        });

        Button btnEdit = new Button("Edycja");
        btnEdit.setStyle(btnStyle);
        btnEdit.setOnAction(e -> {
            onToolChange.accept(ToolType.EDIT);
            statusLabel.setText(getToolInstruction(ToolType.EDIT));
        });
        
        Button btnSave = new Button("Zapisz");
        btnSave.setStyle(btnStyle);
        btnSave.setOnAction(e -> onSave.run());

        Button btnLoad = new Button("Wczytaj");
        btnLoad.setStyle(btnStyle);
        btnLoad.setOnAction(e -> onLoad.run());

        Button btnInfo = new Button("Info");
        btnInfo.setStyle(btnStyle);
        btnInfo.setOnAction(e -> showInfoDialog());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolBar.getChildren().addAll(btnSave, btnLoad, btnRect, btnCirc, btnPoly, spacer, btnEdit, btnInfo);
        statusBar.getChildren().add(statusLabel);
        
        VBox topBox = new VBox();
        topBox.getChildren().addAll(toolBar, statusBar);
        
        return topBox;
    }

    /**
     * Wyświetla natywne okno dialogowe z informacjami o programie i autorze.
     */
    private static void showInfoDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("O programie");
        alert.setHeaderText("Edytor Graficzny");
        alert.setContentText("Autor: Michal Rudzik\n\n"
                + "Przeznaczenie: Aplikacja do rysowania i edycji prostych figur wektorowych.\n\n"
                + "Instrukcja:\n"
                + "- Wybierz narzedzie z gornego paska.\n"
                + "- Pasek pod narzedziami na biezaco wyswietla instrukcje do wybranego narzedzia.\n"
                + "- Zapis i odczyt odbywa sie we wlasnym formacie tekstowym.");
        alert.showAndWait();
    }

    /**
     * Zwraca tekstową instrukcję obsługi dla wybranego narzędzia.
     * 
     * @param tool Typ aktywnego narzędzia
     * @return String z podpowiedzią dla użytkownika
     */
    private static String getToolInstruction(ToolType tool) {
        switch (tool) {
            case RECTANGLE: return "Narzędzie: PROSTOKĄT | LPM: przeciągnij żeby narysować";
            case CIRCLE: return "Narzędzie: OKRĄG | LPM: przeciągnij żeby narysować";
            case POLYGON: return "Narzędzie: WIELOKĄT | LPM: dodaj punkt | PPM: nowa figura";
            case EDIT: return "Narzędzie: EDYCJA | LPM: przesuń | PPM: kolory | SCROLL: skaluj | R+PPM: obrót | DEL: usuń";
            default: return "";
        }
    }
}