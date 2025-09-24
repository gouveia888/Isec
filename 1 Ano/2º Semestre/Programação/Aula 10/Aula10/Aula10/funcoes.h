
#ifndef FUNCOES_H
#define FUNCOES_H

typedef struct {int d, m, a;} calendario;

typedef struct pessoa atleta, *pAtleta;

struct pessoa{
    char nome[100];
    int id;
    pAtleta prox;
};

struct prova{
    char local[50];
    calendario data;
    pAtleta p;
};

void libertaTudo(struct prova *p, int tam);

struct prova* criaED(int *totC);

void mostraTudo(struct prova *a, int totC);

void mostraCorrida(struct prova *a, int totC, char *nome);

int nrProvas(struct prova *a, int totC, int id);

void terminouTodas(struct prova *a, int totC);

void campeao(struct prova *a, int totC);

struct prova* novaProva(struct prova *a, int *totC, char *n, calendario d, int nAt, char nomes[][100], int ids[]);

#endif //FUNCOES_H
