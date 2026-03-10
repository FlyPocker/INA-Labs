#pragma once
typedef struct{
    int* data;
    unsigned size;
}IntArray;

IntArray PrimeFactors (unsigned n);
unsigned Totient(unsigned n); 
unsigned PrimeNumbers (unsigned n);
unsigned Prime (unsigned n);
unsigned IsPrime (unsigned n);
void Free_Arr(IntArray* arr);