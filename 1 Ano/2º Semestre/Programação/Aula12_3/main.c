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

void mostrar_ficheiro_caracter(char *nome_fich){

    FILE *f = fopen(nome_fich,"rt");

    if(f==NULL){
        printf("O ficheiro nao existe\n");
        return;
    }
    char c;

    while ((c = fgetc(f)) != EOF)
        putchar(c);

    fclose(f);
}

void mostrar_ficheiro_char(char *nome_fich){

    FILE *f = fopen(nome_fich,"rt");

    if(f==NULL){
        printf("O ficheiro nao existe\n");
        return;
    }
    char c;

    while ((fscanf(f,"%c",&c)) && !feof(f))
        fprintf(stdout,"%c",c);
    puts("\n");

    fclose(f);
}

void mostrar_numero_linhas(char *nome_fich){

    FILE *f = fopen(nome_fich,"rt");

    if(f==NULL){
        printf("O ficheiro nao existe\n");
        return;
    }
    char linha[TAM];
    int i=1;

    while (fgets(linha,TAM,f))
        fprintf(stdout,"%d %s", i++, linha);

    fclose(f);
}

void mostrar_numero(char *nome_fich){

    FILE *f = fopen(nome_fich,"rt");

    if(f==NULL){
        printf("O ficheiro nao existe\n");
        return;
    }
    char linha[TAM];
    int i=1;
    char novalinha="true";
    char c;

    while ((c = fgetc(f)) != EOF){
        if(novalinha)
            fprintf(stdout,"%d", i++);
        fputc(c,stdout);
        novalinha = (c=='\n');
    }

    fclose(f);
}

void mostrar_linha(char *nome_fich, int lin){

    FILE *f = fopen(nome_fich,"rt");

    if(f==NULL){
        printf("O ficheiro nao existe\n");
        return;
    }
    char linha[TAM];
    int i=1;

    while (fgets(linha,TAM,f) && i<lin)
        i++;
    if(lin == i)
        fputs(linha,stdout);

    fclose(f);
}

int main(){

    char nome[30];

    printf("Nome do ficheiro -> ");
    gets(nome);
    puts("\nLinha a Linha\n");
    mostrar_ficheiro_linhas(nome);
    puts("\nCaracter a caracter \n");
    mostrar_ficheiro_caracter(nome);
    puts("\nMostra Char \n");
    mostrar_ficheiro_char(nome);
    puts("\nMostra Numeracao linhas \n");
    mostrar_numero_linhas(nome);
    puts("\nMostra Numeracao linhas 2 \n");
    mostrar_numero(nome);
    puts("\nMostra Numeracao linhas pedida \n");
    mostrar_linha(nome, 12);

    return 0;
}
