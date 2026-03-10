#include <stdio.h>

int isPrime(int n){
    if (n <= 1){
        return 0;
    }
    if (n == 2){
        return 1;
    }
    if (n % 2 == 0){
        return 0;
    }
    for(int i=3; i*i<=n; i += 2){
        if (n % i == 0){
            return 0;
        }
    }
    return 1;
}

int main(){
    int a;
    printf("Podaj liczbe: ");
    scanf("%d", &a);
    if (isPrime(a) == 1){
        printf("Liczba %d jest pierwsza\n", a);
    }else{
        printf("Liczba %d NIE jest pierwsza\n", a);
    }


 return 0;
}