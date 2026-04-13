#pragma once
#include "Figure.hpp"
#include <cmath>

class Circle : public Figure {
protected:
    double radius;

public:
    Circle(double radius) {
        this->radius = radius;
    }

    std::string getName() const override {
        return "Circle";
    }

    double getPerimeter() const override {
        return 2 * M_PI * radius;
    }

    double getArea() const override {
        return M_PI * radius * radius;
    }
};