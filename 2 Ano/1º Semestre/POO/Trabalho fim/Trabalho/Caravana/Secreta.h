#ifndef TRABALHO_SECRETA_H
#define TRABALHO_SECRETA_H

#include "Caravana.h"

class Secreta : public Caravana {
public:
    Secreta(); // Construtor padrão
    void mover(int x, int y); // Sobrescreve o metodo mover
    void verificarConsumoAgua() ; // Implementação do metodo para verificar consumo de água
};

#endif // TRABALHO_SECRETA_H