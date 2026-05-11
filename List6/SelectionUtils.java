import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Klasa narzędziowa do obsługi interfejsu zaznaczania figur.
 * Zawiera metody do tworzenia ramki wyboru oraz elementów menu kontekstowego.
 */
public class SelectionUtils {

    /**
     * Tworzy przerywaną ramkę otaczającą wybraną figurę i wiąże jej transformacje.
     * 
     * @param selectedShape Figura, dla której tworzymy ramkę
     * @return Skonfigurowany prostokąt pełniący rolę markera zaznaczenia
     */
    public static Rectangle createSelection(Shape selectedShape) {
        Rectangle marker = new Rectangle();
        marker.setFill(Color.TRANSPARENT);
        marker.setStroke(Color.AQUA);
        marker.getStrokeDashArray().addAll(5.0, 5.0);

        marker.setX(selectedShape.getBoundsInLocal().getMinX());
        marker.setY(selectedShape.getBoundsInLocal().getMinY());
        marker.setWidth(selectedShape.getBoundsInLocal().getWidth());
        marker.setHeight(selectedShape.getBoundsInLocal().getHeight());

        marker.translateXProperty().bind(selectedShape.translateXProperty());
        marker.translateYProperty().bind(selectedShape.translateYProperty());
        marker.scaleXProperty().bind(selectedShape.scaleXProperty());
        marker.scaleYProperty().bind(selectedShape.scaleYProperty());
        marker.rotateProperty().bind(selectedShape.rotateProperty());
        
        marker.setMouseTransparent(true);
        return marker;
    }

    /**
     * Usuwa powiązania między markerem a figurą przed jego usunięciem.
     * 
     * @param marker Ramka zaznaczenia do odpięcia
     */
    public static void deleteSelection(Rectangle marker) {
        marker.translateXProperty().unbind();
        marker.translateYProperty().unbind();
        marker.scaleXProperty().unbind();
        marker.scaleYProperty().unbind();
        marker.rotateProperty().unbind();
    }

    /**
     * Tworzy komponent wyboru koloru z etykietą, gotowy do wstawienia do menu.
     * 
     * @param labelText Etykieta
     * @param initialColor Początkowy kolor pickera
     * @param onColorChanged Akcja wywoływana automatycznie po zmianie koloru
     * @return Kontener HBox z etykietą i wybieraniem koloru
     */
    public static HBox createPicker(String labelText, Color initialColor, Consumer<Color> onColorChanged) {
        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: black; -fx-font-weight: bold;");

        ColorPicker picker = new ColorPicker(initialColor);
        picker.setStyle("-fx-color-label-visible: false;");
        picker.setOnAction(e -> onColorChanged.accept(picker.getValue()));

        HBox box = new HBox(10, label, picker);
        box.setStyle("-fx-padding: 5px; -fx-background-color: #f0f0f0;");
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }
}