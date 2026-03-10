import sys

def Sieve(P, n):
    for i in range(n+1):
        P.append(True);
    P[0] = False
    P[1] = False
    i = 2
    while i*i <= n:
        if P[i]:
            for j in range(i*i,n+1,i):
                P[j] = False
        i += 1
def countPrimes(P, n):
    count = 0
    for i in range(2,n+1):
        if P[i]:
            count += 1
    return count

def main():
    if len(sys.argv) != 2:
        print("Zla liczba argumentow")
        return
    try:
        n = int(sys.argv[1])
    except ValueError:
        print("Zly typ zmiennej")
        return
    P = []
    Sieve(P,n)
    print(f"Liczb pierwszych nie wiekszych od {n} jest {countPrimes(P,n)}")
if __name__ == "__main__":
    main()