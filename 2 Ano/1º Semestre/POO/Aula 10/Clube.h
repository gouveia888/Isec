//
// Created by Henrique Marques on 07/11/2024.
//

#ifndef CLUBE_H
#define CLUBE_H

#include"Pessoa.h"
#include<string>
#include <memory>

using namespace std;

class Clube {
public:
    static const int N_JOGADORES = 11;
    Clube(string nome): nome(nome), jogadores{}, nJogadores(0) {}
    Clube(const Clube &clube):
            nome(clube.nome), jogadores{}, nJogadores(clube.nJogadores)
    {
        for(int i = 0; i < N_JOGADORES; i++)
            jogadores[i] = clube.jogadores[i];
    }
    bool adicionaJogador(weak_ptr<Pessoa> novoJogador);
    bool existeJogador(int BI) const;
    bool removeJogador(int BI);
    string obtemListagem() const;

private:
    string nome;
    weak_ptr<Pessoa> jogadores[N_JOGADORES];
    int nJogadores;

    shared_ptr<const Pessoa> procuraJogador(int BI) const;
};



#endif //CLUBE_H
