import sys

def CalculateChange(C, U, d, C_size, d_size):
    # d_min-indeks nominalu dla najmniejszego wyniku C_min-najmnijeszy wynik
    for i in range(1, C_size + 1):
        C_min = -1
        d_min = 0
        for j in range(1, d_size + 1):
            if d[j] <= i:
                Temp = C[i - d[j]] + 1
                if Temp != 0 and (Temp < C_min or C_min == -1):
                    C_min = Temp
                    d_min = j
            else:
                break
        C[i] = C_min
        U[i] = d_min

def main():
    if len(sys.argv) < 2:
        print("Zla liczba argumentow")
        return

    try:
        in_file = open(sys.argv[1], "r")
        line = in_file.readline()
        if not line:
            in_file.close()
            return
        D_size = int(line.strip())
        
        Denom = [0] * (D_size + 1)
        Denom_Count = [0] * (D_size + 1)
        
        for i in range(1, D_size + 1):
            line = in_file.readline()
            if line:
                Temp = int(line.strip())
                if Temp < 0:
                    print("Zly typ zmiennej")
                    return
                Denom[i] = Temp
        in_file.close()
    except (ValueError, FileNotFoundError):
        return

    MaxChange = 0
    for i in range(2, len(sys.argv)):
        try:
            Temp = int(sys.argv[i])
            if Temp < 0:
                print("Zly typ zmiennej")
                return
            if MaxChange < Temp:
                MaxChange = Temp
        except ValueError:
            print("Zly typ zmiennej")
            return

    Cost = [-1] * (MaxChange + 1)
    Cost[0] = 0
    Used = [0] * (MaxChange + 1)

    CalculateChange(Cost, Used, Denom, MaxChange, D_size)

    for i in range(2, len(sys.argv)):
        Temp = int(sys.argv[i])

        if Cost[Temp] == -1:
            print(f"{sys.argv[i]} ==> No solution!")
        else:
            print(f"{sys.argv[i]} ==> {Cost[Temp]}")
            while Temp != 0:
                Denom_Count[Used[Temp]] = Denom_Count[Used[Temp]] + 1
                Temp = Temp - Denom[Used[Temp]]
            
            for j in range(1, D_size + 1):
                if Denom_Count[j] != 0:
                    print(f" {Denom_Count[j]} x {Denom[j]}")
                    Denom_Count[j] = 0

if __name__ == "__main__":
    main()