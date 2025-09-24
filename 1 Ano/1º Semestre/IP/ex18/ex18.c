#include <stdio.h>

void main(){

int dia,mes,ano;

/*
printf("Introduza o dia:");
scanf("%d",&dia);
printf("\nIntroduza o mes:");
scanf("%d",&mes);
printf("\nIntroduza o ano:");
scanf("%d",&ano);
*/
printf("\nIntroduza o dia/mes/ano:");
scanf("%d/%d/%d",&dia, &mes, &ano);

    if(dia==31 && mes==12){
        dia=1;
        mes=1;
        ano=ano+1;
        }else if(dia==28 && mes==2 || (dia==30 && (mes==4 || mes==6 || mes==9 || mes==11)) || dia==31){
            dia=1;
            mes=mes+1;
        }else
        dia=dia+1;

        printf("\nO dia seguinte e %d/%d/%d",dia,mes,ano);
}

