#pragma once
#include "Figure.hpp"

class Quadrangle : public Figure {
protected:
    double sides[4];
    double angle;

public:
    Quadrangle(double side1, double side2, double side3, double side4, double angle) {
        this->sides[0] = side1;
        this->sides[1] = side2;
        this->sides[2] = side3;
        this->sides[3] = side4;
        this->angle = angle;
    }

    double getPerimeter() const override {
        return sides[0] + sides[1] + sides[2] + sides[3];
    }
};