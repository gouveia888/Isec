
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "agenda.h"

// Escreve os dados de todos os contactos na agenda
// Recebe endereço do vetor e numero de contactos armazenados
void listaC(pct p, int total){
    int i;

    printf("Existem %d contactos na agenda\n", total);
    for(i=0; i<total; i++)
        printf("%s\t%d\n", p[i].nome, p[i].num);
}

// Adiciona um novo contacto ao vetor dinamico. Os dados são indicados pelo utilizador
// Recebe endereço do vetor e endereço de variavel inteira contento o numero de contactos
// Devolve endereço de vetor depois de efetuada a atualizacao
pct addC(pct p, int *total){
    char nome[200];
    int num,i;
    pct aux;

    printf("Nome do novo contacto: ");
    scanf(" %[^\n]", &nome);

    for(i=0; i<*total; i++){
        if(strcmp(p[i].nome,nome)==0){
            printf("O nome introduzido ja se encontra na agenda\n");
            return p;
        }
    }
    printf("Numero do novo contacto: ");
    scanf("%d", &num);

        // Completar funcao

    aux = realloc(p,sizeof (pct) * (*total+1));

    if(aux==NULL)
        return 0;
        else{
            p=aux;
            //free(aux);
            strcpy(p[*total].nome,nome);
            p[*total].num=num;
            (*total)++;
        }


    return p;
}

// Recebe endereço do vetor, numero de contactos armazenados e nome do contacto a pesquisar
// Devolve o numero de telemovel de um contacto
int getTel(pct p, int total, char *nome){

    for(int i=0; i<total;i++){
        if(strcmp(p[i].nome,nome)==0)
            return p[i].num;
    }
    return -1;
}

// Atualiza numero de telemovel de um contacto
// Recebe endereço do vetor, numero de contactos armazenados, nome do contacto a atualizar e novo numero
// Devolve 1 se a atualizacao for efetuada, ou 0, caso contrario
int atualizaTel(pct p, int total, char *nome, int novoT){

    for(int i=0; i<total; i++){
        if(strcmp(p[i].nome,nome)==0){
            p[i].num=novoT;
            return 1;
        }
    }
    return 0;
}

// Eliminar um novo contacto do vetor dinamico
// Recebe endereço do vetor, endereço de variavel inteira contento o numero de contactos e nome do contacto a eliminar
// Devolve endereço de vetor depois de efetuada a atualizacao

pct eliminaC(pct p, int *total, char *nome){

    pct aux;
    int igual=-1,i;
    for(i=0;i<*total;i++){
        if(strcmp(p[i].nome,nome)==0)
        igual=i;
    }
    
    aux = realloc(p, sizeof(pct) * (*total - 1));

    if(aux==NULL || igual==-1){
        printf("Alteraçao nao efetuada");
        return p;
    }else{
        p=aux;
        for(i=igual; i<*total-1;i++){
            strcpy(p[i].nome,p[i+1].nome);
            p[i].num=p[i+1].num;
        }
        (*total)--;
    }

    return p;
}