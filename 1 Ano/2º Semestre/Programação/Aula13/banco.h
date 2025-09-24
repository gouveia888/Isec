#ifndef P2324_AULA13_BANCO_H
#define P2324_AULA13_BANCO_H

typedef struct{
    char nome[200], morada[200];
    int conta, montante;
} cliente;

void printFile(char *nomeF);

void printDados(char *nomeF);

int corrigeMorada(char *nomeF, char *nomeC, char *nMorada);

void printFileInv(char *nomeF);

int transfere(char *nomeF, char *or, char *dest, int valor);

int eliminaC(char *nomeF, char *nome);

#endif //P2324_AULA13_BANCO_H
