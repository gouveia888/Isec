#ifndef TRABALHO_SURPRESA_H
#define TRABALHO_SURPRESA_H
#include "Itens.h"

class Surpresa : public Itens{
public:
    void aplicarEfeito(Caravana &caravana) override;
};


#endif //TRABALHO_SURPRESA_H
