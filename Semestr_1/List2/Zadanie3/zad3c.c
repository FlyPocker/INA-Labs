#include <stdio.h>
void rozkladCzynnikow(int n){
    int count = 0;
    int even = 0;
    while (n % 2 == 0){
        count += 1;
        n = n/2;
    }
    if (count > 0){
        even = 1;
        printf("2^%d", count);
    }
    for (int i=3; i*i<=n; i+=2){
        count = 0;
        while (n % i == 0){
            count += 1;
            n = n/i;
        }
        if (count > 0){
            if (even == 1){
                printf("*%d^%d", i, count);
            }else{
                even = 1;
                printf("%d^%d", i, count);
            }
        }
    }
    if (n > 1){
        if (even == 1){
            printf("*%d^1", n);
        }else{
            printf("%d^1", n);
        }
    }
}

int main(){

    int a;
    printf("Podaj liczbe: ");
    scanf("%d", &a);
    printf("Rozklad na czynniki pierwsze liczby %d to: ", a);
    rozkladCzynnikow(a);
    printf("\n");

 return 0;
}