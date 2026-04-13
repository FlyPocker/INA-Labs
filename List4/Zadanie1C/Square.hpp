#pragma once
#include "Quadrangle.hpp"

class Square : public Quadrangle {
public:
    Square(double side) : Quadrangle(side, side, side, side, 90.0) {}

    std::string getName() const override {
        return "Square";
    }

    double getArea() const override {
        return sides[0] * sides[0];
    }
};