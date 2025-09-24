//
// Created by Gouveia on 28/11/2024.
//

#ifndef AULA_13_LISTACOMPRASVETOR_H
#define AULA_13_LISTACOMPRASVETOR_H

#include <iostream>
#include <string>
#include <vector>
#include "Compra.h"

using namespace std;

// includes e outras declarações omitidos
class ListaComprasVetor {
public:
    ListaComprasVetor(): lista{}{};
    bool adiciona(string nome, int qty);
    bool removeQty(string nome, int qty);
    bool elimina(string nome);
    string obtemLista() const;
    int eliminaTodosCom(int qty);
private:
    vector<Compra> lista;
};


#endif //AULA_13_LISTACOMPRASVETOR_H
