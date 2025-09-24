#include <stdio.h>
#include "ex2.h"


int main(){

    v tab[300]={{"Lisboa", "Londres",1,12,30},{"Porto", "LA",2,10,45},{"Lisboa", "Funchal",3,18,01}};
    int total=3, num, hora, min, quant;
    hor atual;

    lista_v(tab,total);
    printf("\nInsira a quantidade de voos a adicionar:");
    scanf("%d", &quant);
    preenche_v(tab, &total, quant);

    printf("\nInsira o numero do voo a alterar a hora:");
    scanf("%d", &num);
    printf("\nInsira a hora a alterar:");
    scanf("%d", &hora);
    printf("\nInsira os minutos a alterar:");
    scanf("%d", &min);
    altera_hora(tab, num, hora, min, total);

    lista_v(tab,total);

    printf("\nInsirao numero do aviao que pretende saber se ja partiu:");
    scanf("%d", &num);
    printf("\nInsira a hora a atual:");
    scanf("%d", &atual.hora);
    printf("\nInsira os minutos atuais:");
    scanf("%d", &atual.min);

    if(check(tab, atual, num, total))
        printf("O voo ja partiu!!");

    return 0;
}

//feito por tiago gouveia
