#include <stdio.h>
#include "ponto.h"
#include "retangulo.h"

int main(){

    ponto2D p1 = {3, 5}, p2 ,a = {7,2} , b={4,4} ,c={16,16};
    int reta, dentro;
    ret r1 = {2,4,10,10}, r2;

    printPonto(p1);

    initPonto(&p2);
    printPonto(p2);

    movePonto(&p1, -4, -1);
    printPonto(p1);
    printf("Quadrante deste ponto: %d\n", quadrante(p1));

    reta = eReta(a ,b ,c);

    if(reta == 1)
        printf("Os pontos pretencem a mesma reta\n");
    else
        printf("Os pontos nao pertencem a mesma reta\n");

    printRet(r1);
    initRet(&r2);
    printRet(r2);
    printf("A area do retangulo e %d\n", areaR(r2));
    dentro = dentroR(r2,p2);

    if(dentro == 1)
        printf("O ponto (%d,%d) esta dentro do retangulo\n",p2.x,p2.y);
    else
        printf("O ponto (%d,%d) nao esta dentro do retangulo\n",p2.x,p2.y);

    moveR(r2,10,10);
    printf("Retangulo deslocado");
    printRet(r2);

    return 0;
}
