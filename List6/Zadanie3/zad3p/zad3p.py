import sys

def evaluate_feedback(trial_code, secret_code):
    count_on_place = sum(p == s for p, s in zip(trial_code, secret_code))
    
    trial_remaining = [p for p, s in zip(trial_code, secret_code) if p != s]
    secret_remaining = [s for p, s in zip(trial_code, secret_code) if p != s]
    
    count_off_place = 0
    for digit in trial_remaining:
        if digit in secret_remaining:
            count_off_place += 1
            secret_remaining.remove(digit)
            
    return count_on_place, count_off_place

def main():
    all_possible_codes = [[i, j, k, l] for i in range(1, 7) 
                                       for j in range(1, 7) 
                                       for k in range(1, 7) 
                                       for l in range(1, 7)]
    is_code_possible = [True] * 1296

    for current_turn in range(1, 9):
        current_code_index = next((i for i, possible in enumerate(is_code_possible) if possible), None)
        
        if current_code_index is None:
            print("Oszukujesz!")
            return

        trial_code = all_possible_codes[current_code_index]
        print(f"{current_turn}: {''.join(map(str, trial_code))} ?")
        
        try:
            count_on_place = int(input("Na swoim miejscu: "))
            count_off_place = int(input("Nie na swoim miejscu: "))
        except ValueError:
            print("Zly typ zmiennej")
            return

        if count_on_place == 4:
            print("Wygralem.")
            return

        for i in range(1296):
            if is_code_possible[i]:
                temp_on_place, temp_off_place = evaluate_feedback(trial_code, all_possible_codes[i])
                if temp_on_place != count_on_place or temp_off_place != count_off_place:
                    is_code_possible[i] = False

if __name__ == "__main__":
    main()