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
    if (m==0){
        return i;
    }
    return -1;
}
PrimeNumbers::PrimeNumbers(int n){
    isPrime.assign(n+1, true);
    for (int i=2;i<=n;i++){
        if (isPrime[i]){
            for (int j=i+i;j<=n;j=j+i){
                isPrime[j] = false;
            }
        }
    }    
}