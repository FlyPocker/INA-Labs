#include <stdio.h>
#include "primes_lib.h"

int main(int argc, char *argv[]){
    if (argc < 2){
        printf("Zla liczba argumentow\n");
        return -1;
    }
    unsigned n;
    for (int i=2; i<=argc; i++){
        if (sscanf(argv[i-1], "%u", &n) != 1){
            printf("Zly typ zmiennej: ");
            printf("%s\n", argv[i-1]);
            return -1;
        }
        printf("totient(%d) = %u\n", n, Totient(n));
    }
    
 return 0;
}