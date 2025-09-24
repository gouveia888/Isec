#include "Barbara.h"
#include <iostream>

using namespace std;

void Barbara::mover(Buffer &buffer) {
    int dx = (rand() % 3) - 1; // -1, 0 ou 1
    int dy = (rand() % 3) - 1; // -1, 0 ou 1

    // Calcula nova posição
    int novaX = this->getX() + dx;
    int novaY = this->getY() + dy;

    // Verifica se a nova posição é válida
    if (buffer.posicaoValida(novaX, novaY)) {
        // Limpa a posição anterior da caravana
        buffer.atualizarPosicaoCaravana(this->getId(), this->getX(), this->getY(),
                                        false); // Passa false para caravanas normais
        setPosicao(novaX, novaY); // Atualiza a posição interna da caravana
        buffer.atualizarPosicaoCaravana(this->getId(), novaX, novaY, true); // Passa true para a caravana bárbara
        cout << "Caravana bárbara movida para a posição (" << novaY << ", " << novaX << ")." << endl;
    } else {
        cout << "Movimento inválido! A nova posição está fora dos limites ou ocupada." << endl;
    }
}

void Barbara::Tempestade() {
    int tripulantesPerdida = static_cast<int>(getTripulacao() * 0.10); // Perde 10% dos tripulantes

    if (getTripulacao() <= 0) {
        setTripulacao(0); // Destruir caravana
    } else {
        setTripulacao(getTripulacao() - tripulantesPerdida);
    }

    int chanceDestruicao = 25;
    int sorteio = rand() % 100; // Devolve 0 e 99

    if (sorteio < chanceDestruicao) {
        cout << "A caravana foi destruida pela tempestade!" << endl;
        // Lógica para destruir a caravana
    }
}

void Barbara::atualizar() {
    turnosativos++;
    if (turnosativos >= maxTurnosAborrecidos) {
        cout << "A caravana barbara desapareceu após " << maxTurnosAborrecidos << " turnos." << endl;
        // Lógica para lidar com a caravana desaparecida
    }
}

void Barbara::verificarConsumoAgua() {
cout<<"caravana barabara nao consome agua"<<endl;
}

