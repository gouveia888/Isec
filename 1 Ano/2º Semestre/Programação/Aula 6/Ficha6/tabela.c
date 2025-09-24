#include <stdio.h>
#include <string.h>
#include "tabela.h"

void printV(ret a[], int total){
    int i;

    printf("Existem %d retangulos na tabela\n", total);

    for(i=0; i<total; i++) {
        printf("\nR. %d\n", i+1);
        printRet(a[i]);
    }
}

int addRet(ret a[], int *total){

    if(*total <= 10){
        initRet(&a[*total]);
        (*total)++;
        return 1;
    }

    return 0;
}

void duplicaAltLarg(ret a[], int total){

    for(int i=0; i<total; i++){
        if(areaR(a[i]) % 2 == 0){
            a[i].alt*=2;
            a[i].larg*=2;
        }
    }
}


int quadrante1(ret a[], int total){

    int i,cont=0;

    for(i=0;i<total;i++){
        if(a[i].canto.x>0 && a[i].canto.y>0)
            cont++;
    }

    return cont;
}

void eliminaMenor(ret a[], int *total){

    int menor=-1;
    int i;

    for(i=0; i<*total; i++){
        if(areaR(a[i]) < areaR(a[i]))
            menor = i;
    }

    if(menor==-1)
        printf("Nao existem retangulos\n");
    else{
        for (i=menor; i<*total ; i++) {
            a[i]=a[i+1];
        }
    }
    (*total)--;
}

void inverte(ret a[], int total){

    ret aux[total];
    int i,j;

    for(i=total-1,j=0;i>=0;i--,j++)
        aux[j]=a[i];

    for(i=0;i<total;i++)
        a[i]=aux[i];

    printV(a,total);

}

void eliminaVarios(ret a[], int *total, int lim){

    for(int i=*total-1; i>=0;i--){
        if(areaR(a[i])<lim){
            for (int j=i; j<*total ; j++) {
                a[j]=a[j+1];
            }
            (*total)--;
        }
    }

}