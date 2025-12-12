#include "list.h"
#include <stdlib.h>
#include <stdio.h>

bool is_empty(list l){
    return (l->length ==0);
}
int pop(list l){
    node_ptr n = l->first;
    int e = n->elem;
    l->first = l->first->next;
    if (l->first == NULL){
        l->last = NULL;
    }
    free(n);
    l->length -= 1;
    return e;
}

void push(list l, int e){
    node_ptr n = malloc(sizeof(node));
    n->elem = e;
    n->next = l->first;
    l->first = n;
    if (l->last == NULL){
        l->last = n;
    }
    l->length += 1;
}
void append(list l, int e){
    node_ptr n = malloc(sizeof(node));
    n->elem = e;
    if(l->last == NULL){
        l->first = n;
    }else{
        l->last->next = n;
    }
    l->last = n;
    l->length += 1;
}
int get(list l, int i){
    node_ptr n = l->first;
    while (n!= NULL && i>1){
        n = n->next;
        i -= 1;
    }
    if (n==NULL){
        return 0;
    }
    return n->elem;
}
void put(list l, int i, int e){
    node_ptr n = l->first;
    while (n!= NULL && i>1){
        n = n->next;
        i -= 1;
    }
    if (n!= NULL){
        n->elem = e;
    }
}
void insert(list l, int i, int e){
    if (i == 1){
        push(l,e);
    }else if(i==l->length+1){
        append(l,e);
    }else{
        node_ptr n_new = malloc(sizeof(node));
        node_ptr n = l->first;
        i -= 1;
        n_new->elem = e;
        while (n!= NULL && i>1){
            n = n->next;
            i -= 1;
        }
        if (n!=NULL){
            n_new->next = n->next;
            n->next = n_new;
            l->length += 1;
        }
    }
}
void delet(list l, int i){
    bool last = false;
    if(i<= l->length){
        if(i==1){
            pop(l);
        }
        if(i == l->length){
            last = true;
        }
        node_ptr n = l->first;
        node_ptr nprev = l->first;
        while (i>1){
            nprev = n;
            n = n->next;
            i -= 1;
        }
        if (last){
            nprev->next = NULL;
            l->last = nprev;
            l->length -= 1;
            free(n);
        }else{
            nprev->next = n->next;
            l->length -= 1;
            free(n);
        }
    }
}

void printl(list l){
    node_ptr n = l->first;
    while (n!= NULL){
        printf(" %d", n->elem);
        n = n->next;
    }
}
int length(list l){
    int i = 0;
    node_ptr n = l->first;
    while (n!= NULL){
        i += 1;
        n = n->next;
    }
    return i;
}
void clean(list l){
    node_ptr n = l->first;
    node_ptr nnext = n->next;
    while (n!= NULL){
        nnext = n->next;
        free(n);
        n = nnext;
    }
    l->first = NULL;
    l->last = NULL;
}