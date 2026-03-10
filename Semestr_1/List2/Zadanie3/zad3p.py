
def rozkladCzynnikow (n):
    res = ""
    count = 0
    while n % 2 == 0:
        count += 1
        n /= 2
    if count > 0:
        res += str(2) + "^" + str(count) + "*"
    i = 3
    while i*i <= n:
        count = 0
        while n % i == 0:
            count += 1
            n /= i
        if count >0:
            if res == "":
                res += str(i) + "^" + str(count) +"*"
        i += 2
    if n > 1:
        res += str(int(n))+"^1"
    else:
        res = res[0:-1]
    return res
        

def main():
    a = int(input("Podaj liczbe: "))
    print(f"Rozklad na czynniki pierwsze liczby {a} to: "+rozkladCzynnikow(a))
if __name__ == "__main__":
    main()