//
// Created by Gouveia on 10/10/2024.
//

#include "cofre.h"
#include <sstream>

using namespace std;

bool cofre::estaAberto() const{
    return aberto;
}

bool cofre::estaBloqueado() const{
    if(num_tentativas == NUM_MAX_TENTATIVAS)
        return true;
    return false;
}

int cofre::numTentativasRestantes() const{
    return NUM_MAX_TENTATIVAS - num_tentativas;
}

string cofre::obtemobjetos() const{

    if (!this->aberto)
        return "Cofre fechado";

    ostringstream oss;

    for (int i=0; i < NUM_OBJETOS; i++){
        oss << "[" << i+1 << "]" << objetos[i] << endl;
    }

    return oss.str();
}

bool cofre::contemobjetos(const string &objeto) const {

    for(string obj : objetos){
        if (obj == objeto)
            return true;
    }
    return false;
}

bool cofre::fechar() {

    this->alterandoCodigo = false;
    if(estaBloqueado()){
        return false;
    }
    aberto = false;
    return true;
}

bool cofre::abrir(int codigo){

    this->alterandoCodigo = false;
    if(this->estaBloqueado()){
        return false;
    }

    if (codigo == this->codigo){
        aberto=true;
        num_tentativas = 0;
        return true;
    }

    ++num_tentativas;
    return false;
}

bool cofre::desbloqueia(int codigo_desbloqueio) {

    this->alterandoCodigo = false;
    if(!this->estaBloqueado() && codigo_desbloqueio == this->codigo_desbloqueio){
        num_tentativas = 0;
        aberto = true;
        return true;
    }
    return false;
}

bool cofre::mudacodigo(int novocodigo, int codigoatual){

    if(!this->estaAberto())
        return false;

    if(codigoatual == this->codigo ){
        if(this->alterandoCodigo){
            if(this->confirmacodigo == novocodigo){
                this->codigo=novocodigo;
                this->num_tentativas = 0;
                this->alterandoCodigo = false;
                return true;
            }else {
                this->confirmacodigo = 0;
                this->alterandoCodigo = false;
                return false;
            }
        }
        this->alterandoCodigo = true;
        this->confirmacodigo = novocodigo;
        return false; //Ainda nao alterou o codigo será na invocaçao seguinte
    }
    this->alterandoCodigo = false;
    return false;
}

bool cofre::adicionaObjeto(std::string novoObjeto) {

    this->alterandoCodigo = false;

    if(novoObjeto.empty())
        return false;

    if(!this->estaAberto())
        return false;

    for(int i = 0; i < NUM_OBJETOS ; i++){
        if(this->objetos[i].empty()){
            this->objetos[i] = novoObjeto;
            return true;
        }
    }
    return false;
}

bool cofre::removeObjeto(std::string novoObjeto) {

    this->alterandoCodigo = false;

    if(novoObjeto.empty())
        return false;

    if(!this->estaAberto())
        return false;

    for(int i = 0; i < NUM_OBJETOS ; i++){
        if(this->objetos[i] == novoObjeto){
            this->objetos[i] = ""; //this->objetos[i].clear;
            fechar();
            return true;
        }
    }
    return false;
}


