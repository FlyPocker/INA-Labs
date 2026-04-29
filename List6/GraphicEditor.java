import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.Priority;

/**
 * Glowna klasa edytora graficznego
 * 
 * @author Michal Rudzik
 * @version 1.0
 */

public class GraphicEditor extends Application {

    private double startX;
    private double startY;
    /**
     * Enum obslugujacy poszczegolne tryby wybrane przez uzytkownika
     * RECTANGLE,CIRCLE,EDIT
     */
    public enum ToolType {
        RECTANGLE, CIRCLE, POLYGON, EDIT
    }
    private ToolType currentTool = ToolType.RECTANGLE;
    private Shape currentShape, selectedShape;
    private Polygon currentPoly = new Polygon();
    
    
    /**
     * Metoda obslugujaca okienko i zdazenia
    */
    @Override
    public void start(Stage primaryStage) {

        BorderPane mainPane = new BorderPane();
        Pane drawingPane = new Pane();
        drawingPane.setStyle("-fx-background-color: black;");
        
        Rectangle clipRect = new Rectangle();
        // Bindujemy (łączymy na stałe) wymiary maski z wymiarami panelu
        clipRect.widthProperty().bind(drawingPane.widthProperty());
        clipRect.heightProperty().bind(drawingPane.heightProperty());
        // Ustawiamy maskę na nasz panel
        drawingPane.setClip(clipRect);

        HBox toolBar = new HBox(10);
        toolBar.setStyle("-fx-padding: 10px; -fx-background-color: #404040;");
        HBox statusBar = new HBox();
        statusBar.setStyle("-fx-padding: 5px; -fx-background-color: #353535");

        Label currentToolLabel = new Label("Aktywne narzedzie: "+ currentTool.name());
        currentToolLabel.setTextFill(Color.WHITE);
        Button btnRect = new Button("Prostokat");
        btnRect.setOnAction(e -> changeTool(ToolType.RECTANGLE, currentToolLabel));
        Button btnCirc = new Button("Okrag");
        btnCirc.setOnAction(e -> changeTool(ToolType.CIRCLE, currentToolLabel));
        Button btnPoly = new Button("Wielokat");
        btnPoly.setOnAction(e -> changeTool(ToolType.POLYGON, currentToolLabel));
        
        Button btnEdit = new Button("Edycja");
        btnEdit.setOnAction(e -> changeTool(ToolType.EDIT, currentToolLabel));
        Button btnInfo = new Button("Info");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolBar.getChildren().addAll(btnRect, btnCirc, btnPoly, spacer, btnEdit, btnInfo);
        statusBar.getChildren().add(currentToolLabel);
        
        // 1. Zaczynamy rysować
        drawingPane.setOnMousePressed(e -> {
            startX = e.getX();
            startY = e.getY();
            
            if(e.getButton() == MouseButton.PRIMARY){
            switch (currentTool){
                case RECTANGLE:
                    currentShape = new Rectangle(startX, startY, 0, 0);
                    break;
                case CIRCLE:
                    currentShape = new Circle(startX, startY, 0);
                    break;
                case POLYGON:
                    if(currentPoly == null) currentPoly = new Polygon();
                    currentPoly.getPoints().addAll(startX,startY);
                    break;
                case EDIT:
                    if (selectedShape != null){
                        selectedShape.setStroke(Color.WHITE);
                        selectedShape = null;
                    }
                    if (e.getTarget() instanceof Shape){
                        selectedShape = (Shape) e.getTarget();
                        selectedShape.setStroke(Color.RED);
                    }
                    break;
            }}
            if(e.getButton() == MouseButton.SECONDARY){
            switch (currentTool) {
                case POLYGON:
                    currentPoly = null;
                    break;
            }}
            
            if(currentShape != null){
                currentShape.setFill(Color.TRANSPARENT);
                currentShape.setStroke(Color.WHITE); 
                drawingPane.getChildren().add(currentShape);
            }
            if(currentPoly != null){
                currentPoly.setFill(Color.TRANSPARENT);
                currentPoly.setStroke(Color.WHITE);
                drawingPane.getChildren().add(currentPoly);
            }

            
        });

        // 2. Rozciągamy figurę
        drawingPane.setOnMouseDragged(e -> {
            if (currentShape == null & currentTool != ToolType.EDIT) return;

            double currentX = e.getX();
            double currentY = e.getY();

            switch (currentTool) {
                case RECTANGLE:
                    Rectangle rect = (Rectangle) currentShape;
                    rect.setWidth(Math.abs(currentX-startX));
                    rect.setHeight(Math.abs(currentY-startY));
                    rect.setX(Math.min(currentX,startX));
                    rect.setY(Math.min(currentY,startY));
                    break;
                case CIRCLE:
                    Circle circ = (Circle) currentShape;
                    circ.setRadius(Math.sqrt(Math.pow(currentX-startX, 2)+Math.pow(currentY-startY, 2)));
                    break;
                case EDIT:
                    if(selectedShape != null){
                        double offsetX = currentX-startX;
                        double offsetY = currentX-startY;
                    if(e.isPrimaryButtonDown()){
                        offsetX = currentX-startX;
                        offsetY = currentY-startY;
                        selectedShape.setTranslateX(selectedShape.getTranslateX()+offsetX);
                        selectedShape.setTranslateY(selectedShape.getTranslateY()+offsetY);
                        startX = currentX;
                        startY = currentY;
                    }else if(e.isSecondaryButtonDown()){
                        offsetX = currentX-startX;
                        double newAngle = selectedShape.getRotate()+offsetX;

                        if(e.isShiftDown()){
                            double perfectAngle = Math.round(newAngle/45.0)*45.0;
                            if(Math.abs(newAngle-perfectAngle)<5.0) newAngle = perfectAngle;
                        }
                        selectedShape.setRotate(newAngle);
                        startX = currentX;
                    }
                        
                    }
                    break;
            
            }
        });

        drawingPane.setOnScroll(e -> {
            if(currentTool != ToolType.EDIT || selectedShape == null) return;
            double delta = e.getDeltaY() + e.getDeltaX();
            double scrollFactor = 1.1;
            double newScale = selectedShape.getScaleX();
            if(e.isShiftDown()){
                scrollFactor = 1.05;
            }
            if(delta > 0){
                newScale = newScale * scrollFactor;
            }else{
                newScale = newScale / scrollFactor;
            }
            if(newScale > 0.1){
                selectedShape.setScaleX(newScale);
                selectedShape.setScaleY(newScale);
            }
        });

        // 3. Kończymy rysowanie
        drawingPane.setOnMouseReleased(e -> {
            currentShape = null;
        });


        mainPane.setTop(toolBar);
        mainPane.setCenter(drawingPane);

        Scene scene = new Scene(mainPane, 800, 600);
        primaryStage.setTitle("Edytor Figur");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changeTool(ToolType newTool, Label label){
        currentTool = newTool;
        label.setText("Aktywne narzedzie: " + currentTool.name());
        currentShape = null;
        currentPoly = null;
        selectedShape = null;
    }

    public static void Main(String[] args) {
        launch(args);
    }
}