#include <iostream>
#include <string>
#include <stdexcept>
#include "PrimeNumbersc.hpp"

int main(int argc, char* argv[]) {
    if (argc < 2) return 0;

    int n;
    PrimeNumbers* primes = nullptr;

    try {
        n = std::stoi(argv[1]);
        primes = new PrimeNumbers(n);
    } 
    catch (...) {
        std::cout << argv[1] << " nieprawidlowa dana" << std::endl;
        return 0;
    }

    for (int i = 2; i < argc; i++) {
        try {
            int m = std::stoi(argv[i]);
            int result = primes->getNumber(m);

            if (result > 1) {
                std::cout << m << " -> " << result << std::endl;
            } else {
                std::cout << m << " -> liczba spoza zakresu" << std::endl;
            }
        } 
        catch (...) {
            std::cout << argv[i] << " nieprawidlowa dana" << std::endl;
        }
    }

    delete primes;
    return 0;
}