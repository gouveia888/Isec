//
// Created by Henrique Marques on 05/12/2024.
//

#ifndef LOJA_H
#define LOJA_H
#include "../Imovel.h"

class Loja : public Imovel{
public:
    Loja(int area):
        Imovel(area, area*PRECO_METRO_QUADRADO, 0,
                geraIdentificador("loja")) {}
protected:
    string obterTipo() const override;
private:
    static const int PRECO_METRO_QUADRADO=15;
};

#endif //LOJA_H
