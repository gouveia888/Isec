//
// Created by Henrique Marques on 12/12/2024.
//

#include "Tubarao.h"

#include "../../Aquario.h"

Peixe* Tubarao::clone() const
{
    return new Tubarao(*this);
}

void Tubarao::alimenta(int quantidade, Aquario& aq)
{
    if (this->peso > 20)
        this->peso--;
    else if (this->peso < 5)
        aq.eliminaPeixe(this->id);
    else
    {
        int peixecomerID = aq.obtemPeixeQualquer(this->id);
        if(peixecomerID == -1)
            this->peso -= 2;
        else
        {
            const Peixe* p = aq.obtemPeixe(peixecomerID);
            this->peso += p->obtemPeso();
            aq.eliminaPeixe(peixecomerID);
        }
    }

}
