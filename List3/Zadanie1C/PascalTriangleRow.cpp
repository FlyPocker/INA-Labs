#include <iostream>
#include "PascalTriangleRow.hpp"

int PascalTriangleRow::getBinom(int m){
    return row[m];
}

void PascalTriangleRow::solveRow(int m){
    for (int i=1;i<m;i++){
        row[i]=(row[i-1]*(m-i+1))/i;
    }
}

PascalTriangleRow::PascalTriangleRow(int n){
    row.assign(n+1, 1);
    solveRow(n);
}