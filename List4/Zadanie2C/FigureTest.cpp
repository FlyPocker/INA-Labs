#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include "Figure.hpp"

class FigureData {
private:
    std::string name;
    double area;
    double perimeter;

public:
    FigureData(std::string n, double a, double p) : name(n), area(a), perimeter(p) {}
    std::string getName() const { return name; }
    double getArea() const { return area; }
    double getPerimeter() const { return perimeter; }
};

int main(int argc, char* argv[]) {
    std::vector<FigureData> figures;

    for (int i = 1; i < argc; i++) {
        std::string token = argv[i];
        // Konwersja na małe litery dla pewności
        for (char &c : token) c = std::tolower(c);

        try {
            if (token == "c") { // Kolo
                if (i + 1 >= argc) throw std::out_of_range("Brak parametru");
                double radius = std::stod(argv[++i]);
                if (radius <= 0) throw std::invalid_argument("Promien musi byc wiekszy od zera.");

                Figure::SingleFigCalculator calc(Figure::SingleFig::Circle);
                figures.push_back(FigureData(calc.getName(), calc.calculateArea(radius), calc.calculatePerimeter(radius)));
            }
            else if (token == "p") { // Pieciokat
                if (i + 1 >= argc) throw std::out_of_range("Brak parametru");
                double pSide = std::stod(argv[++i]);
                if (pSide <= 0) throw std::invalid_argument("Bok musi byc wiekszy od zera.");

                Figure::SingleFigCalculator calc(Figure::SingleFig::Pentagon);
                figures.push_back(FigureData(calc.getName(), calc.calculateArea(pSide), calc.calculatePerimeter(pSide)));
            }
            else if (token == "h") { // Szesciokat
                if (i + 1 >= argc) throw std::out_of_range("Brak parametru");
                double hSide = std::stod(argv[++i]);
                if (hSide <= 0) throw std::invalid_argument("Bok musi byc wiekszy od zera.");

                Figure::SingleFigCalculator calc(Figure::SingleFig::Hexagon);
                figures.push_back(FigureData(calc.getName(), calc.calculateArea(hSide), calc.calculatePerimeter(hSide)));
            }
            else if (token == "q") { // Czworokat
                if (i + 5 >= argc) throw std::out_of_range("Brak parametru");
                double s1 = std::stod(argv[++i]);
                double s2 = std::stod(argv[++i]);
                double s3 = std::stod(argv[++i]);
                double s4 = std::stod(argv[++i]);
                double angle = std::stod(argv[++i]);

                if (s1 <= 0 || s2 <= 0 || s3 <= 0 || s4 <= 0 || angle <= 0 || angle >= 180) {
                    throw std::invalid_argument("Boki musza byc dodatnie, a kat w przedziale (0, 180).");
                }

                if (s1 == s2 && s2 == s3 && s3 == s4) {
                    if (angle == 90) {
                        // Kwadrat
                        Figure::SingleFigCalculator calc(Figure::SingleFig::Square);
                        figures.push_back(FigureData(calc.getName(), calc.calculateArea(s1), calc.calculatePerimeter(s1)));
                    } else {
                        // Diamond (Romb)
                        Figure::DoubleFigCalculator calc(Figure::DoubleFig::Diamond);
                        figures.push_back(FigureData(calc.getName(), calc.calculateArea(s1, angle), calc.calculatePerimeter(s1, angle)));
                    }
                } else if ((s1 == s3 && s2 == s4) || (s1 == s2 && s3 == s4)) {
                    if (angle == 90) {
                        // Prostokat
                        double sideA = s1;
                        double sideB = (s1 != s2) ? s2 : s3;
                        Figure::DoubleFigCalculator calc(Figure::DoubleFig::Rectangle);
                        figures.push_back(FigureData(calc.getName(), calc.calculateArea(sideA, sideB), calc.calculatePerimeter(sideA, sideB)));
                    } else {
                        throw std::invalid_argument("Parametry tworza rownoleglobok, ktory nie jest obslugiwany.");
                    }
                } else {
                    throw std::invalid_argument("Z podanych parametrow nie da sie utworzyc kwadratu, prostokata ani rombu.");
                }
            }
            else {
                std::cout << "Blad: Nieznana figura '" << token << "'. Pomijam..." << std::endl;
            }
        }
        catch (const std::out_of_range& e) {
            std::cout << "Blad: Za malo parametrow dla figury '" << token << "'." << std::endl;
            break; 
        }
        catch (const std::invalid_argument& e) {
            std::string msg = e.what();
            if (msg.find("stod") != std::string::npos) {
                std::cout << "Blad: Oczekiwano liczby po literze '" << token << "'." << std::endl;
            } else {
                std::cout << "Blad logiczny dla figury '" << token << "': " << e.what() << std::endl;
            }
        }
    }

    // Wypisanie wynikow
    std::cout << "\n===Figury===" << std::endl;
    for (const auto& fig : figures) {
        std::cout << fig.getName() << " | Pole: " << fig.getArea() << " | Obwod: " << fig.getPerimeter() << std::endl;
    }

    return 0;
}