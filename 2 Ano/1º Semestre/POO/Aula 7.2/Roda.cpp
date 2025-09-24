//
// Created by Gouveia on 24/10/2024.
//

#include "Roda.h"
#include <iostream>

using namespace std;

Roda::Roda():diametro(21) {}

Roda::Roda(double d) : diametro (d > 1 && d < 30? d : 21) {}

Roda::Roda(const Roda &a) {
    this->diametro=a.diametro;
}

double Roda::obtemDiametro() const { return diametro; }

Roda::~Roda(){
    cout << "Rodas destruidas" <<endl;
}