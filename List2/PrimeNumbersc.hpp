#pragma once

#include <ostream>
#include <vector>

class PrimeNumbers {
    private:
        std::vector<bool> isPrime;
    public:
        int getNumber(int m);

        PrimeNumbers(int n);

        friend std::ostream& operator<<(std::ostream& out, PrimeNumbers* t);
};