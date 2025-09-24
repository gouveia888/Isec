//
// Created by Henrique Marques on 12/12/2024.
//

#include "Peixe.h"

#include <ostream>

int Peixe::sequencia = 1000;

int Peixe::obtemPeso() const
{
    return peso;
}

int Peixe::obtemID() const
{
    return id;
}

string Peixe::obtemEspecie() const
{
    return especie;
}

ostream& operator<<(ostream& o, const Peixe& p)
{
    return o << "Peixe: " <<
                "ID: " << p.id <<
                ", Especie: " << p.especie <<
                ", Cor: " << p.cor <<
                ", Peso: " << p.peso;
}
