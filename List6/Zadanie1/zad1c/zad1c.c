#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int is_valid(int *tab, int n) {
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (abs(i - j) == abs(tab[i] - tab[j])) {
                return 0;
            }
        }
    }
    return 1;
}

int next_permutation(int *tab, int n) {
    int i = n - 2;
    while (i >= 0 && tab[i] >= tab[i + 1]) i--;
    if (i < 0) return 0;

    int j = n - 1;
    while (tab[j] <= tab[i]) j--;

    int temp = tab[i];
    tab[i] = tab[j];
    tab[j] = temp;

    int left = i + 1, right = n - 1;
    while (left < right) {
        temp = tab[left];
        tab[left] = tab[right];
        tab[right] = temp;
        left++;
        right--;
    }
    return 1;
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Zla liczba argumentow\n");
        return -1;
    }

    int n;
    if (sscanf(argv[1], "%d", &n) != 1) {
        printf("Zly typ zmiennej\n");
        return -1;
    }

    if (n < 1) {
        printf("Number of solutions: 0\n");
        return 0;
    }

    int *tab = (int *)malloc(n * sizeof(int));
    for (int i = 0; i < n; i++) tab[i] = i + 1;

    int count = 0;
    do {
        if (is_valid(tab, n)) {
            count++;
            for (int i = 0; i < n; i++) printf("%2d", tab[i]);
            printf("\n");
        }
    } while (next_permutation(tab, n));

    printf("Number of solutions: %d\n", count);
    free(tab);

    return 0;
}