#include "ArcaTesouro.h"

void ArcaTesouro::aplicarEfeito(Caravana &caravana) {
    int moedas = 1.1 * Dados::getMoedas();
    Dados::setMoedas(moedas);
}