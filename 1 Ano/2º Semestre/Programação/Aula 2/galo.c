
#include <stdio.h>

#define DIM 3 // dimensão do tabuleiro

void escreve_tab(char t[][DIM]){
    int i, j;

    printf("\n\n");
    for(i=0; i<DIM; i++){
        for(j=0; j<DIM; j++)
            printf(" %c ", t[i][j]);
        putchar('\n');
    }
}

void escreve_resultado(int ganhou){
    if(ganhou == 0)
        printf("\nEmpate\n\n");
    else
        printf("\nGanhou o jogador %d\n\n", ganhou);
}


int linha(char t[][DIM]){
    int i, j;

    for(i=0; i<DIM; i++)
        if(t[i][0] != '_'){
            for(j=0; j<DIM-1 && t[i][j] == t[i][j+1]; j++)
                ;
            if(j==DIM-1)
                return 1;
        }
    return 0;
}

int coluna(char t[][DIM]){
    int i,j;

     for(j=0; j<DIM; j++)
        if(t[0][j] != '_'){
            for(i=0; i<DIM-1 && t[i][j] == t[i+1][j]; i++)
                ;
            if(i==DIM-1)
                return 1;
        }

    return 0;
}


int diag(char t[][DIM]){

    int i,j;

    for(i=0; i<DIM; i++)
        if(t[i][0] !='_'){
            for(j=0; j<DIM-1 && i<DIM-1 && t[i][j] == t[i+1][j+1];j++,i++);
            if(i==DIM-1)
                return 1;
        }

    for(i=DIM-1; i>=0; i--)
        if(t[i][DIM-1] !='_'){
            for(j=DIM-1; j>=1 && i>=1 && t[i][j] == t[i-1][j-1];j--);
            if(i==0 && j==0 && t[0][0] == t[DIM - 1][DIM - 1])
                return 1;
        }

    return 0;
}


void escolhe_jogada(char t[][DIM], int jogador){
    int pos;

    printf("\nE a vez do jogador %d\n", jogador);
    do{
        printf("Posicao: ");
        scanf(" %d", &pos);
    }while(pos<1 || pos>DIM*DIM || t[(pos-1)/DIM][(pos-1)%DIM] != '_');

    if(jogador == 1)
        t[(pos-1)/DIM][(pos-1)%DIM] = 'X';
    else
        t[(pos-1)/DIM][(pos-1)%DIM] = 'O';
}


void inicializa_tab(char t[][DIM]){
    int i,j;

    for(i=0; i<DIM; i++)
        for(j=0; j<DIM; j++)
            t[i][j] = '_';
}


int main(){

    char tab[DIM][DIM];
    int joga, n_jogadas, ganhou;

    joga=1;
    n_jogadas=0;
    ganhou=0;

    inicializa_tab(tab);
    do{
        escreve_tab(tab);
        escolhe_jogada(tab, joga);
        n_jogadas ++;

        if(linha(tab)==1 || coluna(tab)==1 || diag(tab)==1)
            ganhou = joga;
        else
            joga = joga%2 + 1;
    }
    while(ganhou==0 && n_jogadas < DIM*DIM);

    escreve_tab(tab);
    escreve_resultado(ganhou);
    return 0;
}
