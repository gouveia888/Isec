// Programação 2023/24
// Aula Prática 3 - Ponteiros e Endereços: Comunicação entre funções e manipulação de tabelas

#include <stdio.h>

// Recebe: Endereços/ponteiros para 3 variáveis do tipo float
// Deve efetuar a rotação de valores entre essas variáveis
void rotacao(float *p1, float *p2, float *p3){

    float aux1=*p3;
    *p3=*p2;
    *p2=*p1;
    *p1=aux1;

}

// Recebe: Endereço inicial de uma tabela de inteiros, tamanho da tabela e endereços de 4 variáveis inteiras
// Deve colocar nas variáveis referenciadas pelos 4 ponteiros:
// número de pares, de impares, maior valor e posição do maior valor
void conta(int *t, int tam, int *np, int *ni, int *maior, int *pos){

    for(int i=0;i<tam;i++){
        if(t[i]>*maior){
            *maior=t[i];
            *pos=i;
        }

        if(t[i]%2==0)
            (*np)++;
        else
            (*ni)++;
    }

}

// Recebe: Endereço inicial e tamnaho de uma tabela de inteiros e endereços de 2 variáveis inteiras
// Deve colocar nas variáveis referenciadas pelos 2 ponteiros o maior e segundo maior elementos existentes na tabela
void procuraDupla(int *tab, int tam, int *prim, int *seg){

    //utilizando indicies

    int pmax=0, smax=0;

    if(*(tab+1)>*tab)
        pmax=1;
    else
        smax=1;

    for(int i=2;i<tam;i++)
        if(*(tab+i)>*(tab+pmax)){
            smax=pmax;
            pmax=i;
        }else if(*(tab+i)>*(tab+smax))
            smax=i;

        *prim = *(tab+pmax);
        *seg = *(tab+smax);

    /*for(int i=0;i<tam;i++){
        if(tab[i]>*prim)
            *prim=tab[i++];
        if(tab[i]>*seg && *seg<*prim)
            *seg=tab[i];
    }*/
}

void transforma(int *tab, int dim){

int media=0,i;

    for(i=0;i<dim;i++){
        media+=tab[i];
    }
    media=media/dim;
    printf("Media=%d\n", media);
    for(i=0;i<dim;i++){
        if(media>tab[i])
            tab[i]=0;
    }
}

int compara(int *v1, int tamv1, int *v2, int tamv2){

    /*int i,cont=0;

    tamv1!=tamv2 ? return 0 ;

    for(i=0, i<tamv1, i++){

        *(v1+i)==*(v2+i) ? cont++ : return 0;
    }

    cont==tamv1 ? return 1 : return 0;
    */
}

// Deve testar o código das funções com as 3 tabelas exemplificadas na função main()

int main(){


    float x=1.2, y=4.9, z=-2.3;

    int tab1[10] = {12, 7, 9, 4, 1, 4, 41, 7, 21, 14};
    int tab2[5] = {-2, -7, -8, -9, -1};
    int tab3[8] = {12, 10, 11, 5, 8, 3, -4, -1};

    int pares=0, impares=0, maior=0, posMaior=0;
    int prim=0, seg=0,i;

    printf("Antes: X=%.1f\tY=%.1f\tZ=%.1f\n", x, y, z);

    rotacao(&x,&y,&z);

    printf("Depois: X=%.1f\tY=%.1f\tZ=%.1f\n", x, y, z);

    conta(tab1, 10 , &pares, &impares , &maior , &posMaior);

    printf("\n\nPares: %d\tImpares: %d\t, Maior: %d\t, Posicao: %d", pares, impares, maior, posMaior);

    procuraDupla(tab3, 8, &prim, &seg);

    printf("\n\nMaior: %d\t, Segundo Maior: %d\n", prim, seg);

    /*printf("Exercicio 5");

    for(i=0;i<8;i++){
        printf("Valor %d, %d\n", i+1,tab3[i]);
    }

    transforma(tab3, 8);

    for(i=0;i<8;i++){
        printf("Valor %d, %d\n", i+1,tab3[i]);
    }*/

    printf("Exercicio 6\n");

    printf("Devolve: %d",compara(tab3, 8, tab3, 8));

    return 0;
}
