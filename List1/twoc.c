#include <stdio.h>
#include <math.h>
int main() {
    float a, b, c, delta;
    printf("Podaj wspolczynnik przy x^2: ");
    scanf("%f", &a);
    printf("Podaj wspolczynnik przy x: ");
    scanf("%f", &b);
    printf("Podaj wyraz wolny: ");
    scanf("%f", &c);
    if (a == 0) {
        printf("To nie jest rownanie kwadratowe!\n");
        return 0;
    }
    delta = b * b - 4 * a * c;
    if (delta < 0) {
        printf("Brak rozwiazan w zbiorze liczb rzeczywistych\n");
    } else if (delta == 0) {
        printf("Jest jedno rozwiazanie: x = %f\n", -b / (2 * a));
    } else {
        float x1 = (-b - sqrt(delta)) / (2 * a);
        float x2 = (-b + sqrt(delta)) / (2 * a);
        printf("Sa dwa rozwiazania: x1 = %f, x2 = %f\n", x1, x2);
    }
    
 return 0;
}