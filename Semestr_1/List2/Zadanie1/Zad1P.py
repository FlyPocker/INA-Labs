def NWD(a,b):
    while b != 0:
        c = b
        b = a % c
        a = c
    return a

def main():
    a = int(input("Podaj pierwsza liczbe: "))
    b = int(input("Podaj druga liczbe: "))
    print(f"NWD podanych liczb to {NWD(a,b)}")

if __name__ == "__main__":
    main()