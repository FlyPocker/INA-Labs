#include <iostream>
#include <string>
#include <cmath>

class Figure {
public:
    class OneParamFigure {
    public:
        virtual double calculateArea(double param) const = 0;
        virtual double calculatePerimeter(double param) const = 0;
        virtual std::string getName() const = 0;
        virtual ~OneParamFigure() = default;
    };

    class TwoParamFigure {
    public:
        virtual double calculateArea(double param1, double param2) const = 0;
        virtual double calculatePerimeter(double param1, double param2) const = 0;
        virtual std::string getName() const = 0;
        virtual ~TwoParamFigure() = default;
    };

    enum class SingleFig { Circle, Square, Pentagon, Hexagon };
    enum class DoubleFig { Rectangle, Diamond };

   
    class SingleFigCalculator : public OneParamFigure {
    private:
        SingleFig type; // Przechowujemy enum, żeby wiedzieć co liczyć
    public:
        SingleFigCalculator(SingleFig type) : type(type) {}

        double calculateArea(double param) const override {
            switch (type) {
                case SingleFig::Circle: return M_PI * param * param;
                case SingleFig::Square: return param * param;
                case SingleFig::Pentagon: return (std::sqrt(25 + 10 * std::sqrt(5)) / 4.0) * param * param;
                case SingleFig::Hexagon: return (3 * std::sqrt(3) / 2.0) * param * param;
                default: return 0;
            }
        }

        double calculatePerimeter(double param) const override {
            switch (type) {
                case SingleFig::Circle: return 2 * M_PI * param;
                case SingleFig::Square: return 4 * param;
                case SingleFig::Pentagon: return 5 * param;
                case SingleFig::Hexagon: return 6 * param;
                default: return 0;
            }
        }

        std::string getName() const override {
            switch (type) {
                case SingleFig::Circle: return "Circle";
                case SingleFig::Square: return "Square";
                case SingleFig::Pentagon: return "Pentagon";
                case SingleFig::Hexagon: return "Hexagon";
                default: return "Unknown";
            }
        }
    };

    // Klasa Kalkulatora dla 2 parametrów
    class DoubleFigCalculator : public TwoParamFigure {
    private:
        DoubleFig type;
    public:
        DoubleFigCalculator(DoubleFig type) : type(type) {}

        double calculateArea(double p1, double p2) const override {
            switch (type) {
                case DoubleFig::Rectangle: return p1 * p2;
                case DoubleFig::Diamond: 
                    return p1 * p1 * std::sin(p2 * M_PI / 180.0);
                default: return 0;
            }
        }

        double calculatePerimeter(double p1, double p2) const override {
            switch (type) {
                case DoubleFig::Rectangle: return 2 * p1 + 2 * p2;
                case DoubleFig::Diamond: return 4 * p1; // p2 to kąt, więc używamy tylko p1 do obwodu
                default: return 0;
            }
        }

        std::string getName() const override {
            switch (type) {
                case DoubleFig::Rectangle: return "Rectangle";
                case DoubleFig::Diamond: return "Diiamond";
                default: return "Unknown";
            }
        }
    };
};