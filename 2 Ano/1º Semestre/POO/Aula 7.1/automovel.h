//
// Created by Gouveia on 10/10/2024.
//
#include <string>
#ifndef AULA2_AUTOMOVEL_H
#define AULA2_AUTOMOVEL_H

using namespace std;

class carro {

private:
    int ano;
    string cor, marca, matricula;
    static int cont_carros;
public:

    carro(int ano, string cor, string marca, string matricula) : ano(ano), marca(marca), cor(cor), matricula(matricula) {
        cont_carros++;
    }

    string getcor();
    string getmarca();
    int getano();
    carro& copia(carro a);
    int getNcarros();

};


#endif //AULA2_carro_H
