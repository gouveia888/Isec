//
// Created by Gouveia on 28/11/2024.
//

#ifndef AULA_13_COMPRA_H
#define AULA_13_COMPRA_H
#include <iostream>
#include <string>

using namespace std;

// includes e outras declarações omitidos
class Compra {
public:
    Compra(string n="", int q=0);
    string getNome() const;
    int getQty () const;
    void atualizaQty(int dif);
    string descricao() const;
    bool operator<(const Compra &compra) const;
private:
    string nome;
    int qty;
};


#endif //AULA_13_COMPRA_H
