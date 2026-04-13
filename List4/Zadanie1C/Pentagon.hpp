#pragma once
#include "Figure.hpp"
#include <cmath>

class Pentagon : public Figure {
protected:
    double side;

public:
    Pentagon(double side) : side(side) {}

    std::string getName() const override {
        return "Pieciokat foremny";
    }

    double getPerimeter() const override {
        return 5 * side;
    }

    double getArea() const override {
        return (std::sqrt(25 + 10 * std::sqrt(5)) / 4.0) * side * side;
    }
};