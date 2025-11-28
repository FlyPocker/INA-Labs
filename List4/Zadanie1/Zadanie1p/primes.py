def Generate_Arr(P,n,k):
    for _ in range(n+1):
        P.append(k)

def Sieve(P, n):
    Generate_Arr(P,n,True)
    P[0] = False
    P[1] = False
    i = 2
    while i*i <= n:
        if P[i]:
            for j in range(i*i,n+1,i):
                P[j] = False
        i += 1

def PrimeNumbers(n):
    if n < 2:
        return 0
    count = 0
    P = []
    Sieve(P, n)
    for i in range(2,n+1):
        if P[i]:
            count += 1
    return 

def Prime(n):
    if n < 1:
        return 0
    P = []
    P.append(2)
    k = 3
    i = 0
    while i < n-1:
        j = 0
        is_Prime = True
        while (j <= i) and (P[j]*P[j] <= k) and (is_Prime):
            if k % P[j] == 0:
                is_Prime = False
            else:
                j += 1
        if is_Prime:
            i += 1
            P.append(k)
        k += 2
    return P[n-1]
def IsPrime(n):
    P = []
    Sieve(P,n)
    return P[n]