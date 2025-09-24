//
// Created by Gouveia on 03/10/2024.
//
#include <sstream>

#include "Automovel.h"
using namespace std;

Automovel::Automovel(){
    marca="Opel";
    combustivel=50;
    matricula="23-AD-76";
    modelo="corsa";
}

string Automovel::Imprime(){
    Automovel b;
    ostringstream oss;

    oss << "O carro com a matricula " << b.matricula << " com " << b.combustivel << "l de combustivel da marca " << b.marca << " do modelo " << b.modelo << endl;

    return oss.str();
}

string Automovel::getmatricula(){
 ostringstream oss;

    oss << this->matricula << endl;

    return oss.str();
}

string Automovel::getmarca(){
    ostringstream oss;

    oss << this->marca << endl;

    return oss.str();
}

string Automovel::getmodelo(){
    ostringstream oss;

    oss << this->modelo << endl;

    return oss.str();
}

int Automovel::getcombustivel(){

    return this->combustivel;
}