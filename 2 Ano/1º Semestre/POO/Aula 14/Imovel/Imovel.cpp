//
// Created by Henrique Marques on 05/12/2024.
//

#include "Imovel.h"

#include <ostream>
#include <sstream>

int Imovel::sequencia = 1;

string Imovel::geraIdentificador(string tipo)
{
    return tipo + "_" + to_string(sequencia++);
}

string Imovel::obterCodigo() const
{
    return identificador;
}

int Imovel::obterPreco() const
{
    return preco;
}

int Imovel::obterAndar() const
{
    return andar;
}

string Imovel::obterDescricao() const
{
    ostringstream oss;
    oss << "Codigo: " << identificador << ", "
        << "Area: " << area << "m2, "
        << "Preco: " << preco << "€, "
        << "Andar:" << andar;
    return oss.str();
}

ostream& operator<<(ostream& os, const Imovel& imovel)
{
    os << imovel.obterDescricao() << endl;
    return os;
}