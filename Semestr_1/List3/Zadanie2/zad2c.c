#include <stdio.h>
#include <stdlib.h>

unsigned Binomial (unsigned R[], unsigned n, unsigned k){
    R[0] = 1;
    unsigned d;
    for (unsigned i = 1; i<=n; i++){
        d = k;
        if (i<=k){
            d = i-1;
            R[i] = 1;
        }
        for (unsigned j = d; j >=1; j--){
            R[j] = R[j] + R[j-1];
        }
    }
    return R[k];
}

int main(int argc, char *argv[]){
    unsigned n, k;
    unsigned *R;
    if (argc != 3){
        printf("Zla liczba argumentow\n");
        return -1;
    }
    if (sscanf(argv[1], "%u", &n) != 1){
        printf("Zly typ zmiennej\n");
        return -1;
    }
    if (sscanf(argv[2], "%u", &k) != 1){
        printf("Zly typ zmiennej\n");
        return -1;
    }
    if (k>n){
        printf("k nie moze byc wieksze od n\n");
        return -1;
    }
    if (n-k < k){
        k = n-k;
    }
    R = malloc((k+1)*sizeof(unsigned));

    printf("%u\n", Binomial(R,n,k));

    free(R);
 return 0;
}