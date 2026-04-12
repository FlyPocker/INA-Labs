public class Figure {
    // interfejs figur z jednym parametrem
    public interface SingleFigure {
        double calculateArea(double p1);
        double calculatePerimeter(double p1);
        String getName();
    }
    // interfejs figur z dwoma parametrami
    public interface DoubleFigure {
        double calculateArea(double p1, double p2);
        double calculatePerimeter(double p1, double p2);
        String getName();
    }

    // enum figur z jednym parametrem
    public enum SingleFig implements SingleFigure {
        Circle {
            @Override
            public double calculateArea(double r) { return Math.PI * r * r; }
            @Override
            public double calculatePerimeter(double r) { return 2 * Math.PI * r; }
            @Override
            public String getName() { return "Kolo"; }
        },
        Square {
            @Override
            public double calculateArea(double a) { return a * a; }
            @Override
            public double calculatePerimeter(double a) { return 4 * a; }
            @Override
            public String getName() { return "Kwadrat"; }
        },
        Pentagon {
            @Override
            public double calculateArea(double a) { 
                return (Math.sqrt(25 + 10 * Math.sqrt(5)) / 4) * a * a; 
            }
            @Override
            public double calculatePerimeter(double a) { return 5 * a; }
            @Override
            public String getName() { return "Pieciokat foremny"; }
        },
        Hexagon {
            @Override
            public double calculateArea(double a) { 
                return (3 * Math.sqrt(3) / 2) * a * a; 
            }
            @Override
            public double calculatePerimeter(double a) { return 6 * a; }
            @Override
            public String getName() { return "Szesciokat foremny"; }
        }
    }
    //enum figur z dwoma parametrami
    public enum DoubleFig implements DoubleFigure {
        Rectangle {
            @Override
            public double calculateArea(double a, double b) { return a * b; }
            @Override
            public double calculatePerimeter(double a, double b) { return 2 * a + 2 * b; }
            @Override
            public String getName() { return "Prostokat"; }
        },
        Diamond {
            @Override
            public double calculateArea(double a, double angle) { 
                return a * a * Math.sin(Math.toRadians(angle)); 
            }
            @Override
            public double calculatePerimeter(double a, double angle) { return 4 * a; }
            @Override
            public String getName() { return "Romb"; }
        }
    }

}