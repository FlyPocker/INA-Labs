#pragma once
#include "Quadrangle.hpp"
#include <cmath>

class Diamond : public Quadrangle {
public:
    Diamond(double side, double angle) : Quadrangle(side, side, side, side, angle) {}

    std::string getName() const override {
        return "Diamond";
    }

    double getArea() const override {
        return sides[0] * sides[0] * std::sin(angle * M_PI / 180.0);
    }
};