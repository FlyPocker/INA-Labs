import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Menedżer plików edytora graficznego.
 * Odpowiada za zapis i odczyt figur wektorowych we własnym formacie tekstowym.
 */
public class FileManager {

    /**
     * Zapisuje wszystkie narysowane figury do pliku tekstowego.
     * Ignoruje ramkę zaznaczenia oraz maskę przycinającą.
     * 
     * @param file Plik docelowy
     * @param drawingPane Plansza zawierająca figury
     * @param selectionMarker Aktualna ramka zaznaczenia (do pominięcia przy zapisie)
     */
    public static void saveToFile(File file, Pane drawingPane, Rectangle selectionMarker) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Node node : drawingPane.getChildren()) {
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
     * Wczytuje figury z pliku tekstowego, czyszcząc najpierw planszę.
     * Odtwarza również maskę przycinającą planszy.
     * 
     * @param file Plik źródłowy do odczytu
     * @param drawingPane Plansza, na której pojawią się wczytane figury
     */
    public static void loadFromFile(File file, Pane drawingPane) {
        try (Scanner scanner = new Scanner(file)) {
            drawingPane.getChildren().clear();
            
            // Odtworzenie maski przycinającej
            Rectangle clipRect = new Rectangle();
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
}