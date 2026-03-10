import math

def main():
    a = int(input("Podaj wspolcznynnik przy x^2: "))
    b = int(input("Podaj wspolcznynnik przy x: "))
    c = int(input("Podaj wyraz wolny: "))
    if a == 0:
        print("To nie jest rownanie kwadratowe.")
        return
    delta = b**2 - 4*a*c
    if delta < 0:
        print("Brak rozwiazan w zbiorze liczb rzeczywistych")
    elif delta == 0:
        x = -b / (2*a)
        print(f"Jest jedno rozwiazanie: x = {x}")
    else:
        x1 = (-b - math.sqrt(delta)) / (2*a)
        x2 = (-b + math.sqrt(delta)) / (2*a)
        print(f"Są dwa rozwiazania: x1 = {x1}, x2 = {x2}")

if __name__ == "__main__":
    main()