#include "Buffer.h"
#include <fstream>
#include <iostream>
using namespace std;
Buffer::Buffer(int colunas, int linhas):colunas(colunas), linhas(linhas), mapa(nullptr){
    mapa = new char *[linhas];
    for(int i = 0; i < linhas; i++){
        mapa[i] = new char [colunas];
    }
}

Buffer::~Buffer() {
    for (int i = 0; i < linhas; i++) {
        delete[] mapa[i];
    }
    delete[] mapa;
}

int Buffer::getColunas() const{
    return this->colunas;
}

int Buffer::getLinhas() const {
    return this->linhas;
}

string Buffer::displaymapa() {
    ostringstream oss;

    for (int l = 0; l < this->linhas; l++) {
        for (int c = 0; c < this->colunas; c++){
            if(c == 0)
                oss << endl;
            oss << mapa[l][c];
        }
    }
    return oss.str();
}

void Buffer::setColunas(int colunas) {
    this->colunas=colunas;
}

void Buffer::setLinhas(int linhas) {
    this->linhas = linhas;
}

int Buffer::getmapa(char *nome_fich){
    ifstream f(nome_fich);
    string parametro;
    int linhas, colunas;

    if (!(f >> parametro && f >> linhas)) return -1;
    if (!(f >> parametro && f >> colunas)) return -1;

    for (int i = 0; i < this->linhas; i++) {
        delete[] this->mapa[i];
    }
    delete[] this->mapa;

    // Atualiza dimensões
    setLinhas(linhas);
    setColunas(colunas);

    // Realoca o buffer
    this->mapa = new char*[linhas];
    for (int i = 0; i < linhas; i++) {
        this->mapa[i] = new char[colunas];
    }

    for(int l = 0; l < this->getLinhas(); l++) {
        for (int c = 0; c < this->getColunas(); c++)
            if (!(f >> this->mapa[l][c]))
                return -1;
    }
    return 0;
}

void Buffer::limpamapa() {
    for(int l = 0; l < this->getLinhas(); l++) {
        for (int c = 0; c < this->getColunas(); c++)
            mapa[l][c]=' ';
    }
}




// Exemplo de implementação do metodo na classe Buffer
bool Buffer::posicaoValida(int x, int y) const {
    // Verifica se as coordenadas estão dentro dos limites do mapa
    if (x < 0 || x >= colunas || y < 0 || y >= linhas) {
        return false; // Fora dos limites
    }

    // Verifica se a posição é uma montanha ou ocupada
    char posicao = mapa[y][x]; // Supondo que mapa é uma matriz de caracteres
    if (posicao == '+' || posicao == '!') {
        return false;
    }

    return true; // Posição válida
}


void Buffer::atualizarPosicaoCaravana(int id, int x, int y, bool isBarbara) {
    // Limpa a posição anterior da caravana
    for (int l = 0; l < this->linhas; l++) {
        for (int c = 0; c < this->colunas; c++) {
            if (mapa[l][c] == '0' + id) { // Verifica se a célula contém o ID da caravana
                mapa[l][c] = '.'; // Substitui a posição anterior por '.'
            }
        }
    }
    // Atualiza a nova posição da caravana
    if (isBarbara) {
        mapa[y][x] = '!'; // Coloca o símbolo da caravana bárbara na nova posição
    } else {
        mapa[y][x] = '0' + id; // Coloca o ID da caravana na nova posição
    }
}