//
// Created by Henrique Marques on 12/12/2024.
//

#ifndef PEIXE_H
#define PEIXE_H
#include <string>

class Aquario;
using namespace std;

class Peixe {
public:
    Peixe(string especie, string cor, int peso):
    especie(especie), cor(cor), peso(peso),
    id(sequencia++) {}
    virtual ~Peixe(){}

    virtual Peixe* clone() const = 0;
    int obtemPeso() const;
    int obtemID() const;
    string obtemEspecie() const;
    virtual void alimenta(int quantidade, Aquario& aq) = 0;
    friend ostream& operator<<(ostream& o, const Peixe& p);

protected:
    string especie, cor;
    int peso, id;
private:
    static int sequencia;
};



#endif //PEIXE_H
