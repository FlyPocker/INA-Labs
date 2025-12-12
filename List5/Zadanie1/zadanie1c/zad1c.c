#include "list.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

int main() {
    bool cont = true;
    int r, i, len;
    char command[20];
    
    list l = malloc(sizeof(list_t));
    if (l == NULL) {
        return 1;
    }
    l->first = NULL;
    l->last = NULL;
    l->length = 0;

    while (cont) {
        printf(" Command : ");
        scanf("%s", command);

        if (!strcmp(command, "exit")) {
            cont = false;
            printf("Ending the code...\n");
        }
        else if (!strcmp(command, "pop")) {
            if (!is_empty(l)) {
                r = pop(l);
                printf(" Result : %d\n", r);
            } else {
                printf(" Error - list is empty!\n");
            }
        }
        else if (!strcmp(command, "push")) {
            printf(" Value : ");
            scanf("%d", &r);
            push(l, r);
            printf(" Result : OK \n");
        }
        else if (!strcmp(command, "append")) {
            printf(" Value : ");
            scanf("%d", &r);
            append(l, r);
            printf(" Result : OK \n");
        }
        else if (!strcmp(command, "get")) {
            printf(" Index : ");
            scanf("%d", &i);
            
            len = length(l);
            if (is_empty(l)) {
                printf(" Error - list is empty!\n");
            } 
            else if (i < 0 || i >= len) {
                printf(" Error - index out of bounds (0 to %d)!\n", len - 1);
            } 
            else {
                r = get(l, i);
                printf(" Result : %d\n", r);
            }
        }
        else if (!strcmp(command, "put")) {
            printf(" Index : ");
            scanf("%d", &i);
            printf(" Value : ");
            scanf("%d", &r);

            len = length(l);
            if (is_empty(l)) {
                printf(" Error - list is empty!\n");
            }
            else if (i < 0 || i >= len) {
                printf(" Error - index out of bounds (0 to %d)!\n", len - 1);
            } 
            else {
                put(l, i, r);
                printf(" Result : OK \n");
            }
        }
        else if (!strcmp(command, "insert")) {
            printf(" Index : ");
            scanf("%d", &i);
            printf(" Value : ");
            scanf("%d", &r);

            len = length(l);
            if (i < 0 || i > len) {
                printf(" Error - index out of bounds for insert (0 to %d)!\n", len);
            } 
            else {
                insert(l, i, r);
                printf(" Result : OK \n");
            }
        }
        else if (!strcmp(command, "delet")) {
            printf(" Index : ");
            scanf("%d", &i);

            len = length(l);
            if (is_empty(l)) {
                printf(" Error - list is empty!\n");
            }
            else if (i < 0 || i >= len) {
                printf(" Error - index out of bounds (0 to %d)!\n", len - 1);
            } 
            else {
                delet(l, i);
                printf(" Result : OK \n");
            }
        }
        else if (!strcmp(command, "printl")) {
            printf(" Result : ");
            if (is_empty(l)) {
                printf("Empty list");
            } else {
                printl(l);
            }
            printf("\n");
        }
        else if (!strcmp(command, "length")) {
            r = length(l);
            printf(" Result : %d\n", r);
        }
        else if (!strcmp(command, "clean")) {
            if (is_empty(l)) {
                 printf(" Info - list is already empty\n");
            } else {
                clean(l);
                printf(" Result : OK \n");
            }
        }
        else {
            printf(" Error - unknown command!\n");
        }
    }

    if (l != NULL) {
        clean(l);
        free(l);
    }

    return 0;
}