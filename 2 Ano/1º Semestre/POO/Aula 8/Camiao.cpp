//
// Created by Gouveia on 31/10/2024.
//

#include "Camiao.h"
#include <iostream>
#include <string>
#include <sstream>

#define N_RODAS_MIN 4

using namespace std;

Camiao::Camiao(string marca, int potencia, int n_rodas, int diametro): marca(marca), potencia(potencia), num_rodas(n_rodas), diametro(diametro){
    if(num_rodas < N_RODAS_MIN)
        num_rodas = N_RODAS_MIN;

    rodas = new Roda[num_rodas];

    for(int i = 0; i < num_rodas ; i++){
        rodas[i]= Roda(diametro);
    }
}

Camiao::~Camiao(){
    eliminarRodas();
    cout << "Camião destruido" << endl;
}

Camiao::Camiao(const Camiao &a) : marca(a.marca), potencia(a.potencia), num_rodas(a.num_rodas), diametro(a.diametro) {
    criarRodas(num_rodas, diametro);
}

string Camiao::tostring() const {
    ostringstream  oss;

    oss << this->marca << " " << this->potencia << " ";
    for(int i = 0; i <num_rodas; i++) // rodas r : rodas nao funciona porque para o compilador um ponteiro nao e considerado um array
        oss << this->rodas[i].obtemDiametro() << " ";

    return oss.str();
}

string Camiao::getMarca() {
    return this->marca;
}

int Camiao::getPotencia() {
    return this->potencia;
}

int Camiao::getNum_rodas() {
    return this->num_rodas;
}

int Camiao::getDiametro() {
    return this->diametro;
}

void Camiao::setMarca(string marca) {
    this->marca=marca;
}

void Camiao::setPotencia(int num) {
    this->potencia=num;
}

void Camiao::setNum_Rodas(int num) {
    this->num_rodas=num;
}

void Camiao::setDiametro(int num) {
    this->diametro=num;
}

void Camiao::eliminarRodas() {
    delete[] this->rodas;
    this->rodas = nullptr;
}

void Camiao::criarRodas(int n, int d) {
    eliminarRodas();
    this->diametro=d;
    this->setNum_Rodas(n);
    rodas = new Roda[num_rodas];
    for (int i = 0; i < num_rodas; i++) {
        this->rodas[i] = diametro;
    }
}