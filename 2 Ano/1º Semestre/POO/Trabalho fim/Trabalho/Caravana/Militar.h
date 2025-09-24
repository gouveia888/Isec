#ifndef TRABALHO_MILITAR_H
#define TRABALHO_MILITAR_H
#include "Caravana.h"

class Militar : public Caravana{
private:
    int instantesSemTripulantes, lastX, lastY;
public:
    Militar(int mercadoria = 0, int agua = 400, int tripulacao = 40, int x = 0, int y = 0) :
            Caravana(mercadoria, agua, tripulacao, x, y), instantesSemTripulantes(0){}
    virtual ~Militar() override {}
    void mover(int dx, int dy);
    void verificarConsumoAgua();
    void Tempestade();
    void moverAleatoriamente();
    void ApanharItem();
    void atualizar();

};

#endif //TRABALHO_MILITAR_H
