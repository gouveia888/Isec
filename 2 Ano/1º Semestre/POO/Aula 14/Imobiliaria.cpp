//
// Created by Henrique Marques on 12/12/2024.
//

#include "Imobiliaria.h"
#include <sstream>

bool Imobiliaria::adicionarImovel(Imovel* imovel)
{
    if (imovel == nullptr)
        return false;
    imoveis.push_back(imovel);
    return true;
}

string Imobiliaria::listaImoveis(int andar) const
{
    ostringstream oss;
    oss << "Lista de imoveis no andar " << andar << ": "<< endl;
    for(auto it = imoveis.begin(); it != imoveis.end(); it++)
        if((*it)->obterAndar() == andar)
            oss << (*it)->obterDescricao() << endl;
    return oss.str();
}

Imovel* Imobiliaria::pesquisaImovel(string codigo) const
{
    for(auto it = imoveis.begin(); it != imoveis.end(); it++)
        if((*it)->obterCodigo() == codigo)
            return *it;
    return nullptr;
}

bool Imobiliaria::removeImovel(string codigo)
{
    for(auto it = imoveis.begin(); it != imoveis.end();)
    {
        if((*it)->obterCodigo() == codigo)
        {
            // erase devolve o iterador para o seguinte ao apagado
            imoveis.erase(it);
            return true;
        }
        ++it;
    }
    return false;
}