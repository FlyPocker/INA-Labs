#include <stdio.h>

int main() {
    // 1. Podstawowa paleta (kolory 30-37 to pierwszy plan)
    printf("--- PODSTAWOWE KOLORY ANSI ---\n");
    for (int i = 30; i <= 37; i++) {
        // Zmienna i wchodzi w miejsce %d
        printf("\033[%dmHello, World! (Kolor: %d)\033[0m\n", i, i);
    }

    // 2. Czy terminal może wyświetlić 256 kolorów? Tak.
    // Sekwencja to \033[38;2;<r>;<g>;<b>m, gdzie r, g, b to wartości od 0 do 255.
    printf("\n--- PALETA 256 KOLOROW ---\n");
    for (int i = 0; i < 256; i++) {
        printf("\033[38;2;255;%d;255m%03d\033[0m ", i, i);
        if ((i + 1) % 16 == 0) printf("\n");
    }
    
    printf("\n\033[0m"); // Gwarantowany reset na końcu
    return 0;
}
