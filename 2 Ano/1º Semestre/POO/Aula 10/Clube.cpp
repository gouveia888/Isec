//
// Created by Henrique Marques on 07/11/2024.
//

#include "Clube.h"
#include<sstream>
#include <memory>

shared_ptr<const Pessoa> Clube::procuraJogador(int BI) const
{
    for (int i = 0; i < nJogadores; i++)
    {
        shared_ptr<const Pessoa> sp = jogadores[i].lock();
        if(!sp)
            continue;
        if(sp->getNumeroBI() == BI)
            return sp;
    }
    return nullptr;
}

bool Clube::adicionaJogador(const Pessoa* novoJogador)
{
    if(nJogadores == N_JOGADORES)
        return false;

    if(novoJogador.expire())

    const Pessoa* p = procuraJogador(novoJogador->getNumeroBI());
    if(p != nullptr) // Se novo jogador já faz parte do clube
        return false;

    jogadores[nJogadores++] = novoJogador;
    return true;
}

bool Clube::existeJogador(int BI) const
{
    return procuraJogador(BI) != nullptr;
}

bool Clube::removeJogador(int BI)
{
    for (int i = 0; i < nJogadores; i++)
    {
        if(jogadores[i]->getNumeroBI() == BI)
        {
            for(int j = i; j < nJogadores-1; j++) // nJogadores -1 para nao acedermos fora do array
                jogadores[j] = jogadores[j+1];
            --nJogadores;
            return true;
        }
    }
    return false;
}

string Clube::obtemListagem() const
{
    ostringstream oss;
    oss << "Clube: " << nome << endl;
    for (int i = 0; i < nJogadores; i++)
        oss << jogadores[i]->descricao() << endl;
    return oss.str();
}