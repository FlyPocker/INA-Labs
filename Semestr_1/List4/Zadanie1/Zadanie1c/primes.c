#include <stdio.h>
#include <stdlib.h>
#include "primes.h"

void Sieve(unsigned *P, unsigned n){
    for (unsigned i=2; i<=n; i++){
        P[i] = 1;
    }
    for (unsigned i=2; i*i<=n; i++){
        if (P[i] == 1){
            for (unsigned j=i*i; j<=n; j= j+i){
                P[j] = 0;
            }
        }
    }
}
unsigned PrimeNumbers(unsigned n){
    unsigned count;
    unsigned *P;
    if (n<2){
        return 0;
    }

    P = malloc((n+1)*sizeof(unsigned));
    Sieve(P, n);
    count = 0;
    for (unsigned i=2; i<=n; i++){
        if (P[i] == 1){
            count = count+1;
        }
    }
    free(P);
    return count;
}
unsigned Prime(unsigned n){
    if (n<1){
        return 0;
    }
    unsigned k = 3;
    unsigned i = 0;
    unsigned j;
    unsigned is_Prime;
    unsigned *P;
    P = malloc((n+1)*sizeof(unsigned));
    P[0] = 2;
    while (i < n-1){
        j = 0;
        is_Prime = 1;
        while ((j <= i) && (P[j]*P[j] <= k) && (is_Prime == 1)){
            if (k % P[j] == 0){
                is_Prime = 0;
            }else{
                j += 1;
            }
        }
        if (is_Prime == 1){
            i += 1;
            P[i] = k;
        }
        k += 2;
    }
    k = P[n-1];
    free(P);
    return k;    
}
unsigned IsPrime(unsigned n){
    if (n<2){
        return 0;
    }
    unsigned *P;
    unsigned Res;
    P = malloc((n+1)*sizeof(unsigned));
    Sieve(P, n);
    Res = P[n];
    free(P);
    return Res;
}