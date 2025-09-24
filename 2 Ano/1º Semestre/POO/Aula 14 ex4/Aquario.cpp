//
// Created by Henrique Marques on 12/12/2024.
//

#include "Aquario.h"

#include <random>
#include <sstream>
//#include <__random/random_device.h>
#include <random>

void Aquario::adicionaPeixe(const Peixe& peixe)
{
    peixes.push_back(peixe.clone());
}

bool Aquario::eliminaPeixe(int id)
{
    for(auto it = peixes.begin(); it != peixes.end();)
    {
        if((*it)->obtemID() == id)
        {
            if(aAlimentar)
            {
                idsAEliminar.push_back(id);
            } else
            {
                delete *it;
                peixes.erase(it);
                return true;
            }
        }
        ++it;
    }
    return false;
}

int Aquario::obtemPeixeQualquer(int id) const
{
    random_device rd;
    default_random_engine gen(rd());
    vector<Peixe*> peixesEliminar;
    for(auto it = peixes.begin(); it != peixes.end(); ++it)
    {
        if((*it)->obtemID() != id && (*it)->obtemEspecie() != "tubarao")
        {
            peixesEliminar.push_back(*it);
        }
    }
    if (peixesEliminar.empty())
        return -1;
    uniform_int_distribution<> distr(0, peixesEliminar.size()-1);
    int random = distr(gen);
    return peixesEliminar[random]->obtemID();
}

const Peixe* Aquario::obtemPeixe(int id) const
{
    if(id == -1)
        return nullptr;
    for(auto it = peixes.begin(); it != peixes.end(); ++it)
    {
        if((*it)->obtemID() == id)
        {
            return (*it);
        }
    }
    return nullptr;
}

void Aquario::alimenta(int quantidade)
{
    aAlimentar = true;
    vector<Peixe*> copia(peixes);
    for(auto it = copia.begin(); it != copia.end(); ++it) // Percorrer a copia
    {
        (*it)->alimenta(quantidade, *this); // eliminar peixes vais ser feito no vector original
    }
    aAlimentar = false;

    // Apenas necessário para lidar com o delete da memoria dinamica
    for(auto it = idsAEliminar.begin(); it != idsAEliminar.end(); ++it)
        eliminaPeixe(*it);
    idsAEliminar.clear();
}

string Aquario::obtemDescricao() const
{
    ostringstream oss;
    oss << "Aquario: " << endl;
    for(auto it = peixes.begin(); it != peixes.end(); ++it)
        oss << *(*it) << endl;
    return oss.str();
}

Aquario::~Aquario()
{
    for(auto p : peixes)
    {
        delete p;
    }
}