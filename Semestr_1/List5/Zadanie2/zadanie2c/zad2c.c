#include <stdlib.h>
#include <stdio.h>
#include <math.h>


typedef double (*functype)(double);
double findzero (functype f, double a, double b, double eps){
    double c;
    
    while ((b-a)/2.0 > eps){
        c = a+((b-a)/2.0);
        
        if (f(c) == 0.0){
            return c;
        }
        if (f(a)*f(c)<0.0){
            b = c;
        }else{
            a = c;
        }
    }
    return a+((b-a)/2.0);
}
double cosfunction (double x){
    return cos(x/2.0);
}

int main(){

    double eps = 0.1;
    double a = 2.0;
    double b = 4.0;
    for (int i=1;i<=8;i++){
        printf("Dla eps=%.0e , oszacowane miejsce zerowe cos(x/2) to %.10f\n", eps, findzero(cosfunction,a,b,eps));
        eps = eps/10.0;
    }

 return 0;
}