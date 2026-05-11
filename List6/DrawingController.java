import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Kontroler odpowiadający za logikę rysowania i edycji.
 * Zarządza stanem aplikacji, obsługą zdarzeń myszy i skrótów klawiszowych.
 */
public class DrawingController {

    private double startX;
    private double startY;
    
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
     * Inicjalizuje kontroler i przypina zdarzenia do planszy.
     * 
     * @param drawingPane Główny panel, po którym rysujemy
     * @param clipRect Maska przycinająca planszy
     */
    public DrawingController(Pane drawingPane, Rectangle clipRect) {
        this.drawingPane = drawingPane;
        this.clipRect = clipRect;
        setupMouseHandlers();
    }

    /**
     * Zmienia aktualne narzędzie i resetuje tymczasowy stan zaznaczenia.
     * 
     * @param newTool Nowe narzędzie wybrane z interfejsu
     */
    public void setCurrentTool(ToolType newTool) {
        currentTool = newTool;
        if (colorMenuPopup != null) colorMenuPopup.hide();
        currentShape = null;
        currentPoly = null;
        isRPressed = false;
        
        if (drawingPane.getScene() != null) {
            drawingPane.getScene().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * Zwraca aktualną ramkę zaznaczenia (przydatne do ominięcia przy zapisie pliku).
     */
    public Rectangle getSelectionMarker() {
        return selectionMarker;
    }

    /**
     * Konfiguruje nasłuchiwanie klawiszy na głównej scenie.
     * Obsługuje obracanie (klawisz R) oraz usuwanie figur (klawisz DELETE).
     */
    public void setupKeyHandlers(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (currentTool == ToolType.EDIT) {
                // Obsługa obracania
                if (e.getCode() == KeyCode.R) {
                    isRPressed = !isRPressed;
                    scene.setCursor(isRPressed ? Cursor.H_RESIZE : Cursor.DEFAULT);
                } 
                // Obsługa usuwania
                else if (e.getCode() == KeyCode.DELETE) {
                    if (selectedShape != null) {
                        drawingPane.getChildren().remove(selectedShape); 
                        if (selectionMarker != null) {
                            drawingPane.getChildren().remove(selectionMarker);
                            SelectionUtils.deleteSelection(selectionMarker);
                        }
                        selectedShape = null;
                        selectionMarker = null;
                        isRPressed = false;
                        scene.setCursor(Cursor.DEFAULT);
                    }
                }
            }
        });
    }

    /**
     * Główna logika obsługi kliknięć, przeciągania i scrollowania myszką.
     */
    private void setupMouseHandlers() {
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
                        if (currentPoly == null) currentPoly = new Polygon();
                        currentPoly.getPoints().addAll(startX, startY);
                        break;
                    case EDIT:
                        if (e.getTarget() == drawingPane || e.getTarget() == clipRect) {
                            if (selectionMarker != null) {
                                drawingPane.getChildren().remove(selectionMarker);
                                SelectionUtils.deleteSelection(selectionMarker);
                                selectedShape = null;
                                selectionMarker = null;
                            }
                        } else if (e.getTarget() instanceof Shape) {
                            if (selectedShape != e.getTarget()) {
                                if (selectionMarker != null) {
                                    drawingPane.getChildren().remove(selectionMarker);
                                    SelectionUtils.deleteSelection(selectionMarker);
                                }
                                selectedShape = (Shape) e.getTarget();
                                selectionMarker = SelectionUtils.createSelection(selectedShape);
                                drawingPane.getChildren().add(selectionMarker);
                            }
                        }
                        break;
                }
            }
            
            if (e.getButton() == MouseButton.SECONDARY) {
                if (currentTool == ToolType.EDIT && selectedShape != null && e.getTarget() == selectedShape) {
                    HBox strokeBox = SelectionUtils.createPicker("Stroke:", (Color) selectedShape.getStroke(), color -> selectedShape.setStroke(color));
                    HBox fillBox = SelectionUtils.createPicker("Fill:", (Color) selectedShape.getFill(), color -> selectedShape.setFill(color));
                    
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
                            if (e.isShiftDown()) {
                                double perfectAngle = Math.round(newAngle / 45.0) * 45.0;
                                if (Math.abs(newAngle - perfectAngle) < 5.0) newAngle = perfectAngle;
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
                default:
                    break;
            }
        });

        drawingPane.setOnScroll(e -> {
            if (currentTool != ToolType.EDIT || selectedShape == null) return;
            double delta = e.getDeltaY() + e.getDeltaX();
            double scrollFactor = e.isShiftDown() ? 1.05 : 1.1;
            double newScale = selectedShape.getScaleX();
            
            newScale = (delta > 0) ? newScale * scrollFactor : newScale / scrollFactor;
            
            if (newScale > 0.1) {
                selectedShape.setScaleX(newScale);
                selectedShape.setScaleY(newScale);
            }
        });

        drawingPane.setOnMouseReleased(e -> {
            if (currentTool != ToolType.POLYGON) {
                currentShape = null;
            }
        });
    }
}