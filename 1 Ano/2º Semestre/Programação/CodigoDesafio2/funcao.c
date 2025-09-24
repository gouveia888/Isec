
#include "funcao.h"

// Nome: Tiago Gouveia Filipe
// Número: 2019112767


// Recebe:
// Endereço inicial de uma variável do tipo data (limD)
// Variável do tipo submissao (sub)

// Devolve a classificação obtida pela submissão

// Regras para calcular a classificação:
// 1. Submissão sub deve ter sido submetida na data indicada por limD, até às 11.55. Se cumprir estas restrições, devolve 10.
// Se tiver sido submetida na data indicada, mas depois das 11.55, perde 1 ponto por cada minuto de atraso. A partir das 12.05 passa a ter cotação 0
// Se a submissão tiver data diferente da referenciada por limD, deve ser devolvido o valor 0

// A função deve igualmente atualizar a data referenciada por limD, passando-a para o dia seguinte.
// Esta atualização deve ser efetuada depois de calcular a nota a atribuir à submissão

void avanca_dia (data *limb);
int ano_bissexto (data *p);

int calculaNota(data* limD, submissao sub){
    int nota=10;
    if(limD->ano != sub.dataSub.ano || limD->mes != sub.dataSub.mes || limD->dia != sub.dataSub.dia){
        nota = 0;
        avanca_dia(limD);
        return nota;
    }

    if(limD->ano == sub.dataSub.ano && limD->mes==sub.dataSub.mes && limD->dia==sub.dataSub.dia)
        if(sub.horaSub.h <= 11 && sub.horaSub.m <=55){
            avanca_dia(limD);
            return nota;
        }
        else if(sub.horaSub.h >=12 && sub.horaSub.m>5){
            nota=0;
            avanca_dia(limD);
            return nota;
        }
        else
            for(int i=11; i<=sub.horaSub.h;i++)
                for(int j=sub.horaSub.m; j<=6 || j>55; j++)
                   nota-=1;
        avanca_dia(limD);
        return nota;
}

void avanca_dia(data *limb){

    if(limb->dia==31 && limb->mes==12){
        limb->dia=1;
        limb->mes=1;
        limb->ano+=1;
    }else if(limb->mes==2 && limb->dia==ano_bissexto (limb)){
        limb->dia=1;
        limb->mes=3;
    }else if(limb->dia==31 && (limb->mes==1 || limb->mes==3 || limb->mes==5 || limb->mes==7 || limb->mes==8 || limb->mes==10)){
        limb->dia=1;
        limb->mes+=1;
    }else if(limb->dia==30 && (limb->mes==4 || limb->mes==6 || limb->mes==9 || limb->mes==11)){
        limb->dia=1;
        limb->mes+=1;
    }else{
        limb->dia+=1;
    }
}

int ano_bissexto (data *p){

    if ( ( p->ano % 4 == 0 && p->ano % 100 != 0 ) || p->ano % 400 == 0 )
        return 29;
    return 28;
}