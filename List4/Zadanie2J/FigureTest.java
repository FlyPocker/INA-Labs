// Pomocnicza klasa pelniaca role pojemnika na gotowe wyniki do tablicy
class FigureData {
    private String name;
    private double area;
    private double perimeter;

    public FigureData(String name, double area, double perimeter) {
        this.name = name;
        this.area = area;
        this.perimeter = perimeter;
    }

    public String getName() { return name; }
    public double getArea() { return area; }
    public double getPerimeter() { return perimeter; }
}

public class FigureTest {

    public static void main(String[] args) {
        FigureData[] figures = new FigureData[args.length];
        int count = 0;

        for (int i = 0; i < args.length; i++) {
            String token = args[i].toLowerCase();

            try {
                switch (token) {
                    case "c": // Kolo
                        double radius = Double.parseDouble(args[i + 1]);
                        if (radius <= 0) throw new IllegalArgumentException("Promien musi byc wiekszy od zera.");
                        
                        figures[count] = new FigureData(Figure.SingleFig.Circle.getName(), Figure.SingleFig.Circle.calculateArea(radius), Figure.SingleFig.Circle.calculatePerimeter(radius));
                        count += 1;
                        i += 1;
                        break;

                    case "p":
                        double pSide = Double.parseDouble(args[i + 1]);
                        if (pSide <= 0) throw new IllegalArgumentException("Bok musi byc wiekszy od zera.");
                        
                        figures[count] = new FigureData(Figure.SingleFig.Pentagon.getName(), Figure.SingleFig.Pentagon.calculateArea(pSide), Figure.SingleFig.Pentagon.calculatePerimeter(pSide));
                        count += 1;
                        i += 1;
                        break;

                    case "h":
                        double hSide = Double.parseDouble(args[i + 1]);
                        if (hSide <= 0) throw new IllegalArgumentException("Bok musi byc wiekszy od zera.");
                        
                        figures[count] = new FigureData(Figure.SingleFig.Hexagon.getName(), Figure.SingleFig.Hexagon.calculateArea(hSide), Figure.SingleFig.Hexagon.calculatePerimeter(hSide));
                        count += 1;
                        i += 1;
                        break;

                    case "q":
                        double s1 = Double.parseDouble(args[i + 1]);
                        double s2 = Double.parseDouble(args[i + 2]);
                        double s3 = Double.parseDouble(args[i + 3]);
                        double s4 = Double.parseDouble(args[i + 4]);
                        double angle = Double.parseDouble(args[i + 5]);

                        if (s1 <= 0 || s2 <= 0 || s3 <= 0 || s4 <= 0 || angle <= 0 || angle >= 180) {
                            throw new IllegalArgumentException("Boki musza byc dodatnie, a kat w przedziale (0, 180).");
                        }
                        // Sprawdzanie typu czworokata
                        if (s1 == s2 && s2 == s3 && s3 == s4) {
                            if (angle == 90) {
                                // kat 90 i rowne boki -> kwadrat
                                figures[count] = new FigureData(Figure.SingleFig.Square.getName(), Figure.SingleFig.Square.calculateArea(s1), Figure.SingleFig.Square.calculatePerimeter(s1));
                                count += 1;
                            } else {
                                // jedynie rowne boki -> romb
                                figures[count] = new FigureData(Figure.DoubleFig.Diamond.getName(), Figure.DoubleFig.Diamond.calculateArea(s1, angle), Figure.DoubleFig.Diamond.calculatePerimeter(s1, angle));
                                count += 1;
                            }
                        } else if ((s1 == s3 && s2 == s4) || (s1 == s2 && s3 == s4)) {
                            // pary rownych bokow
                            if (angle == 90) {
                                // Zabezpieczenie: wybieramy 2 rozne boki dla prostokata
                                double sideA = s1;
                                double sideB = (s1 != s2) ? s2 : s3;
                                
                                figures[count] = new FigureData(Figure.DoubleFig.Rectangle.getName(), Figure.DoubleFig.Rectangle.calculateArea(sideA, sideB), Figure.DoubleFig.Rectangle.calculatePerimeter(sideA, sideB));
                                count += 1;
                            } else {
                                throw new IllegalArgumentException("Parametry tworza rownoleglobok, ktory nie jest obslugiwany.");
                            }
                        } else {
                            throw new IllegalArgumentException("Z podanych parametrow nie da sie utworzyc kwadratu, prostokata ani rombu.");
                        }
                        
                        i += 5; // "Zjadamy" 5 parametrow
                        break;

                    default:
                        System.out.println("Blad: Nieznana figura '" + token + "'. Pomijam...");
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Blad: Za malo parametrow dla figury '" + token + "'.");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Blad: Oczekiwano liczby, a podano tekst po literze '" + token + "'.");
            } catch (IllegalArgumentException e) {
                System.out.println("Blad logiczny dla figury '" + token + "': " + e.getMessage());
                if(token.equals("q"))
                    i += 5; 
                else 
                    i += 1;
            }
        }

        // Wypisanie wynikow dla wszystkich poprawnie zapisanych figur
        System.out.println("\n===Figury===");
        for (int j = 0; j < count; j++) {
            System.out.println(figures[j].getName() +" | Pole: " + figures[j].getArea() + " | Obwod: " + figures[j].getPerimeter());
        }
    }
}