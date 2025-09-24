// Comercio.cpp
#include "Comercio.h"
#include <iostream>

using namespace std;

void Comercio::mover(int x, int y) {

    int distancia = (rand() % 2) + 1; // devolve 1 ou 2
    int novaX = this->getX() + x * distancia;
    int novaY = this->getY() + y * distancia;

    this->setPosicao(novaX, novaY);

    this->ApanharItem();
}

void Comercio::verificarConsumoAgua() {
    if (this->getTripulacao() == 0) {
        return;
    } else if (this->getTripulacao() < 10) { // Menos de metade dos tripulantes
        this->setAgua(getAgua() - 1);
    } else {    // Mais de metade dos tripulantes
        this->setAgua(getAgua() - 2);
    }

    if (this->getAgua() < 0) {
        this->setAgua(0);
        auto it = std::remove(Caravana::todasAsCaravanas.begin(), Caravana::todasAsCaravanas.end(), this);
        Caravana::todasAsCaravanas.erase(it, Caravana::todasAsCaravanas.end());

    }
}

void Comercio::Tempestade() {

    int chanceDestruicao;
    int sorteio = rand() % 100; // Devolve 0 e 99

    if(getMercadoria() > 40 /2)
        chanceDestruicao = 50;
    else
        chanceDestruicao = 25;

    if (sorteio < chanceDestruicao) {
        cout << "A caravana destruida pela tempestade!" << endl;
        // Lógica para destruir a caravana
    } else {
        int cargaPerdida = getMercadoria() * 0.25; // Perde 25% da carga
        setMercadoria(getMercadoria() - cargaPerdida);
    }
}

void Comercio::moverAleatoriamente() {

    int dx = (rand() % 3) - 1; // devolve -1, 0 ou 1
    int dy = (rand() % 3) - 1; // devolve -1, 0 ou 1
    this->mover(dx, dy);
}

void Comercio::ApanharItem() {

    //todo verificar se existe algum elemento da class item
}

void Comercio::atualizar() {
    verificarConsumoAgua();

    if (getTripulacao() == 0) {
        instantesSemTripulantes++;
        moverAleatoriamente();

        if (instantesSemTripulantes >= 5) {
            cout << "A caravana desapareceu após 5 instantes sem tripulantes." << endl;
        }
    } else {
        instantesSemTripulantes = 0;
    }

    ApanharItem();
}

