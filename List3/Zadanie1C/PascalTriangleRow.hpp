#pragma once
#include <ostream>
#include <vector>

class PascalTriangleRow {
    private:
        std::vector<int> row;
    public:
        int getBinom(int m);
        void solveRow(int m);
        PascalTriangleRow(int n);

        friend std::ostream& operator<<(std::ostream& out, PascalTriangleRow* t);
};