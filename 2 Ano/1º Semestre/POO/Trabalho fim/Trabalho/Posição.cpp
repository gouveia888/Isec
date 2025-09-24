#include "Posição.h"

int Posição::getX() const {
    return x;
}

int Posição::getY() const {
    return y;
}

void Posição::setPosicao(int newX, int newY ) {
    x = newX;
    y = newY;
}

Posição::Posição(int x, int y, Dados* dados) : x(x), y(y), dados(dados) {}