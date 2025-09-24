#include <stdio.h>
#include "ex2.h"

void lista_v(v *p, int total){

    for(int i=0;i<total;i++)
        printf("O voo numero %d com partida de %s e destino a %s parte as %d:%d.\n",p[i].num, p[i].partida, p[i].destino, p[i].part.hora, p[i].part.min);

}

void preenche_v(v *p,int *total, int quant){
    int aux=*total+quant;

        for(int i=*total; i<aux;i++,(*total)++){
            printf("\nInsira o numero do voo:");
            scanf("%d",&p[*total].num);
            printf("\nInsira o local de partida:");
            scanf("%s",&p[*total].partida);
            printf("\nInsira o local de chegada:");
            scanf("%s",&p[*total].destino);
            printf("\nInsira a hora de partida:");
            scanf("%d",&p[*total].part.hora);
            printf("\nInsira os minutos de partida:");
            scanf("%d",&p[*total].part.min);
        }

}

void altera_hora(v *p, int num, int hora, int min, int total){

    int i;

    for(i=0; i<=total;i++){
        if(p[i].num==num){
            p[i].part.hora=hora;
            p[i].part.min=min;
        }

    }

}

int check(v *p, hor atual, int num, int total){

    int i;

    for(i=0; i<=total;i++){
        if(p[i].num==num && p[i].part.hora>atual.hora && p[i].part.min>atual.min){
            return 1;
        }
    }
    return 0;
}