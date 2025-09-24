//
// Created by Henrique Marques on 05/12/2024.
//

#ifndef IMOVEL_H
#define IMOVEL_H
#include <string>
using namespace std;

class Imovel {
public:
    Imovel(int area, int preco, int andar, string identificador) :
        area(area), preco(preco),
        andar(andar), identificador(identificador){}

    Imovel(const Imovel& imovel) = delete;
    Imovel& operator=(const Imovel& o) = delete;

    string obterCodigo() const;
    int obterPreco() const;
    int obterAndar() const;
    virtual string obterDescricao() const;
    friend ostream& operator<<(ostream& os, const Imovel& imovel);
protected:
    static string geraIdentificador(string tipo);
    virtual string obterTipo() const = 0;
private:
    string identificador;
    int area, preco, andar;
    static int sequencia;
};



#endif //IMOVEL_H
