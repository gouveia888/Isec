#include "Secreta.h"

Secreta::Secreta() : Caravana(20, 500, 1, 0, 0) {}

void Secreta::mover(int x, int y) {
    int distancia = 1; // Define a distância que deseja mover
    int novaX = this->getX() + x * distancia; // Acesso à posição X
    int novaY = this->getY() + y * distancia; // Acesso à posição Y
    setPosicao(novaX, novaY); // Atualiza a posição da caravana
}

void Secreta::verificarConsumoAgua() {
        if (this->getTripulacao() == 0) {
            return;
        } else if (this->getTripulacao() <= 1) { // Mais que 1 tripulante
            this->setAgua(getAgua() - 1);
        } else {    // mais que 1
            this->setAgua(getAgua() - 600); //morre
        }

        if (this->getAgua() < 0) {
            this->setAgua(0);
            auto it = std::remove(Caravana::todasAsCaravanas.begin(), Caravana::todasAsCaravanas.end(), this);
            Caravana::todasAsCaravanas.erase(it, Caravana::todasAsCaravanas.end());

        }
}

