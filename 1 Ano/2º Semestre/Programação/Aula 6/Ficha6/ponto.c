
#include <stdio.h>
#include "ponto.h"

// Escreve as coordenadas do ponto recebido como parâmetro
void printPonto(ponto2D a){
    printf("Ponto: (%d,%d)\n", a.x, a.y);
}

// Inicializa as coordenadas do ponto referenciado pelo parâmetro recebido. O utilizador indica os valores
void initPonto(ponto2D *p){
    printf("Introduza um ponto (x-enter y-enter)\n");
    scanf("%d",&p->x);//do tipo struct variavel a, a parte e parte y
    scanf("%d",&p->y);//do tipo struct variavel a, a parte parte y
}

// Recebe endereço de um ponto e valores para o deslocamento ao longo dos eixos
// Atualiza as coordenadas do ponto
void movePonto(ponto2D* p, int dx, int dy){
    p->x+=dx; //-> acesso por ponteiro
    p->y+=dy;
}

// Devolve o quadrante a que pertence o ponto recebido por parâmetro
int quadrante(ponto2D a){

    if(a.x==0 && a.y==0 )  // o . acesso direto ao elemento
        return 0;
    else if(a.x >= 0){
        if(a.y >= 0)
            return 1;
        else
            return 4;
    }else if(a.x <= 0){
        if(a.y>=0)
            return 2;
        else
            return 3;
    }
}

// Recebe 3 pontos
// Devolve 1 se estiverem na mesma reta, 0 se não estiverem
int eReta(ponto2D a, ponto2D b, ponto2D c){

    float m = (float)(b.y-a.y)/(b.x-a.x);
    //CALCULA VALOR DE B, ORDENADA NA ORIGEM
    float  d = (float)(a.y-(m*a.x));

    printf("\n Reta -->Y = %4.2f X+ %4.2f \n",m, d);

    if((c.y) == (m*c.y + d))
        return 1;
    else
        return 0;

}