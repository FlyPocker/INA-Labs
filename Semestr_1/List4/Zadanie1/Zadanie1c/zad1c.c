#include "primes.h"
#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]){
    if (argc != 3){
        printf("Zla liczba argumentow\n");
        return -1;
    }
    unsigned n;
    char *FunctionType = argv[1];
    if (sscanf(argv[2], "%u", &n) != 1){
        printf("Zly typ zmiennej");
        return -1;
    }
    if (strcmp(FunctionType,"pn") == 0){
        printf("%u\n", PrimeNumbers(n));
        return 0;
    }else if(strcmp(FunctionType,"pr") == 0){
        printf("%u\n", Prime(n));
        return 0;
    }else if(strcmp(FunctionType,"ip") == 0){
        if (IsPrime(n) == 1){
            printf("True\n");
        }else{
            printf("False\n");
        }
        return 0;
    }else{
        printf("Nie znane polecenie\n");
        return -1;
    }

}