//
// Created by Gouveia on 31/10/2024.
//

#ifndef AULA8_AUTOMOVEL_H
#define AULA8_AUTOMOVEL_H
#include "Roda.h"
#include <string>

using namespace std;

class Automovel {
    string marca;
    int potencia;
    static const int N_RODAS = 4;
    Roda rodas[N_RODAS];

public:
    Automovel(string marca, int potencia, double diametro);
    ~Automovel();
    Automovel(const Automovel &a);
    string tostring() const;
    Automovel& operator=(const Automovel &a);
};


#endif //AULA8_AUTOMOVEL_H
