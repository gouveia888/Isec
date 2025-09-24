#ifndef TRABALHO_MINA_H
#define TRABALHO_MINA_H
#include "Itens.h"

class Mina : public Itens{
public:
    void aplicarEfeito(Caravana &caravana) override;
};


#endif //TRABALHO_MINA_H
