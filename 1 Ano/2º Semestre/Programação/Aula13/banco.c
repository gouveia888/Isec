
#include <stdio.h>
#include <string.h>
#include "banco.h"

// Escreve conteudo do ficheiro binario na consola
// O nome do ficheiro érecebido como parâmetro
void printFile(char *nomeF){
    cliente a;
    FILE *f;

    f = fopen(nomeF, "rb");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro.\n"); return;
    }
    while(fread(&a, sizeof(cliente), 1, f) == 1)
        printf("%s - %s - %d - %d\n", a.nome, a.morada, a.conta, a.montante);
    fclose(f);
}

// Escreve tamanho do ficheiro binario e numero de clientes armazenados na consola
// O nome do ficheiro érecebido como parâmetro
void printDados(char *nomeF){
    FILE *f;

    f = fopen(nomeF, "rb");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro.\n"); return;
    }
    fseek(f, 0, SEEK_END);
    printf("\nTamanho do ficheiro: %ld\n", ftell(f));
    printf("Numero de clientes: %ld\n", ftell(f) / sizeof(cliente));

    fclose(f);
}

// Corrige morada de um cliente armazenado no ficheiro
// Recebe nome do ficheiro, nome do cliente e nova morada
// Devolve 1 se a correcao for efetuada com sucesso, ou 0, caso contrario
int corrigeMorada(char *nomeF, char *nomeC, char *nMorada){

    FILE *f;
    cliente a;
    f = fopen(nomeF, "rb+");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro.\n"); return -1;
    }
    while(fread(&a, sizeof(cliente), 1, f) == 1)
        if(strcmp(a.nome,nomeC)==0){
            strcpy(a.morada,nMorada);
            fseek(f, -(long unsigned)sizeof(cliente), SEEK_CUR);
            fwrite(&a, sizeof(cliente),1,f);
            fclose(f);
            return 1;
        }
    fclose(f);

    return 0;
}

// Escreve conteudo do ficheiro binario na consola. A informação deve ser listada por ordem alfabética inversa
// O nome do ficheiro érecebido como parâmetro
void printFileInv(char *nomeF) {
    FILE *f;
    cliente a;
    f = fopen(nomeF, "rb");
    int cont;
    if(f == NULL){
        printf("Erro no acesso ao ficheiro.\n");
        return;
    }

    fseek(f, 0, SEEK_END);
    cont = ftell(f) / sizeof(cliente);

    fseek(f, -sizeof(cliente), SEEK_END);

    for(; cont > 0; cont--) {
        fread(&a, sizeof(cliente), 1, f);
        printf("%s - %s - %d - %d\n", a.nome, a.morada, a.conta, a.montante);
        fseek(f, -2 * sizeof(cliente), SEEK_CUR);
    }

    printf("\n\n");

    fclose(f);
}



// Transfere montante entre 2 clientes
// Recebe nome do ficheiro, identificacaos clientes envolvidos na operacaa e montante a transferir
// Devolve 1 se a transferencia for efetuada com sucesso, ou 0, caso contrario
int transfere(char *nomeF, char *or, char *dest, int valor){

    FILE *f;
    cliente a;
    int origem=0,destinatario=0, cont=0;
    f = fopen(nomeF, "rb+");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro.\n"); return -1;
    }

    while(fread(&a, sizeof(cliente), 1, f) == 1){
        cont++;
        if(strcmp(a.nome,or)==1)
            origem=cont;
        if(strcmp(a.nome,dest)==1)
            destinatario=cont;
    }

    if(origem == 0 || destinatario == 0){
        printf("Cliente nao existe!!");
        fclose(f);
        return 0;
    }

    fseek(f, origem*sizeof(cliente), SEEK_SET);

    fread(&a, sizeof(cliente), 1, f);
    if(a.montante<valor){
        printf("Dinheiro insuficiente");
        return 0;
    }else{
        a.montante=a.montante-valor;
        fseek(f, destinatario*sizeof(cliente), SEEK_SET);
        fread(&a, sizeof(cliente), 1, f);
        a.montante=a.montante+valor;
    }

    return 0;
}

// Elimina um cliente do ficheiro, mantendo a ordem alfabetica
// Recebe nome do ficheiro e nome do cliente a eliminar
// Devolve 1 se a eliminação for efetuada com sucesso, ou 0, caso contrario
int eliminaC(char *nomeF, char *nome){
    return 0;
}