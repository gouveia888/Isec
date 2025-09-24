//
// Created by Gouveia on 28/11/2024.
//

#include "ListaCompras.h"
#include <sstream>

bool ListaCompras::adiciona(string nome, int qty){
    if(nome == "" || qty == 0) //nome.empty()
        return false;
//procurar por compra pelo mesmo nome para atualizar a quantidade
    /*for(int i = 0; i < lista.size(); i++){
        if(lista[i].getNome() == nome){
            lista[i].atualizaQty(qty);
            return true;
        }
    }*/

    for(/*array<Compra, 20>:: iterator*/auto it = lista.begin(); it != lista.end(); it++){
        if(it->getNome() == nome){
            it->atualizaQty(qty);
            return true;
        }
    }
    //procurar e adicionar uma nova compra
    for(auto it = lista.begin(); it != lista.end(); it++)
        if(it->getNome().empty() && it->getQty() == 0){
            *it = Compra(nome,qty);
            return true;
        }

    return false;
}

bool ListaCompras::removeQty(string nome, int qty){

    for(auto it = lista.begin(); it != lista.end(); it++){
        if(it->getNome() == nome){
            it->atualizaQty(-qty);
            return true;
        }
    }
    return false;
}

bool ListaCompras::elimina(string nome){
    for(auto it = lista.begin(); it != lista.end(); it++){
        if(it->getNome() == nome){
            *it = Compra();
            return true;
        }
    }
}

string ListaCompras::obtemLista() const{
    ostringstream oss;
    oss << "Lista de compras: " << endl;
    for(auto it = lista.begin(); it != lista.end(); it++){
        if(!it->getNome().empty() && it->getQty() != 0)
            oss << it->descricao() << endl;
    }
    return oss.str();
}