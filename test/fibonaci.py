def pitagoras (n):
    if n == 0:
        return 0
    if n == 1:
        return 1
    return pitagoras(n-1)+pitagoras(n-2)

print(pitagoras(36))