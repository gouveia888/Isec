//
// Created by Gouveia on 28/11/2024.
//

#ifndef AULA_13_LISTACOMPRASSET_H
#define AULA_13_LISTACOMPRASSET_H
#include <iostream>
#include <string>
#include <set>
#include "Compra.h"

using namespace std;

class ListaComprasSet {
public:
    bool adiciona(string nome, int qty);
    bool removeQty(string nome, int qty);
    bool elimina(string nome);
    string obtemLista() const;
    int eliminaTodosCom(int qty);

private:
    set<Compra> lista;
};


#endif //AULA_13_LISTACOMPRASSET_H
