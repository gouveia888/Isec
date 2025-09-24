//
// Created by Henrique Marques on 12/12/2024.
//

#include "Carpa.h"

#include "../../Aquario.h"

Peixe* Carpa::clone() const
{
    return new Carpa(*this);
}

void Carpa::alimenta(int quantidade, Aquario& aq)
{
    this->peso += quantidade;
    if(this->peso > 50)
    {
        this->peso = 20;
        Carpa novoPeixe = Carpa(*this);
        novoPeixe.peso = PESO_INICIAL;
        aq.adicionaPeixe(novoPeixe);
    }
}
