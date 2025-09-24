#include "CaixaPandora.h"

void CaixaPandora::aplicarEfeito(Caravana &caravana) {
    int tripulacao = caravana.getTripulacao();
    tripulacao *= 0.8; // reduz 20% da tripulacao
    caravana.setTripulacao(tripulacao);
}