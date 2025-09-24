#ifndef TRABALHO_CAIXAPANDORA_H
#define TRABALHO_CAIXAPANDORA_H
#include "Itens.h"

class CaixaPandora : public Itens{
public:
    void aplicarEfeito(Caravana& caravana) override;
};


#endif //TRABALHO_CAIXAPANDORA_H
