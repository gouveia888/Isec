//
// Created by Henrique Marques on 05/12/2024.
//

#ifndef APARTAMENTO_H
#define APARTAMENTO_H
#include "../Imovel.h"

class Apartamento : public Imovel{
public:
    Apartamento(int area, int andar, int numeroAssoalhadas) :
        Imovel(area, area*PRECO_METRO_QUADRADO, andar,
            Imovel::geraIdentificador("apartamento")),
        numeroAssoalhadas(numeroAssoalhadas) {}

    string obterDescricao() const override;
    int obterAssoalhadas() const;
protected:
    string obterTipo() const override;
private:
    int numeroAssoalhadas;
    static const int PRECO_METRO_QUADRADO = 10;
};


#endif //APARTAMENTO_H
