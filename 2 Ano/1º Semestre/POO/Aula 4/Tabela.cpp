//
// Created by Gouveia on 03/10/2024.
//
#include <iostream>
#include "Tabela.h"

using namespace std;

Tabela::Tabela(int valor = 0):matriz{}{
    insere(valor);
    cout << "Tabela construida" << endl;
}

Tabela::Tabela(int a, int valor):matriz{}{
    matriz[0]=a;
    for(int i=1;i<TAM; i++)
        this->matriz[i]=matriz[i-1]+valor;
    //matriz[i]=a;
    //a+=b
    // ou matriz[i] = a+i*b
    cout << "Tabela construida" << endl;
}

Tabela::Tabela(const Tabela &t):matriz{}{
    cout << "Tabela construida por copia" << endl;
    for(int i=0; i<TAM; i++)
        this->matriz[i]=t.matriz[i];
}

Tabela::~Tabela(){
    cout << "Tabela destruida" << endl;
}

void Tabela::insere (int valor){
    int i=0;
    for(i=0; i<TAM; i++)
        this->matriz[i]=valor;
}

void Tabela::imprime () const{ //const para indicar que o valor interno da struct nao vai ser alterada
    int i=0;

    for(i=0; i<TAM; i++)
        cout << "Valor " << i <<" da matriz "<< this->matriz[i] << "\n";
    Tabela tmp; //este objeto e destruido quando a funçao imprime termina
}

int Tabela::obtem (int pos) const{
    if(pos >= 0 && pos <= TAM){
        return this->matriz[pos];
    }else{
        throw invalid_argument("Posicao invalida");
    }

}

bool Tabela::atualiza (int i, int valor){
    if(i >= 0 && i <= TAM){
        this->matriz[i] = valor;
        return true;
    }else{
        return false;
    }
}

int &Tabela::elementEm(int i){
    if (i < 0 && i >= TAM){
        //int erro = -1;
        //return erro;

        throw invalid_argument("Posicao invalida");
    }
    return this->matriz[i];
}

bool Tabela::procura(int valor) const {
    for(int i = 0; i<TAM; i++){
        if(this->matriz[i] == valor){
            return true;
        }
    }
    return false;
}

bool Tabela::contemValores(const Tabela& t) const{
    for(int i=0; i<TAM; i++){
        // if(!this.procura(t.matriz[i])) se fosse ao contrario
        if(!t.procura(this->matriz[i])){
            return false;
        }
    }
    return true;
}
