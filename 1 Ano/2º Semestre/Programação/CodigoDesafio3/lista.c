
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "lista.h"

// Nome: Tiago Gouveia Filipe
// Número: 2019112767

void eliminaLista(pno lista){
    pno aux;

    while(lista != NULL){
        aux = lista;
        lista = lista->prox;
        free(aux);
    }
}

pno criaLista(no tab[], int tam){
    int i;
    pno lista=NULL, novo;

    for(i=tam-1; i>=0; i--){
        novo = malloc(sizeof(no));
        if(novo == NULL){
            eliminaLista(lista);
            return NULL;
        }
        *novo = tab[i];
        novo->prox = lista;
        lista = novo;
    }
    return lista;
}
pno insere_ordenado(pno lista, pno novo);
void mostraLista(pno lista){
    printf("{ ");
    while(lista != NULL){
        printf("%s-%d", lista->id, lista->v);
        lista = lista->prox;
        if(lista!=NULL)
            printf(",\t");
    }
    printf("}");
}
int tam_lista(pno lista);
pno elimina_no(pno ant, pno lista);
pno desafio3(pno lista, char *velhoID, char* novoID){
    pno aux = lista;
    pno ant = NULL;
    pno no_aux = NULL;

    if (tam_lista(aux) < 3) //verifica se tem 3 elementos
        return lista;

    int i = 1;
    while (aux != NULL) {  //elimina os nos pares
        if (i % 2 == 0) {
            aux = elimina_no(ant, aux);
        } else {
            ant = aux;
            aux = aux->prox;
        }
        i++;
    }

    aux = lista;
    ant = NULL;
    while (aux != NULL) {  //alterar o id
        if (strcmp(aux->id, velhoID) == 0) {
            strcpy(aux->id, novoID);
            no_aux = aux; //no que queremos inserir ordenadamente
            if (ant == NULL) { //se for o primeiro
                lista = aux->prox;
            } else {
                ant->prox = ant->prox->prox;
            }
        }
        ant = aux;
        aux = aux->prox;
    }

    if (no_aux != NULL)
        lista = insere_ordenado(lista, no_aux);

    return lista;
}

int tam_lista(pno lista) {

    int i = 0;
    while (lista != NULL) {
        i++;
        lista = lista->prox;
    }

    return i;
}

pno elimina_no(pno ant, pno lista) {
    if (ant != NULL) {
        ant->prox = lista->prox;
        free(lista);
        return ant->prox;
    } else {
        pno temp = lista;
        lista = lista->prox;
        free(temp);
        return lista;
    }
}

pno insere_ordenado(pno lista, pno novo) {
    pno anterior = NULL;
    pno atual = lista;
    novo->prox=NULL;

    while (atual != NULL && strcmp(atual->id, novo->id) < 0) {
        anterior = atual;
        atual = atual->prox;
    }//local da inserçao

    if (anterior == NULL) {  //insere no inicio
        novo->prox = lista;
        return novo;
    } else {
        anterior->prox = novo;
        novo->prox = atual;
        return lista;
    }

}