#include <stdio.h>
int main() {
    int a, b, c;
    printf("Podaj pierwsza liczbe: ");
    scanf("%d", &a);
    printf("Podaj druga liczbe: ");
    scanf("%d", &b);
    while (b != 0) {
        c = b;
        b = a % c;
        a = c;
    }
    printf("NWD to: %d\n", a);
    return 0;
}