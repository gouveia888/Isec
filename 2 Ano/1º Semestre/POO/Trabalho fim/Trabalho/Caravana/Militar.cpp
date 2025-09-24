#include "Militar.h"
#include <iostream>
using namespace std;

void Militar::mover(int x, int y) {

    int distancia = (rand() % 3) + 1; // devolve 1 ou 2 ou 3

    int novaX = this->getX() + x * distancia;
    int novaY = this->getY() + y * distancia;

    this->setPosicao(novaX, novaY);

    lastY = y;
    lastX = x;
}



void Militar::verificarConsumoAgua() {

    if (this->getTripulacao() < 20) { // Menos de metade dos tripulantes
        this->setAgua(getAgua() - 1);
    } else {    // Mais de metade dos tripulantes
        this->setAgua(getAgua() - 3);
    }

    if (this->getAgua() < 0) {
        this->setAgua(0);
        auto it = std::remove(Caravana::todasAsCaravanas.begin(), Caravana::todasAsCaravanas.end(), this);
        Caravana::todasAsCaravanas.erase(it, Caravana::todasAsCaravanas.end());

    }
}

void Militar::Tempestade() {

    int tripulantesPerdida = static_cast<int>(getTripulacao() * 0.10); // Perde 10% dos tripulantes
    setTripulacao(getTripulacao() - tripulantesPerdida);

    int chanceDestruicao = 33;
    int sorteio = rand() % 100; // Devolve 0 e 99

    if (sorteio < chanceDestruicao) {
        cout << "A caravana destruida pela tempestade!" << endl;
        // Lógica para destruir a caravana
    }
}

void Militar::moverAleatoriamente() {
    //todo perseguir barvara a 6 posiçoes de diferença
}

void Militar::atualizar() {

    verificarConsumoAgua();

    if (getTripulacao() == 0) {
        instantesSemTripulantes++;

        if (instantesSemTripulantes < 7) {
            mover(lastX, lastY);
        }

        if (instantesSemTripulantes >= 7) {
            cout << "A caravana desapareceu após 7 instantes sem tripulantes." << endl;
        }
    } else {
        instantesSemTripulantes = 0;
    }
}

