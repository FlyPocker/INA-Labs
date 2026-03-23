#include <iostream>
#include <string>
#include <stdexcept>
#include "PascalTriangleRow.hpp"

int main(int argc, char* argv[]) {
    if (argc < 2){
        std::cout << "zla liczba argumentow" <<std::endl;
        return 0;
    }

    int n;
    PascalTriangleRow* triangleRow = nullptr;
    try {
        n = std::stoi(argv[1]);
        if (n<0){
            std::cout << argv[1] << " zmienna nie moze byc ujemna" << std::endl;
        }
        triangleRow = new PascalTriangleRow(n);
    } 
    catch (...) {
        std::cout << argv[1] << " nieprawidlowa dana" << std::endl;
        return 0;
    }
    
    for (int i = 2; i < argc; i++) {
        try {
            int m = std::stoi(argv[i]);
            if (0<=m & m<=n) {
                std::cout << m << " -> " << triangleRow->getBinom(m) << std::endl;
            } else {
                std::cout << m << " -> liczba spoza zakresu" << std::endl;
            }
        } 
        catch (...) {
            std::cout << argv[i] << " nieprawidlowa dana" << std::endl;
        }
    }

    delete triangleRow;
    return 0;
}