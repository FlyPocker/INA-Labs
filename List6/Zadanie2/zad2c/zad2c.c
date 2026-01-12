#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int count = 0;
int *tab;
int *bije_wiersz;
int *bije_przek1;
int *bije_przek2;
int N;

void ustaw(int i) {
    for (int j = 1; j <= N; j++) {
        if (!bije_wiersz[j] && !bije_przek1[i + j] && !bije_przek2[i - j + N]) {
            tab[i] = j;
            bije_wiersz[j] = 1;
            bije_przek1[i + j] = 1;
            bije_przek2[i - j + N] = 1;

            if (i < N) {
                ustaw(i + 1);
            } else {
                count++;
                for (int k = 1; k <= N; k++) printf("%2d", tab[k]);
                printf("\n");
            }

            bije_wiersz[j] = 0;
            bije_przek1[i + j] = 0;
            bije_przek2[i - j + N] = 0;
        }
    }
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Zla liczba argumentow\n");
        return -1;
    }

    if (sscanf(argv[1], "%d", &N) != 1) {
        printf("Zly typ zmiennej\n");
        return -1;
    }

    tab = (int *)malloc((N + 1) * sizeof(int));
    bije_wiersz = (int *)calloc((N + 1), sizeof(int));
    bije_przek1 = (int *)calloc((2 * N + 1), sizeof(int));
    bije_przek2 = (int *)calloc((2 * N + 1), sizeof(int));

    ustaw(1);

    printf("Liczba rozwiazan: %d\n", count);

    free(tab);
    free(bije_wiersz);
    free(bije_przek1);
    free(bije_przek2);

    return 0;
}