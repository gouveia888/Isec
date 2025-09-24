//
// Created by Henrique Marques on 07/11/2024.
//

#include "Clube.h"
#include<sstream>

shared_ptr<const Pessoa> Clube::procuraJogador(int BI) const
{
    for (int i = 0; i < nJogadores; i++)
    {
        shared_ptr<const Pessoa> sp = jogadores[i].lock();
        if (!sp)
            continue;
        if(sp->getNumeroBI() == BI)
            return sp;
    }
    return nullptr;
}

bool Clube::adicionaJogador(weak_ptr<const Pessoa> novoJogador)
{
    for(int i = 0; i < nJogadores; i++){
        if(jogadores[i].expired()){
            for(int j = i; j < nJogadores-1; j++) // nJogadores -1 para nao acedermos fora do array
                jogadores[j] = jogadores[j+1];
            nJogadores--;
            i--;//temos que nos manter na mesma posicao porque puxamos todos os jogadores
                //uma posiçao para a esquerda
        }
    }

    if(nJogadores == N_JOGADORES)
        return false;
    if(novoJogador.expired())
        return false;
    shared_ptr<const Pessoa> p = procuraJogador(novoJogador.lock()->getNumeroBI());
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
        shared_ptr<const Pessoa> sp = jogadores[i].lock();
        if(sp && sp->getNumeroBI() == BI)
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
    oss << "Clube: " << nome << endl << "N Jogadores: " << nJogadores << endl;
    for (int i = 0; i < nJogadores; i++)
    {
        shared_ptr<const Pessoa> sp = jogadores[i].lock();
        // validar o lock
        if(sp)
            oss << sp->descricao() << endl;
    }
    return oss.str();
}

int Clube::obtemNJogadores() const{ //adicionar o obtemNJogadores a sitio correto
    int cont=0;
    for(int i = 0; i<nJogadores;i++)
        if(!jogadores[i].expired())
            cont++;
    return cont;
}