#include <stdlib.h>
#include <stdio.h>

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
unsigned countPrimes(unsigned *P, unsigned n){
    unsigned count;
    count = 0;
    for (unsigned i=2; i<=n; i++){
        if (P[i] == 1){
            count = count+1;
        }
    }
    return count;
}

int main(int argc, char *argv[]){

    unsigned n;
    unsigned *P;
    unsigned count;
    if (argc != 2){
        printf("Zla liczba argumentow\n");
        return -1;
    }
    if (sscanf(argv[1], "%u", &n) != 1){
        printf("Zly typ zmiennej\n");
        return -1;
    }
    P = malloc((n+1)*sizeof(unsigned));
    Sieve(P, n);
    count = countPrimes(P, n);
    printf("Liczb pierwszych nie wiekszych od %u jest %u\n", n, count);



 free(P);
 return 0;
}