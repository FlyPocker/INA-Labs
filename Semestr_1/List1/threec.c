#include <stdio.h>

int main(){

    int n, p, m, t;
    printf("Podaj liczbe n: ");
    scanf("%d", &n);
    printf("Podaj podstawe systemu p: ");
    scanf("%d", &p);
    m = 0;
    t = n;
    while (t>0){
        m = (p * m) + (t % p);
        t /= p;
    }
    if (m==n)
        printf("Liczba %d jest palindromem w systemie %d\n", n, p);
    else
        printf("Liczba %d NIE jest palindromem w systemie %d\n", n, p);
 return 0;
}