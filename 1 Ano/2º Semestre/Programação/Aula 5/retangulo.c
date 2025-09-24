#include <stdlib.h>
#include <stdio.h>
#include "retangulo.h"

void printRet(ret r){
    printf("\nCanto inferior esquerdo = (%d,%d)\n", r.canto.x , r.canto.y);
    printf("Canto superior esquerdo = (%d,%d)\n", r.canto.x, r.canto.y + r.alt);
    printf("Canto inferior direito =  (%d,%d)\n", r.canto.x + r.larg, r.canto.y);
    printf("Canto superior direito =  (%d,%d)\n", r.canto.x + r.larg , r.canto.y + r.alt);
}

void initRet(ret* p){
    printf("\nIntroduza o valor de x:");
    scanf("%d",&p->canto.x);
    printf("\nIntroduza o valor de y:");
    scanf("%d",&p->canto.y);
    printf("\nIntroduza o valor da altura :");
    scanf("%d",&p->alt);
    printf("\nIntroduza o valor da largura:");
    scanf("%d",&p->larg);
}

int areaR(ret r){

    int area = r.larg * r.alt;
    return area;
}

int dentroR(ret r, ponto2D a){

    if((a.x > r.canto.x && a.x < (r.canto.x + r.larg)) && (a.y > r.canto.y && a.y < (r.canto.y + r.alt)))
        return 1;

    return 0;
}

void moveR(ret* p, int dx, int dy){

    p->canto.x+=dx;
    p->canto.y+=dy;

}