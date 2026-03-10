#include <stdlib.h>
#include <stdio.h>

void primesToN(unsigned *P, unsigned n){
    unsigned k = 3;
    unsigned i = 0;
    unsigned j;
    unsigned is_Prime;
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
    
}

int main(int argc, char *argv[]){

    unsigned n;
    unsigned *P;
    if (argc != 2){
        printf("Zla liczba argumentow\n");
        return -1;
    }
    if (sscanf(argv[1], "%u", &n) != 1){
        printf("Zly typ zmiennej\n");
        return -1;
    }
    P = malloc((n)*sizeof(unsigned));
    primesToN(P, n);
    printf("%u-ta liczba pierwsza to %u\n", n, P[n-1]);

 free(P);
 return 0;
}