#ifndef EX2_H
#define EX2_H

typedef struct tempo hor;
typedef struct voo v;

struct tempo{
    int hora,min;
};

struct voo{
    char partida[200], destino[200];
    int num;
    hor part;
};

void lista_v(v *p,int total);

void preenche_v(v *p, int *total, int quant);

void altera_hora(v *p, int num, int hora, int min, int total);

int check(v *p, hor a, int num, int total);

#endif