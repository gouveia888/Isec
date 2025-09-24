//
// Created by Gouveia on 28/11/2024.
//

#include "ListaComprasVetor.h"
#include <sstream>

bool ListaComprasVetor::adiciona(string nome, int qty){

    if(nome.empty() || qty == 0)
        return false;

    for(auto it = lista.begin(); it != lista.end(); it++){
        if(it->getNome() == nome){
            it->atualizaQty(qty);
            return true;
        }
    }

    lista.push_back(Compra(nome, qty));
    return true;
}
bool ListaComprasVetor::removeQty(string nome, int qty){

    for(auto it = lista.begin(); it != lista.end(); it++){
        if(it->getNome() == nome){
            it->atualizaQty(-qty);
            return true;
        }
    }
    return false;

}

bool ListaComprasVetor::elimina(string nome){
    for(auto it = lista.begin(); it!=lista.end();){
        if(it->getNome()==nome){
            it=lista.erase(it);
            return true;
        }
        it++;
    }

    return false;
}

string ListaComprasVetor::obtemLista() const{

    ostringstream oss;
    oss << "Lista de compras: " << endl;
    for(auto it = lista.begin(); it != lista.end(); it++)
            oss << it->descricao() << endl;

    return oss.str();
}

int ListaComprasVetor::eliminaTodosCom(int qty){
    int cont=0;

    for(auto it = lista.begin(); it!=lista.end();){
        if(it->getQty() == qty){
            it=lista.erase(it);
            cont++;
        }else{
            it++;
        }
    }

    return cont;
}