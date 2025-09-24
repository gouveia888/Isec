//
// Created by Gouveia on 28/11/2024.
//

#include "ListaComprasSet.h"
#include <utility>
#include <sstream>

bool ListaComprasSet::adiciona(string nome, int qty){
    //pair e uma classe com 2 valores, first e second
    //o inset devolve um pair em que first e o iterator
    //second tem um bool que indica se o valor foi inserido ou nao (true/false)
    if(nome.empty() || qty == 0)
        return false;
    auto p = lista.insert(Compra(nome, qty));
    if(p.second== true) //verifica se foi adicionado novo elemento
        return true;
    auto it = p.first;
    //como o set e constante para fazer alteraçoes temos de apagar o atual e adicionar o novo
    int qty_atual= it->getQty(); //guardar os dados antes de apagar
    lista.erase(it); //apagar o atual
    lista.insert(Compra(nome, qty_atual + qty)); //adicionar novamente o elemento
    return true;

}
bool ListaComprasSet::removeQty(string nome, int qty){
    for(auto it = lista.begin(); it != lista.end(); it++){
        if(it->getNome() == nome){
            int qtdAtual = it->getQty();
            lista.erase(it);
            lista.insert(Compra(nome,qtdAtual-qty));
            return true;
        }
    }
    return false;
}

bool ListaComprasSet::elimina(string nome){
    //como apenas usamos o nome para comparar as compras entao podemos eliminar criando um objeto compra com o nome desejado
    return lista.erase(Compra(nome)) > 0;
}

string ListaComprasSet::obtemLista() const{
    ostringstream oss;
    oss << "Lista de compras: " << endl;
    for(auto it = lista.begin(); it != lista.end(); it++)
        oss << it->descricao() << endl;

    return oss.str();
}
int ListaComprasSet::eliminaTodosCom(int qty){
    set<Compra> listaApagar;
    for(auto it = lista.begin(); it != lista.end(); it++){
        if(it->getQty() == qty)
            listaApagar.insert(*it);
    }
    for(auto it = listaApagar.begin(); it != listaApagar.end(); it++){
        lista.erase(*it);
    }
    return listaApagar.size();
}

