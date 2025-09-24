#include <stdio.h>
#include <stdlib.h>
#define  TAM 100

void mostrar_ficheiro_linhas(char *nome_fich){

    FILE *f = fopen(nome_fich,"rt");

    if(f==NULL){
        printf("O ficheiro nao existe\n");
        return;
    }
    char linha[TAM];

    while (fgets(linha,TAM,f))
        fputs(linha, stdout);

    fclose(f);
}

int main(){

    char nome[30];

    printf("Nome do ficheiro -> ");
    gets(nome);
    puts("\nLinha a Linha\n");
    mostrar_ficheiro_linhas(nome);
    //puts("\nCaracter a caracter \n");
    //mostrar_ficheiro_linhas(nome);

    return 0;
}