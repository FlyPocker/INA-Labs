import sys

def main():
    if len(sys.argv) != 2:
        print("Zla liczba argumentow")
        return

    try:
        n = int(sys.argv[1])
    except ValueError:
        print("Zly typ zmiennej")
        return

    tab = [0] * (n + 1)
    bije_wiersz = [False] * (n + 1)
    bije_przek1 = [False] * (2 * n + 1)
    bije_przek2 = [False] * (2 * n + 1)
    count = 0

    def ustaw(i):
        nonlocal count
        for j in range(1, n + 1):
            if not bije_wiersz[j] and not bije_przek1[i + j] and not bije_przek2[i - j + n]:
                tab[i] = j
                bije_wiersz[j] = True
                bije_przek1[i + j] = True
                bije_przek2[i - j + n] = True

                if i < n:
                    ustaw(i + 1)
                else:
                    count += 1
                    print(" ".join(f"{tab[k]:2}" for k in range(1, n + 1)))

                bije_wiersz[j] = False
                bije_przek1[i + j] = False
                bije_przek2[i - j + n] = False

    ustaw(1)
    print(f"Liczba rozwiazan: {count}")

if __name__ == "__main__":
    main()