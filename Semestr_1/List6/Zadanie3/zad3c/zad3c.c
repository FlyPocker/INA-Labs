#include <stdio.h>
#include <stdlib.h>

typedef struct { 
    int Digits[4]; 
} CodeStructure;

void EvaluateFeedback(CodeStructure TrialCode, CodeStructure SecretCode, int *CountOnPlace, int *CountOffPlace) {
    int UsedTrial[4] = {0}, UsedSecret[4] = {0};
    *CountOnPlace = 0; 
    *CountOffPlace = 0;

    for(int i = 0; i < 4; i++) {
        if(TrialCode.Digits[i] == SecretCode.Digits[i]) { 
            (*CountOnPlace)++; 
            UsedTrial[i] = 1; 
            UsedSecret[i] = 1; 
        }
    }

    for(int i = 0; i < 4; i++) {
        if(!UsedTrial[i]) {
            for(int j = 0; j < 4; j++) {
                if(!UsedSecret[j] && TrialCode.Digits[i] == SecretCode.Digits[j]) { 
                    (*CountOffPlace)++; 
                    UsedSecret[j] = 1; 
                    break; 
                }
            }
        }
    }
}

int main() {
    CodeStructure *AllPossibleCodes = malloc(1296 * sizeof(CodeStructure));
    int *IsCodePossible = malloc(1296 * sizeof(int));
    int TotalCodesCount = 0;

    for(int i = 1; i <= 6; i++)
        for(int j = 1; j <= 6; j++)
            for(int k = 1; k <= 6; k++)
                for(int l = 1; l <= 6; l++) {
                    AllPossibleCodes[TotalCodesCount].Digits[0] = i; 
                    AllPossibleCodes[TotalCodesCount].Digits[1] = j; 
                    AllPossibleCodes[TotalCodesCount].Digits[2] = k; 
                    AllPossibleCodes[TotalCodesCount].Digits[3] = l;
                    IsCodePossible[TotalCodesCount] = 1; 
                    TotalCodesCount++;
                }

    for(int CurrentTurn = 1; CurrentTurn <= 8; CurrentTurn++) {
        int CurrentCodeIndex = -1;
        for(int i = 0; i < 1296; i++) {
            if(IsCodePossible[i]) { 
                CurrentCodeIndex = i; 
                break; 
            }
        }
        
        if(CurrentCodeIndex == -1) { 
            printf("Oszukujesz!\n"); 
            break; 
        }

        printf("%d: %d%d%d%d ?\n", CurrentTurn, 
               AllPossibleCodes[CurrentCodeIndex].Digits[0], 
               AllPossibleCodes[CurrentCodeIndex].Digits[1], 
               AllPossibleCodes[CurrentCodeIndex].Digits[2], 
               AllPossibleCodes[CurrentCodeIndex].Digits[3]);
        
        int CountOnPlace, CountOffPlace;
        printf("Na swoim miejscu: "); scanf("%d", &CountOnPlace);
        printf("Nie na swoim miejscu: "); scanf("%d", &CountOffPlace);

        if(CountOnPlace == 4) { 
            printf("Wygralem.\n"); 
            break; 
        }

        for(int i = 0; i < 1296; i++) {
            if(IsCodePossible[i]) {
                int TempOnPlace, TempOffPlace;
                EvaluateFeedback(AllPossibleCodes[CurrentCodeIndex], AllPossibleCodes[i], &TempOnPlace, &TempOffPlace);
                if(TempOnPlace != CountOnPlace || TempOffPlace != CountOffPlace) {
                    IsCodePossible[i] = 0;
                }
            }
        }
    }

    free(AllPossibleCodes); 
    free(IsCodePossible);
    return 0;
}