//
// Created by Gouveia on 10/10/2024.
//

#include "automovel.h"
#include <sstream>

using namespace std;

int carro::cont_carros = 0;

string carro::getcor() {
    return this->cor;
}

string carro::getmarca() {
    return this->marca;
}

int carro::getano() {
    return this->ano;
}

int carro::getNcarros() {
    return cont_carros;
}

carro& carro::copia(carro a){

    this->ano = a.ano;
    this->marca = a.marca;
    this->cor = a.cor;

    return *this;
}


