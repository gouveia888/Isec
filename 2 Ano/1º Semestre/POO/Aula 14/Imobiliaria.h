//
// Created by Henrique Marques on 12/12/2024.
//

#ifndef IMOBILIARIA_H
#define IMOBILIARIA_H
#include <vector>

#include "Imovel/Imovel.h"
#include <string>

using namespace std;

class Imobiliaria {
public:
    Imobiliaria() = default;
    ~Imobiliaria() = default;

    bool adicionarImovel(Imovel* imovel);
    string listaImoveis(int andar) const;
    Imovel* pesquisaImovel(string codigo) const;
    bool removeImovel(string codigo);
protected:
private:
    // Estamos a usar agregacao, idealmente deveriamos precaver o caso
    // em que um imovel pode ser apagado fora da classe
    // deviamos proteger com smart pointer, neste caso weak_ptr
    vector<Imovel*> imoveis;
};



#endif //IMOBILIARIA_H
