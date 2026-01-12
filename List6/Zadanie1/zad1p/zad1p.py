import sys

def is_valid(tab, n):
    for i in range(n):
        for j in range(i + 1, n):
            if abs(i - j) == abs(tab[i] - tab[j]):
                return False
    return True

def next_permutation(tab, n):
    i = n - 2
    while i >= 0 and tab[i] >= tab[i + 1]:
        i -= 1
    if i < 0:
        return False
    
    j = n - 1
    while tab[j] <= tab[i]:
        j -= 1
    
    tab[i], tab[j] = tab[j], tab[i]
    tab[i+1:] = reversed(tab[i+1:])
    return True

def main():
    if len(sys.argv) != 2:
        print("Zla liczba argumentow")
        return

    try:
        n = int(sys.argv[1])
    except ValueError:
        print("Zly typ zmiennej")
        return

    if n < 1:
        print("Number of solutions: 0")
        return

    tab = list(range(1, n + 1))
    count = 0

    while True:
        if is_valid(tab, n):
            count += 1
            print(" ".join(f"{x:2}" for x in tab))
        
        if not next_permutation(tab, n):
            break

    print(f"Number of solutions: {count}")

if __name__ == "__main__":
    main()