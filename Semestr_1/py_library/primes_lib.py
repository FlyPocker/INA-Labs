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
def PrimeFactors (n):
    Res = []
    while n % 2 == 0:
        Res.append(2)
        n /= 2
    i = 3
    while i*i <= n:
        while n % i == 0:
            Res.append(i)
            n /= i
        i += 2
    if n > 1:
        Res.append(int(n))
    return Res
def Totient (n):
    Factors = PrimeFactors(n);
    if n == 1:
        return 1
    if Factors == []:
        return 0
    phi = Factors[0]-1
    for i in range(1,len(Factors)):
        if Factors[i-1] != Factors[i]:
            phi = phi*(Factors[i]-1)
        else:
            phi = phi*(Factors[i])
    return phi