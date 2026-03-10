#include <stdio.h>
#include <stdlib.h>

void CalculateChange(int* C, int* U, int* d, int C_size, int d_size) {
    int d_min, C_min, Temp; // d_min-indeks nominalu dla najmniejszego wyniku C_min-najmnijeszy wynik
    for (int i = 1; i <= C_size; i++) {
        C_min = -1;
        d_min = 0;
        for (int j = 1; j <= d_size; j++) {
            if (d[j] <= i) {
                Temp = C[i - d[j]] + 1;
                if (Temp != 0 && (Temp < C_min || C_min == -1)) {
                    C_min = Temp;
                    d_min = j;
                }
            } else {
                break;
            }
        }
        C[i] = C_min;
        U[i] = d_min;
    }
}

int main(int argc, char *argv[]) {
    int MaxChange, Temp, D_size;
    int *Denom = NULL, *Denom_Count = NULL, *Cost = NULL, *Used = NULL;
    FILE *InFile;

    if (argc < 2) {
        printf("Zla liczba argumentow\n");
        return -1;
    }

    InFile = fopen(argv[1], "r");
    if (InFile == NULL) return -1;

    if(fscanf(InFile, "%d", &D_size)!=1){
        printf("Zly typ zmiennych w pliku\n");
        fclose(InFile);
        return -1;
    }
    if(D_size <= 0){
        printf("Zly typ zmiennej w pliku, musza byc dodatnie\n");
        fclose(InFile);
        return -1;
    }

    Denom = (int*)malloc((D_size + 1) * sizeof(int));
    Denom_Count = (int*)malloc((D_size + 1) * sizeof(int));

    for (int i = 1; i <= D_size; i++) {
        if (fscanf(InFile, "%d", &Temp)!=1){
            printf("Zly typ zmiennej w pliku");
            goto cleanup;
        }
        if (Temp <= 0){
            printf("Zly typ zmiennej w pliku, musza byc dodatnie\n");
            goto cleanup;
        }
        Denom[i] = Temp;
        Denom_Count[i] = 0;
    }
    fclose(InFile);
    InFile = NULL;
    MaxChange = 0;
    for (int i = 2; i < argc; i++) {
        if (sscanf(argv[i], "%d", &Temp) != 1) {
            printf("Zly typ argumentu\n");
            goto cleanup;
        }
        if (Temp < 0){
            printf("Zly typ argumentu, musi byc dodatni\n");
            goto cleanup;
        }
        if (MaxChange < Temp) {
            MaxChange = Temp;
        }
    }

    Cost = (int*)malloc((MaxChange + 1) * sizeof(int));
    Used = (int*)malloc((MaxChange + 1) * sizeof(int));

    for (int i = 0; i <= MaxChange; i++) {
        Cost[i] = -1;
        Used[i] = 0;
    }
    Cost[0] = 0;

    CalculateChange(Cost, Used, Denom, MaxChange, D_size);

    for (int i = 2; i < argc; i++) {
        sscanf(argv[i], "%d", &Temp);
        
        if (Cost[Temp] == -1) {
            printf("%s ==> No solution!\n", argv[i]);
        } else {
            printf("%s ==> %d\n", argv[i], Cost[Temp]);
            while (Temp != 0) {
                Denom_Count[Used[Temp]] = Denom_Count[Used[Temp]] + 1;
                Temp = Temp - Denom[Used[Temp]];
            }
            for (int j = 1; j <= D_size; j++) {
                if (Denom_Count[j] != 0) {
                    printf(" %d x %d\n", Denom_Count[j], Denom[j]);
                    Denom_Count[j] = 0;
                }
            }
        }
    }
    cleanup:
        if (InFile) fclose(InFile);
        free(Denom_Count);
        free(Cost);
        free(Used);
        free(Denom);
        return 0;
}