#ifndef TRABALHO_ARCATESOURO_H
#define TRABALHO_ARCATESOURO_H
#include "Itens.h"

class ArcaTesouro : public Itens {
public:
    void aplicarEfeito(Caravana& caravana) override;
};


#endif //TRABALHO_ARCATESOURO_H
