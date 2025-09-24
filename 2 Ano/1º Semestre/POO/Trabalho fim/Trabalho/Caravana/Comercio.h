#ifndef TRABALHO_COMERCIO_H
#define TRABALHO_COMERCIO_H

#include "Caravana.h"

class Comercio : public Caravana {
private:
    int instantesSemTripulantes;


public:
    Comercio(int mercadoria = 0, int agua = 200, int tripulacao = 20, int x = 0, int y = 0)
            : Caravana(mercadoria, agua, tripulacao, x, y), instantesSemTripulantes(0) {}
    virtual ~Comercio() override {}
    void mover(int dx, int dy);
    void verificarConsumoAgua();
    void Tempestade();
    void moverAleatoriamente();
    void ApanharItem();
    void atualizar();

};

#endif // TRABALHO_COMERCIO_H
