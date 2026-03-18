#include <iostream>
#include <vector>

#include "PrimeNumbersc.hpp"

int PrimeNumbers::getNumber(int m){
    int i = 2;
    while (m>0 & i<isPrime.size() - 1){
        i += 1;
        if (isPrime[i]){
            m -= 1;
        }
    }
    if (m==0 && isPrime.size()>1){
        return i;
    }
    return -1;
}
void PrimeNumbers::sievePrime(){
    for (int i=2;i<isPrime.size();i++){
        if (isPrime[i]){
            for (int j=i+i;j<isPrime.size();j=j+i){
                isPrime[j] = false;
            }
        }
    }
}

PrimeNumbers::PrimeNumbers(int n){
    isPrime.assign(n+1, true);  
}