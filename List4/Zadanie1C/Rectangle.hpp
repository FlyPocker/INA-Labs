#pragma once
#include "Quadrangle.hpp"

class Rectangle : public Quadrangle {
public:
    Rectangle(double sideA, double sideB) : Quadrangle(sideA, sideB, sideA, sideB, 90.0) {}

    std::string getName() const override {
        return "Rectangle";
    }

    double getArea() const override {
        return sides[0] * sides[1]; 
    }
};