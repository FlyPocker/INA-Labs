#include <stdio.h>
int NWD(int a, int b){
    int c;
    while (b != 0) {
        c = b;
        b = a % c;
        a = c;
    }
    return a;
}

int main() {
    int a, b;
    printf("Podaj pierwsza liczbe: ");
    scanf("%d", &a);
    printf("Podaj druga liczbe: ");
    scanf("%d", &b);
    
    printf("NWD to: %d\n", NWD(a, b));
    return 0;
}