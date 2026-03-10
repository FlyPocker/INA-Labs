import math

def find_zero(f, a_start, b_start, eps):
    a = a_start
    b = b_start
    while (b - a) / 2.0 > eps:
        c = a + ((b - a) / 2.0)
        if f(c) == 0.0:
            return c
        if f(a) * f(c) < 0.0:
            b = c
        else:
            a = c
    return a + ((b - a) / 2.0)

def cos_function(x):
    return math.cos(x / 2.0)

def main():
    a = 2.0
    b = 4.0
    eps = 0.1
    for i in range(1, 9):
        res = find_zero(cos_function, a, b, eps)
        print(f"Dla eps={eps:.0e} , oszacowane miejsce zerowe cos(x/2) to: {res:.10f}")
        eps = eps / 10.0

if __name__ == "__main__":
    main()