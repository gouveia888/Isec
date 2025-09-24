//
// Created by Gouveia on 24/10/2024.
//

#include "Automovel.h"
#include <sstream>
#include <iostream>

using namespace std;

Automovel::Automovel(string marca, int potencia, double diametro):
            marca(marca), potencia(potencia), rodas{}{

        for(int i = 0; i < N_RODAS ; i++)
            rodas[i] = Roda(diametro); //na atribuiçao da roda ao array ele destroi a roda ja existente criada anteriormente e atribui ao array a nova roda
}

Automovel::~Automovel(){
    cout << "Auromovel destruido" << endl;
}

Automovel::Automovel(const Automovel &a){
    cout << "Automovel contruido por copia" << endl;
    this->marca=a.marca;
    this->potencia=a.potencia;

    for(int i = 0; i < N_RODAS ;i++)
        this->rodas[i]= Roda(a);
        //this->rodas[i]=Roda(a.rodas[i].obtemDiametro());
}

string Automovel::tostring() const {
    ostringstream  oss;

    oss << this->marca << " " << this->potencia << " ";
    for(Roda r : rodas)
       oss << r.obtemDiametro() << " ";

    return oss.str();
}

Automovel& Automovel::operator=(const Automovel &a){
    cout << "Atrubuindo automovel" << endl;

    if(this == &a)
        return *this;

    this->marca=a.marca;
    this->potencia=a.potencia;

    for(int i = 0; i < N_RODAS ;i++)
        this->rodas[i]=a.rodas[i];

    return *this;