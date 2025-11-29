#include "primes_lib.h"
#include <stdio.h>
#include <stdlib.h>

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
IntArray PrimeFactors(unsigned n){
    IntArray Res;
    Res.data = NULL;
    Res.size = 0;

    if (n <= 1){
        return Res;
    }

    unsigned n_temp = n;
    int count = 0;
    int Temp_Arr[100];
    while (n_temp % 2 == 0){
        
        Temp_Arr[count] = 2;
        count += 1;
        n_temp = n_temp/2;
    }
    for (int i=3; i*i<=n_temp; i+=2){
        while (n_temp % i == 0){
            Temp_Arr[count] = i;
            count += 1;
            n_temp = n_temp/i;
        }
    }
    if (n_temp > 1){
        Temp_Arr[count] = n_temp;
        count += 1;
    }
    Res.data = (int*)malloc(count * sizeof(int));
    Res.size = count;
    for (int i=0; i<count; i++){
        Res.data[i] = Temp_Arr[i];
    }
    return Res;
}
void Free_Arr(IntArray* arr){
    if (arr->data != NULL){
        free(arr->data);
        arr->data = NULL;
    }
    arr->size = 0;
}
unsigned Totient(unsigned n){
    unsigned phi;
    IntArray Factors = PrimeFactors(n);
    
    if (n == 1){
        return 1;
    }
    if (Factors.data == NULL){
        return 0;
    }
    phi = Factors.data[0]-1;
    for (int i=1; i<Factors.size; i++){
        if (Factors.data[i]!= Factors.data[i-1]){
            phi = phi*(Factors.data[i]-1);
        }else{
            phi = phi*(Factors.data[i]);
        }
    }
    Free_Arr(&Factors);
    return phi;
}
