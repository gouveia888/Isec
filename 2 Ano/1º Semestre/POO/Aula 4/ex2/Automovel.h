//
// Created by Gouveia on 03/10/2024.
//
#include <iostream>
#ifndef EX2_AUTOMOVEL_H
#define EX2_AUTOMOVEL_H
using namespace std;

class Automovel {


    string matricula, marca, modelo;
    int combustivel;
public:
    Automovel();
    string Imprime();
    string getmatricula();
    string getmarca();
    string getmodelo();
    int getcombustivel();
};


#endif //EX2_AUTOMOVEL_H
