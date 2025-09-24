//
// Created by Gouveia on 28/11/2024.
//

#ifndef AULA_13_LISTACOMPRAS_H
#define AULA_13_LISTACOMPRAS_H
#include <iostream>
#include <string>
#include <array>
#include "Compra.h"

using namespace std;

// includes e outras declarações omitidos
class ListaCompras {
public:
    ListaCompras(): lista{}{};
    bool adiciona(string nome, int qty);
    bool removeQty(string nome, int qty);
    bool elimina(string nome);
    string obtemLista() const;
private:
    array<Compra, 20> lista;
};


#endif //AULA_13_LISTACOMPRAS_H
