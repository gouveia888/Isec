#ifndef TRABALHO_JAULA_H
#define TRABALHO_JAULA_H
#include "Itens.h"

class Jaula : public Itens{
public:
    void aplicarEfeito(Caravana &caravana) override;
};


#endif //TRABALHO_JAULA_H
