
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "funcoes.h"

// Liberta todo o espaco ocupado pela estrutura dinamica

void libertaTudo(struct prova *p, int tam){
    pAtleta aux;
    int i;

    for(i=0; i<tam; i++){
        while(p[i].p!=NULL){
            aux = p[i].p;
            p[i].p = p[i].p->prox;
            free(aux);
        }
    }
    free(p);
}


// Cria e devolve uma estrutura dinamica exemplo para teste das funções deste exercicio
// A informação criada é igual ao exemplo ilustrado na ficha pratica 10
struct prova* criaED(int *totC){
    struct prova a[4] = {{"Coimbra", {12,3,2023},NULL},{"Soure", {15,4,2023},NULL},
            {"Figueira da Foz", {17,9,2023}, NULL},{"Penela", {20,10,2023},NULL}};
    int i, j, k=14, p[4] = {4,3,5,3};
    atleta c[15] = {{"Ana",34}, {"Paulo", 12}, {"Sofia", 45},{"Rui", 7},
                    {"Ana",34}, {"Rui", 7},{"Paulo", 12},
                    {"Sofia",45}, {"Paulo", 12},{"Ana",34}, {"Rute", 3},{"Pedro", 103},
                     {"Rute", 3},{"Paulo", 12}, {"Ana",34}};

    struct prova *corrida;
    pAtleta novo;

    corrida = malloc(sizeof(struct prova) * 4);
    if(corrida == NULL){
        *totC = 0;
        return NULL;
    }
    *totC = 4;
    for(i=0; i<*totC; i++)
        corrida[i] = a[i];
    for(i=*totC-1; i>=0; i--){
        for(j=0; j<p[i]; j++){
            novo = malloc(sizeof(atleta));
            if(novo == NULL){
                libertaTudo(corrida, *totC);
                *totC = 0;
                return NULL;
            }
            *novo = c[k--];
            novo->prox = corrida[i].p;
            corrida[i].p = novo;
        }
    }
    return corrida;
}

// Recebe ponteiro para o inicio da estrutura dinamica e dimensão do array
// Escreve toda a informação armazenada (provas e classificação dos atletas)
void mostraTudo(struct prova *a, int totC){
    int i;
    pAtleta aux;

    for(i=0; i<totC; i++) {
        printf("\n%s %d-%d-%d\n", a[i].local, a[i].data.d, a[i].data.m, a[i].data.a);
        aux = a[i].p;
        while(aux != NULL){
            printf("%s - %d\n", aux->nome, aux->id);
            aux = aux->prox;
        }
    }
}

// Recebe ponteiro para o inicio da estrutura dinamica, dimensão do array e nome de uma prova
// Escreve a classificação dessa prova
void mostraCorrida(struct prova *a, int totC, char *nome){
    int i;
    pAtleta aux;

    for (i=0;i<totC;i++){
        if(strcmp(a[i].local,nome)==0){
            printf("\n%s %d-%d-%d\n", a[i].local, a[i].data.d, a[i].data.m, a[i].data.a);
            aux=a[i].p;
        }
    }

    while(aux != NULL) {
        printf("%s - %d\n", aux->nome, aux->id);
        aux = aux->prox;
    }

}

// Recebe ponteiro para o inicio da estrutura dinamica, dimensão do array e id de atleta
// Devolve número de provas terminadas por esse atleta
int nrProvas(struct prova *a, int totC, int id){
    int i,cont=0;
    pAtleta aux;

    for(i=0;i<totC;i++){
        aux=a[i].p;
        while(aux!=NULL)
            aux=aux->prox;
            if(aux->id==id){
                cont++;
            }
    }
    return cont;
}

// Recebe ponteiro para o inicio da estrutura dinamica e dimensão do array
// Escreve nome e id dos atletas que terminaram todas as provas
void terminouTodas(struct prova *a, int totC){
    int i=0,cont=0;
    pAtleta aux;

    for(i=0; i<totC; i++){
        aux=a[i].p;
        while (aux!=NULL){
            aux=aux->prox;
            if(aux->id)
                cont++;
        }
        if(cont==totC)
            printf("O atleta %s com o id=%d finalizou todas as provas",aux->nome,aux->id);
    }

    printf("\nFuncao terminouTodas() nao implementada\n");
}


// Recebe ponteiro para o inicio da estrutura dinamica e dimensão do array
// Escreve o nome do atleta campeão
void campeao(struct prova *a, int totC){
    printf("\nFuncao campeao() nao implementada\n");
}


// Recebe ponteiro para o inicio da estrutura dinamica e endereço para a dimensão do array
// Recebe dados de nova prova: Local, data, número de atletas, nome e ids de cada atleta
// Adiciona nova prova à estrutura dinâmica, mantendo a ordem das datas das corridas
// Devolve endereço do array de corridas depois da inserção (o número de provas deve ser atualizado)
struct prova* novaProva(struct prova *a, int *totC, char *n, calendario d, int nAt, char nomes[][100], int ids[]){
    printf("\nFuncao novaProva() nao implementada\n");
    return a;
}