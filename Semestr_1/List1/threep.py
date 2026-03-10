def main():
    print("Podaj liczbe n: ")
    n = int(input())
    print("Podaj podstawe systemu p: ")
    p = int(input())
    m = 0
    t = n
    while t > 0:
        m = p*m + t % p
        t = t // p
    if m == n:
        print(f"Liczba {n} jest palindromem w systemie o podstawie {p}")
    else:
        print(f"Liczba {n} NIE jest palindromem w systemie o podstawie {p}")

if __name__ == "__main__":
    main()