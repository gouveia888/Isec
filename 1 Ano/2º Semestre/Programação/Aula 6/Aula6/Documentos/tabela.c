#include <stdio.h>
#include "tabela.h"

void printV(ret a[], int total){
    int i;

    printf("Existem %d retangulos na tabela\n", total);
    for(i=0; i<total; i++) {
        printf("R. %d\n", i);
        printRet(a[i]);
    }
}

int addRet(ret a[], int *total){

    return 0;
}

void duplicaAltLarg(ret a[], int total){

}


int quadrante1(ret a[], int total){
    return 0;
}

void eliminaMenor(ret a[], int *total){

}

void eliminaVarios(ret a[], int *total, int lim){

}