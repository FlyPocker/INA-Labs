import sys

def primeToN(P,n):
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
    primeToN(P,n)
    print(f"{n}-ta liczba pierwsza to {P[n-1]}")
if __name__ == "__main__":
    main()