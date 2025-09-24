#include <stdio.h>
#include <stdlib.h>

#include "agenda.h"

// a-na funçao main o malloc vair  criar um espaço de memoria dinamica
//   na funcao f1 est a fazer o realocamento de memoria realloc se necessario
// b- os locais onde o podem exitir erros de memoria e no realloc e e nessa situaçao
// devolve o ponteiro a sem nenhuma alteraçao
// c- a dimensao do vetor de interios e 8
// d- a dimensao do vetor de inteiros e 3

int main() {

    pct tab = NULL;
    int i, total=0;

    for(i=0; i<3; i++)
        tab = addC(tab, &total);

    listaC(tab, total);

    printf("O numero do elemento a pesquisar e %d\n",getTel(tab,total,tab[2].nome));

    atualizaTel(tab,total, tab[2].nome, 23);

    listaC(tab, total);

    eliminaC(tab, &total, tab[0].nome);

    listaC(tab, total);

    free(tab);

    return 0;
}
