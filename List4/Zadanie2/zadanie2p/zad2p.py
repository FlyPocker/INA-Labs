import sys
import os

current_dir = os.path.dirname(os.path.abspath(__file__))
lib_path = os.path.join(current_dir, "..", "..", "..", "py_library")
sys.path.append(lib_path)
import primes_lib as prim


def main():
    if len(sys.argv) < 2:
        print("Zla liczba argumentow")
        return
    try:
        for i in range(1, len(sys.argv)):
            n = int(sys.argv[i])
            print(f"totient({n}) = {prim.Totient(n)}")
    except ValueError:
        print("Zly typ zmiennej")
        return
    
if __name__ == "__main__":
    main()