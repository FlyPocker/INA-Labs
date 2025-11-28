import primes as p
import sys

def main():
    if len(sys.argv) != 3:
        print("Zla liczba argumentow")
        return
    try:
        n = int(sys.argv[2])
    except ValueError:
        print("Zly typ zmiennej")
        return
    FunctionType = sys.argv[1]
    if FunctionType == "pm":
        print(p.PrimeNumbers(n))
        return
    elif FunctionType == "pr":
        print(p.Prime(n))
        return
    elif FunctionType == "ip":
        print(p.IsPrime(n))
        return
    else:
        print("Nieznana komenda")
        return
if __name__ == "__main__":
    main()