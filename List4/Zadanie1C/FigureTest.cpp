#include <iostream>
#include <vector>
#include <string>
#include <stdexcept>
#include <cctype>

#include "Figure.hpp"
#include "Circle.hpp"
#include "Pentagon.hpp"
#include "Hexagon.hpp"
#include "Square.hpp"
#include "Rectangle.hpp"
#include "Diamond.hpp"

int main(int argc, char* argv[]) {
   
    std::vector<Figure*> figures;

    for (int i = 1; i < argc; i++) {
        std::string token = argv[i];

        for (char &c : token) c = std::tolower(c);

        try {
            if (token == "c") { // Kolo
                if (i + 1 >= argc) throw std::out_of_range("Brak parametru");
                double radius = std::stod(argv[i + 1]);
                if (radius <= 0) throw std::invalid_argument("Promien musi byc wiekszy od zera.");
                
                figures.push_back(new Circle(radius));
                i += 1;
            }
            else if (token == "p") { // Pieciokat
                if (i + 1 >= argc) throw std::out_of_range("Brak parametru");
                double pSide = std::stod(argv[i + 1]);
                if (pSide <= 0) throw std::invalid_argument("Bok musi byc wiekszy od zera.");
                
                figures.push_back(new Pentagon(pSide));
                i += 1;
            }
            else if (token == "h") { // Szesciokat
                if (i + 1 >= argc) throw std::out_of_range("Brak parametru");
                double hSide = std::stod(argv[i + 1]);
                if (hSide <= 0) throw std::invalid_argument("Bok musi byc wiekszy od zera.");
                
                figures.push_back(new Hexagon(hSide));
                i += 1;
            }
            else if (token == "q") { // Czworokat
                if (i + 5 >= argc) throw std::out_of_range("Brak parametru");
                double s1 = std::stod(argv[i + 1]);
                double s2 = std::stod(argv[i + 2]);
                double s3 = std::stod(argv[i + 3]);
                double s4 = std::stod(argv[i + 4]);
                double angle = std::stod(argv[i + 5]);

                if (s1 <= 0 || s2 <= 0 || s3 <= 0 || s4 <= 0 || angle <= 0 || angle >= 180) {
                    throw std::invalid_argument("Boki musza byc dodatnie, a kat w przedziale (0, 180).");
                }

                // Sprawdzanie typu czworokata
                if (s1 == s2 && s2 == s3 && s3 == s4) {
                    if (angle == 90) {
                        // kat 90 i rowne boki -> kwadrat
                        figures.push_back(new Square(s1));
                    } else {
                        // jedynie rowne boki -> romb
                        figures.push_back(new Diamond(s1, angle));
                    }
                } else if ((s1 == s3 && s2 == s4) || (s1 == s2 && s3 == s4)) {
                    // pary rownych bokow
                    if (angle == 90) {
                        double sideA = s1;
                        double sideB = (s1 != s2) ? s2 : s3;
                        figures.push_back(new Rectangle(sideA, sideB));
                    } else {
                        throw std::invalid_argument("Parametry tworza rownoleglobok, ktory nie jest obslugiwany.");
                    }
                } else {
                    throw std::invalid_argument("Z podanych parametrow nie da sie utworzyc kwadratu, prostokata ani rombu.");
                }
                
                i += 5;
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
                std::cout << "Blad: Oczekiwano liczby, a podano tekst po literze '" << token << "'." << std::endl;
            } else {
                std::cout << "Blad logiczny dla figury '" << token << "': " << e.what() << std::endl;
                if (token == "q") i += 5; else i += 1;
            }
        }
    }

    // Wypisanie wynikow
    std::cout << "\n===Figury===" << std::endl;
    for (size_t j = 0; j < figures.size(); j++) {
        std::cout << figures[j]->getName() << " | Pole: " << figures[j]->getArea() << " | Obwod: " << figures[j]->getPerimeter() << std::endl;
    }

    for (Figure* fig : figures) {
        delete fig;
    }

    return 0;
}