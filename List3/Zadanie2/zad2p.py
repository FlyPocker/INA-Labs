import sys

def Binomial(R, n, k):
    k = min(n-k, k)
    for i in range(k+1):
        R.append(1)
    for i in range(n+1):
        d = k
        if i <= k:
            d = i-1
        for j in range(d,0,-1):
            R[j] += R[j-1]
    return R[k]

def main():

    if len(sys.argv) != 3:
        print("Zla liczba argumentow")
        return
    try:
        n = int(sys.argv[1])
        k = int(sys.argv[2])
    except ValueError:
        print("Zly typ zmiennych")
        return
    R = []
    print(Binomial(R,n,k))


if __name__ == "__main__":
    main()