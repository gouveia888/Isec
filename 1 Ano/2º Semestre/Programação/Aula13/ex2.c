#include <stdio.h>
#include "banco.h"


int main(){

    printFile("bancoA.dat");
    printDados("bancoA.dat");

    corrigeMorada("bancoA.dat", "Joana Ferreira", "Leiria");

    printFileInv("bancoA.dat");

    transfere("bancoA.dat", "Hugo Alves", "Ana Silva", 50);

    eliminaC("bancoA.dat", "Hugo Alves");

    printFile("bancoA.dat");

    return 0;
}