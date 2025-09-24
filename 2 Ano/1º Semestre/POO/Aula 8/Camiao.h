//
// Created by Gouveia on 31/10/2024.
//

#ifndef AULA8_CAMIAO_H
#define AULA8_CAMIAO_H
#include "Roda.h"
#include <iostream>
#include <string>

using namespace std;



class Camiao {
    string marca;
    int potencia, num_rodas, diametro;
    Roda *rodas;

public:
    Camiao(string marca, int potencia, int num_rodas, int diametro);
    ~Camiao();
    string tostring() const;
    string cria();
    string getMarca();
    int getPotencia();
    int getNum_rodas();
    int getDiametro();
    void setMarca(string marca);
    void setNum_Rodas(int num);
    void setDiametro(int num);
    void setPotencia(int num);
    void eliminarRodas();
    void criarRodas(int n, int d);
    Camiao(const Camiao &a);
};


#endif //AULA8_CAMIAO_H
