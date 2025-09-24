#include <stdio.h>
#include "ponto.h"
#include "retangulo.h"
#include "tabela.h"

int main(){

    ret tab[10] = {{{1,1},10,5}, {{2,3},2,6}, {{-1,4},7,2}};
    int total = 3;

    addRet(tab, &total);
    printV(tab, total);

    duplicaAltLarg(tab, total);
    printV(tab, total);

    printf("Existem %d retangulos no quadrante 1\n", quadrante1(tab, total));


    return 0;
}
