#ifndef POINT2D_HPP
#define POINT2D_HPP

#include <cmath>
#include <iostream>

struct Point2D {
    int x, y;

    Point2D(int x = 0, int y = 0) : x(x), y(y) {}

    double getDistance() const {
        return std::sqrt(x * x + y * y);
    }

    // Przeciążenie operatora mniejszości dla drzewa BST
    bool operator<(const Point2D& other) const {
        double d1 = getDistance();
        double d2 = other.getDistance();
        if (d1 != d2) return d1 < d2;
        if (x != other.x) return x < other.x;
        return y < other.y;
    }

    // Przeciążenie operatora większości
    bool operator>(const Point2D& other) const {
        double d1 = getDistance();
        double d2 = other.getDistance();
        if (d1 != d2) return d1 > d2;
        if (x != other.x) return x > other.x;
        return y > other.y;
    }

    // Przeciążenie operatora równości
    bool operator==(const Point2D& other) const {
        return x == other.x && y == other.y;
    }

    // Wypisywanie do strumienia tekstowego
    friend std::ostream& operator<<(std::ostream& os, const Point2D& p) {
        os << "(" << p.x << "," << p.y << ")";
        return os;
    }
};

#endif // POINT2D_HPP