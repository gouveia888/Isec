#include <stdio.h>
#include "ponto.h"
#include "retangulo.h"
#include "tabela.h"

int main(){

    ret tab[10] = {{{1,1},10,5}, {{2,3},2,6}, {{-1,4},7,2}};
    int total = 3, area_eli;

    addRet(tab, &total);
    printV(tab, total);

    duplicaAltLarg(tab, total);
    printV(tab, total);

    printf("Existem %d retangulos no quadrante 1\n", quadrante1(tab, total));

    eliminaMenor(tab, &total);
    printf("Retangulos depois do elimina menor\n");
    printV(tab, total);

    printf("Retangulos antes do inverter\n");
    inverte(tab, total);
    printf("Retangulos depois do inverter\n");

    printf("\nPretende eliminar os retangulos com area inferior a:");
    scanf("%d",&area_eli);

    eliminaVarios(tab, &total, area_eli);
    printV(tab, total);

    return 0;
}

//feito por tiago gouveia
