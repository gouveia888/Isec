#include <stdio.h>
#include "funcoes.h"
#include <string.h>



int main() {
    struct prova* corridas = NULL;
    int totC = 0, x, id=45;

    corridas = criaED(&totC);
    mostraTudo(corridas, totC);

    // Alinea a)
    mostraCorrida(corridas, totC, "Soure");

    // Alinea b)
    x = nrProvas(corridas, totC, id);
    printf("\nO atleta com id %d terminou %d corridas\n", id, x);

    // Alinea c)
    terminouTodas(corridas, totC);

    // Alinea d)
    campeao(corridas, totC);

    corridas = novaProva(corridas, &totC, "Lousa", (calendario){1,6,2023}, 3,
                         (char[][100]){"Rui","Ana","Paulo"}, (int []){23, 56, 33});

    libertaTudo(corridas, totC);

    return 0;
}
