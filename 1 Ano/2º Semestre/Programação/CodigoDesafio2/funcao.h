

#ifndef DESAFIO2_FUNCAO_H
#define DESAFIO2_FUNCAO_H

typedef struct D1 data;
typedef struct D2 hora;
typedef struct D3 submissao;

struct D1{
    int dia, mes, ano;
};

struct D2{
    int h, m;
};

struct D3{
    int num;
    data dataSub;
    hora horaSub;
};

int calculaNota(data* limD, submissao sub);


#endif //DESAFIO2_FUNCAO_H
