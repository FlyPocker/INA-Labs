#pragma once
#include "Figure.hpp"
#include <cmath>

class Hexagon : public Figure {
protected:
    double side;

public:
    Hexagon(double side) : side(side) {}

    std::string getName() const override {
        return "Szesciokat foremny";
    }

    double getPerimeter() const override {
        return 6 * side;
    }

    double getArea() const override {
        return (3 * std::sqrt(3) / 2.0) * side * side;
    }
};