#include "Jaula.h"

void Jaula::aplicarEfeito(Caravana &caravana) {
    int prisioneiros = rand() % 40; //limite maximo de tripulantes de todas as caravanas
    int tripulacao = caravana.getTripulacao();
    tripulacao += prisioneiros; // adiciona os prisioneiros como tripulantes
    caravana.setTripulacao(tripulacao);
}