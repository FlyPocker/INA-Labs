def main():
    a = int(input("Podaj pierwsza liczbe: "))
    b = int(input("Podaj druga liczbe: "))
        
    while b != 0:
        c = b
        b = a % c
        a = c
    print(f"NWD podanych liczb to {a}")
    
if __name__ == "__main__":
    main()