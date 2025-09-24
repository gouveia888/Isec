//
// Created by Henrique Marques on 05/12/2024.
//

#include "Apartamento.h"

#include <sstream>

string Apartamento::obterDescricao() const
{
    ostringstream oss;
    oss << Imovel::obterDescricao() << ", Assoalhadas: " << numeroAssoalhadas;
    return oss.str();
}

string Apartamento::obterTipo() const
{
    return "apartamento";
}

int Apartamento::obterAssoalhadas() const
{
    return numeroAssoalhadas;
}