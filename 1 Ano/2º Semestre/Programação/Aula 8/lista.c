    // a - a posiçao dos novos elementos vai ser no inicio da lista a estrategia utilizada e last in first out
// b - lista ligada com 3 elementos sendos estes "CCC" -> "BBB" -> "AAA" -> NULL
// c - os novos elementos vao ser inseridos no final da lista ligada
// d - lista ligada com 3 elementos sendos estes "AAA" -> "BBB" -> "CCC" -> NULL
// e - os elementos vao ser sempre inseridos na segunda posiçao da lista ligada
// f - lista ligada com 3 elementos sendos estes "AAA" -> "CCC" -> "BBB" -> NULL

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct pessoa no, *pno;

struct pessoa{
    char nome[100];
    int id;
    float peso, altura, imc;
    pno prox;
};


// Função auxiliar para obter dados do utilizador e prencher um novo nó a inserir na lista
// Recebe endereço do nó a preencher
void getDados(pno p){
    printf("Nome: ");
    scanf(" %[^\n]", p->nome);
    printf("ID: ");
    scanf("%d", &(p->id));
    printf("Peso (Kg) e Altura (Metros): ");
    scanf("%f %f", &(p->peso), &(p->altura));
    p->imc = p->peso / (p->altura * p->altura);
    p->prox = NULL;
}

// Função para adicionar um novo nó à lista
// Recebe ponteiro de lista e devolve ponteiro de lista depois da inserção
// Esta função não está de acordo com o pedido no enunciado (2b) e tem que ser corrigida em 2 pontos:
// 1. Insere o novo nó no início da liste e não no final
// 2. Não valida se o ID é único. Esta validação poderá ser feita diretamente na função GetDados(), mas,
// para isso, terá que receber o ponteiro de lista como parâmetro adicional
pno addLista(pno lista){
    pno novo,aux=lista;

    novo = malloc(sizeof(no));
    if(novo == NULL)
        return lista;
    getDados(novo);

    while(aux->prox!=NULL)
        if(novo->id == aux->id){
            lista->prox = novo;
            free(novo);
        }else{
            aux=aux->prox;
        }

    return lista;
}

// Função que mostra o conteúdo da lista
// Recebe ponteiro de lista como parâmetro
void mostraLista(pno lista){
    while (lista != NULL){
        printf("%d - %s\n", lista->id, lista->nome);
        printf("P: %.1f\tA: %.1f\tIMC: %.2f\n\n", lista->peso, lista->altura, lista->imc);
        lista = lista->prox;
    }
}

// Função para atualizar o peso de uma das pessoas
// Recebe ponteiro de lista, id da pessoa a atualizar e novo peso
// Deve tambem ser atualizado o imc
void atualizaPeso(pno lista, int id, float novoPeso){
}

// Função para eliminar uma pessoa da lista
// Recebe ponteiro de lista e id da pessoa a eliminar
// Devolve ponteiro de lista depois da eliminação
pno elimina(pno lista, int id){

    return lista;
}

pno elimina_imc(pno lista, int imc){

    pno ant= NULL, atual=lista;

    while(atual->prox!=NULL){
       if(atual->imc<imc){
           ant->prox=atual->prox;
           free(atual);
       }else{
           atual=atual->prox;
           ant=ant->prox;
       }
    }

    return atual;
}

int main(){
    pno lista = NULL;   // Criação do ponteiro de lista
    int i;

    for(i=0; i<3; i++)
        lista = addLista(lista);
    mostraLista(lista);

    atualizaPeso(lista, 123, 78.5);

    lista = elimina(lista, 456);

    // Falta libertar a lista antes de terminar o programa

    pno elimina_imc(lista, 23)

    return 0;
}
