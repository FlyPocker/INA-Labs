import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.function.Consumer;
import javafx.scene.Cursor;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Glowna klasa edytora graficznego.
 * Obsluguje interfejs uzytkownika, rysowanie figur wektorowych
 * oraz zapis i odczyt projektow.
 * 
 * @author Michal Rudzik
 * @version 1.1
 */
public class GraphicEditor extends Application {

    private double startX;
    private double startY;
    
    public enum ToolType {
        RECTANGLE, CIRCLE, POLYGON, EDIT
    }
    
    private ToolType currentTool = ToolType.RECTANGLE;
    private Shape currentShape, selectedShape;
    private Polygon currentPoly = new Polygon();
    private Rectangle selectionMarker;
    private ContextMenu colorMenuPopup;
    
    private Color selectedStrokeColor = Color.WHITE;
    private Color selectedFillColor = Color.TRANSPARENT;
    
    private Pane drawingPane;
    private Rectangle clipRect;
    private boolean isRPressed = false;

    /**
     * Punkt startowy aplikacji JavaFX.
     * Inicjalizuje glowne okno, pasek narzedzi oraz obsluguje zdarzenia myszy i klawiatury.
     * 
     * @param primaryStage Glowne okno aplikacji przekazane przez srodowisko JavaFX
     */
    @Override
    public void start(Stage primaryStage) {

        // --- 1. INICJALIZACJA GŁÓWNYCH KONTROLEK UI ---
        
        BorderPane mainPane = new BorderPane();
        drawingPane = new Pane();
        drawingPane.setStyle("-fx-background-color: black;");
        
        clipRect = new Rectangle();
        clipRect.setId("clipMask"); 
        clipRect.widthProperty().bind(drawingPane.widthProperty());
        clipRect.heightProperty().bind(drawingPane.heightProperty());
        drawingPane.setClip(clipRect);

        HBox toolBar = new HBox(10);
        toolBar.setStyle("-fx-padding: 10px; -fx-background-color: #404040;");
        toolBar.setAlignment(Pos.CENTER);
        
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-padding: 5px; -fx-background-color: #303030");

        String btnStyle = "-fx-font-size: 14px; -fx-padding: 5px 10px; -fx-cursor: hand;";
        Label currentToolLabel = new Label("Aktywne narzedzie: "+ currentTool.name());
        currentToolLabel.setTextFill(Color.WHITE);
        currentToolLabel.setStyle("-fx-font-family: 'Verdana'; -fx-font-weight: bold; -fx-font-size: 14px;");
        
        // --- 2. TWORZENIE PRZYCISKÓW ---
        
        Button btnRect = new Button("Prostokat");
        btnRect.setOnAction(e -> changeTool(ToolType.RECTANGLE, currentToolLabel));
        btnRect.setStyle(btnStyle);
        
        Button btnCirc = new Button("Okrag");
        btnCirc.setOnAction(e -> changeTool(ToolType.CIRCLE, currentToolLabel));
        btnCirc.setStyle(btnStyle);
        
        Button btnPoly = new Button("Wielokat");
        btnPoly.setOnAction(e -> changeTool(ToolType.POLYGON, currentToolLabel));
        btnPoly.setStyle(btnStyle);

        Button btnEdit = new Button("Edycja");
        btnEdit.setOnAction(e -> changeTool(ToolType.EDIT, currentToolLabel));
        btnEdit.setStyle(btnStyle);
        
        Button btnSave = new Button("Zapisz");
        btnSave.setStyle(btnStyle);
        btnSave.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki Edytora", "*.txt"));
            File file = fc.showSaveDialog(primaryStage);
            if(file != null) saveToFile(file);
        });

        Button btnLoad = new Button("Wczytaj");
        btnLoad.setStyle(btnStyle);
        btnLoad.setOnAction(e -> {
            FileChooser fc = new FileChooser();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Pliki Edytora", "*.txt"));
            File file = fc.showOpenDialog(primaryStage);
            if(file != null) loadFromFile(file);
        });

        Button btnInfo = new Button("Info");
        btnInfo.setOnAction(e -> hideStatusBar(statusBar));
        btnInfo.setStyle(btnStyle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolBar.getChildren().addAll(btnSave, btnLoad, btnRect, btnCirc, btnPoly, spacer, btnEdit, btnInfo);
        statusBar.getChildren().add(currentToolLabel);
        
        VBox topBox = new VBox();
        topBox.getChildren().addAll(toolBar, statusBar);
        
        // --- 3. OBSŁUGA MYSZY (RYSOWANIE I EDYCJA) ---
        
        drawingPane.setOnMousePressed(e -> {
            if (colorMenuPopup != null && colorMenuPopup.isShowing()) {
                colorMenuPopup.hide();
            }
            
            startX = e.getX();
            startY = e.getY();
            
            if (e.getButton() == MouseButton.PRIMARY) {
                switch (currentTool) {
                    case RECTANGLE:
                        currentShape = new Rectangle(startX, startY, 0, 0);
                        break;
                    case CIRCLE:
                        currentShape = new Circle(startX, startY, 0);
                        break;
                    case POLYGON:
                        if(currentPoly == null) currentPoly = new Polygon();
                        currentPoly.getPoints().addAll(startX, startY);
                        break;
                    case EDIT:
                        if (e.getTarget() == drawingPane || e.getTarget() == clipRect) {
                            if (selectionMarker != null) {
                                drawingPane.getChildren().remove(selectionMarker);
                                deleteSelection(selectionMarker);
                                selectedShape = null;
                                selectionMarker = null;
                            }
                        } else if (e.getTarget() instanceof Shape) {
                            if (selectedShape != e.getTarget()) {
                                if (selectionMarker != null) {
                                    drawingPane.getChildren().remove(selectionMarker);
                                    deleteSelection(selectionMarker);
                                }
                                selectedShape = (Shape) e.getTarget();
                                selectionMarker = createSelection(selectedShape);
                                drawingPane.getChildren().add(selectionMarker);
                            }
                        }
                        break;
                }
            }
            
            if (e.getButton() == MouseButton.SECONDARY) {
                if (currentTool == ToolType.EDIT && selectedShape != null && e.getTarget() == selectedShape) {
                    HBox strokeBox = createPicker("Stroke:", (Color) selectedShape.getStroke(), color -> selectedShape.setStroke(color));
                    HBox fillBox = createPicker("Fill:", (Color) selectedShape.getFill(), color -> selectedShape.setFill(color));
                    
                    VBox menuContent = new VBox(5, strokeBox, fillBox);
                    CustomMenuItem colorMenu = new CustomMenuItem(menuContent, false); 
                    
                    colorMenuPopup = new ContextMenu(colorMenu);
                    colorMenuPopup.show(drawingPane, e.getScreenX(), e.getScreenY());
                    
                } else if (currentTool == ToolType.POLYGON) {
                    currentPoly = null;
                }
            }
            
            if (currentShape != null && currentTool != ToolType.EDIT) {
                currentShape.setFill(selectedFillColor);
                currentShape.setStroke(selectedStrokeColor); 
                drawingPane.getChildren().add(currentShape);
            }
            
            if (currentPoly != null && currentTool == ToolType.POLYGON && currentPoly.getParent() == null) {
                currentPoly.setFill(selectedFillColor);
                currentPoly.setStroke(selectedStrokeColor);
                drawingPane.getChildren().add(currentPoly);
            }
        });

        drawingPane.setOnMouseDragged(e -> {
            if (currentShape == null && currentTool != ToolType.EDIT) return;

            double currentX = e.getX();
            double currentY = e.getY();

            switch (currentTool) {
                case RECTANGLE:
                    Rectangle rect = (Rectangle) currentShape;
                    rect.setWidth(Math.abs(currentX - startX));
                    rect.setHeight(Math.abs(currentY - startY));
                    rect.setX(Math.min(currentX, startX));
                    rect.setY(Math.min(currentY, startY));
                    break;
                case CIRCLE:
                    Circle circ = (Circle) currentShape;
                    circ.setRadius(Math.sqrt(Math.pow(currentX - startX, 2) + Math.pow(currentY - startY, 2)));
                    break;
                case EDIT:
                    if (selectedShape != null) {
                        if (isRPressed) {
                            double offsetX = currentX - startX;
                            double newAngle = selectedShape.getRotate() + offsetX;
                            if(e.isShiftDown()){
                                double perfectAngle = Math.round(newAngle / 45.0) * 45.0;
                                if(Math.abs(newAngle - perfectAngle) < 5.0) newAngle = perfectAngle;
                            }
                            selectedShape.setRotate(newAngle);
                            startX = currentX;
                        } 
                        else if (e.isPrimaryButtonDown()) {
                            double offsetX = currentX - startX;
                            double offsetY = currentY - startY;
                            selectedShape.setTranslateX(selectedShape.getTranslateX() + offsetX);
                            selectedShape.setTranslateY(selectedShape.getTranslateY() + offsetY);
                            startX = currentX;
                            startY = currentY;
                        }
                    }
                    break;
            }
        });

        drawingPane.setOnScroll(e -> {
            if(currentTool != ToolType.EDIT || selectedShape == null) return;
            double delta = e.getDeltaY() + e.getDeltaX();
            double scrollFactor = e.isShiftDown() ? 1.05 : 1.1;
            double newScale = selectedShape.getScaleX();
            
            newScale = (delta > 0) ? newScale * scrollFactor : newScale / scrollFactor;
            
            if(newScale > 0.1){
                selectedShape.setScaleX(newScale);
                selectedShape.setScaleY(newScale);
            }
        });

        drawingPane.setOnMouseReleased(e -> {
            if (currentTool != ToolType.POLYGON) {
                currentShape = null;
            }
        });

        // --- 4. KONFIGURACJA SCENY I SKRÓTÓW KLAWISZOWYCH ---

        mainPane.setTop(topBox);
        mainPane.setCenter(drawingPane);
        Scene scene = new Scene(mainPane, 1000, 700);
        
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.R && currentTool == ToolType.EDIT) {
                isRPressed = !isRPressed;
                scene.setCursor(isRPressed ? Cursor.H_RESIZE : Cursor.DEFAULT);
            }
        });

        primaryStage.setTitle("Edytor Figur");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Zmienia aktywne narzedzie edytora, odznaczajac biezace figury i zamykajac menu.
     * 
     * @param newTool Nowe narzedzie do ustawienia (z enum ToolType)
     * @param label Etykieta paska statusu do zaktualizowania
     */
    private void changeTool(ToolType newTool, Label label){
        currentTool = newTool;
        if (colorMenuPopup != null) colorMenuPopup.hide();
        label.setText("Aktywne narzedzie: " + currentTool.name());
        currentShape = null;
        currentPoly = null;
        isRPressed = false;
        
        if (drawingPane.getScene() != null) {
            drawingPane.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Przelacza widocznosc paska statusu na dole ekranu.
     * 
     * @param bar Kontener (HBox) reprezentujacy pasek statusu
     */
    private void hideStatusBar(HBox bar){
        boolean isVisible = bar.isVisible();
        bar.setVisible(!isVisible);
        bar.setManaged(!isVisible);
    }
    
    /**
     * Tworzy wizualny znacznik (ramke) otaczajacy wybrana figure i binduje jego transformacje.
     * 
     * @param selectedShape Figura, ktora zostala kliknieta przez uzytkownika
     * @return Skonfigurowany obiekt Rectangle sluzacy jako ramka zaznaczenia
     */
    private Rectangle createSelection(Shape selectedShape){
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
     * Rozrywa powiazania (bindowanie) miedzy markerem a figura przed jego usunieciem z ekranu.
     * 
     * @param marker Ramka zaznaczenia do odpiecia
     */
    private void deleteSelection(Rectangle marker){
        marker.translateXProperty().unbind();
        marker.translateYProperty().unbind();
        marker.scaleXProperty().unbind();
        marker.scaleYProperty().unbind();
        marker.rotateProperty().unbind();
    }

    /**
     * Tworzy komponent ColorPicker z etykieta, gotowy do umieszczenia w menu kontekstowym.
     * 
     * @param labelText Tekst wyswietlany obok pickera (np. "Stroke:")
     * @param initialColor Domyslny kolor ustawiany przy otwarciu
     * @param onColorChanged Akcja do wykonania (Consumer) po wybraniu nowego koloru
     * @return Skonfigurowany kontener HBox z etykieta i pickerem
     */
    private HBox createPicker(String labelText, Color initialColor, Consumer<Color> onColorChanged) {
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
    
    // ==========================================
    // SEKCJA ZAPISU I ODCZYTU PLIKÓW
    // ==========================================

    /**
     * Zapisuje wszystkie narysowane figury wektorowe do pliku tekstowego (format wlasny CSV).
     * 
     * @param file Plik docelowy wybrany przez uzytkownika
     */
    private void saveToFile(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (var node : drawingPane.getChildren()) {
                if (node instanceof Shape && node != selectionMarker && !"clipMask".equals(node.getId())) {
                    Shape s = (Shape) node;
                    String fill = s.getFill().toString();
                    String stroke = s.getStroke().toString();
                    double tx = s.getTranslateX(), ty = s.getTranslateY();
                    double rot = s.getRotate(), scale = s.getScaleX();

                    if (s instanceof Rectangle) {
                        Rectangle r = (Rectangle) s;
                        writer.println("RECTANGLE;" + fill + ";" + stroke + ";" + tx + ";" + ty + ";" + rot + ";" + scale + ";" 
                                + r.getX() + ";" + r.getY() + ";" + r.getWidth() + ";" + r.getHeight());
                                
                    } else if (s instanceof Circle) {
                        Circle c = (Circle) s;
                        writer.println("CIRCLE;" + fill + ";" + stroke + ";" + tx + ";" + ty + ";" + rot + ";" + scale + ";" 
                                + c.getCenterX() + ";" + c.getCenterY() + ";" + c.getRadius());
                                
                    } else if (s instanceof Polygon) {
                        Polygon p = (Polygon) s;
                        StringBuilder points = new StringBuilder();
                        for (Double point : p.getPoints()) {
                            points.append(point).append(",");
                        }
                        writer.println("POLYGON;" + fill + ";" + stroke + ";" + tx + ";" + ty + ";" + rot + ";" + scale + ";" + points.toString());
                    }
                }
            }
            System.out.println("Zapisano do pliku!");
        } catch (Exception e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }
    }

    /**
     * Wczytuje i odtwarza figury wektorowe z pliku tekstowego na plansze rysowania.
     * 
     * @param file Plik zrodlowy wskazany przez uzytkownika
     */
    private void loadFromFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            drawingPane.getChildren().clear();
            
            clipRect = new Rectangle();
            clipRect.setId("clipMask");
            clipRect.widthProperty().bind(drawingPane.widthProperty());
            clipRect.heightProperty().bind(drawingPane.heightProperty());
            drawingPane.setClip(clipRect);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");
                if (parts.length < 8) continue;

                String type = parts[0];
                Color fill = Color.valueOf(parts[1]);
                Color stroke = Color.valueOf(parts[2]);
                double tx = Double.parseDouble(parts[3]);
                double ty = Double.parseDouble(parts[4]);
                double rot = Double.parseDouble(parts[5]);
                double scale = Double.parseDouble(parts[6]);

                Shape shape = null;

                if (type.equals("RECTANGLE")) {
                    shape = new Rectangle(Double.parseDouble(parts[7]), Double.parseDouble(parts[8]),
                            Double.parseDouble(parts[9]), Double.parseDouble(parts[10]));
                            
                } else if (type.equals("CIRCLE")) {
                    shape = new Circle(Double.parseDouble(parts[7]), Double.parseDouble(parts[8]), Double.parseDouble(parts[9]));
                    
                } else if (type.equals("POLYGON")) {
                    Polygon p = new Polygon();
                    String[] pts = parts[7].split(",");
                    for (String pt : pts) {
                        if (!pt.isEmpty()) p.getPoints().add(Double.parseDouble(pt));
                    }
                    shape = p;
                }

                if (shape != null) {
                    shape.setFill(fill);
                    shape.setStroke(stroke);
                    shape.setTranslateX(tx);
                    shape.setTranslateY(ty);
                    shape.setRotate(rot);
                    shape.setScaleX(scale);
                    shape.setScaleY(scale);
                    drawingPane.getChildren().add(shape);
                }
            }
            System.out.println("Wczytano z pliku!");
        } catch (Exception e) {
            System.out.println("Błąd odczytu: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}