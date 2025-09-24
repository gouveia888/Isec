//
// Created by Gouveia on 07/11/2024.
//

#ifndef AULA9_CLUBE_H
#define AULA9_CLUBE_H
#include "Pessoa.h"

using namespace std;

class Clube {

public:
    static const int N_JOGADORES = 11;
    Clube(string nome):nome(nome),nJogadores(0) ,jogadores{}{}
    bool adicionaJogador(Pessoa *novojogador);
    bool existejogador(int BI) const;
    bool removeJogador(int BI);
    string obtemListagem() const;
    Clube(const Clube &clube):nome(clube.nome), jogadores{}, nJogadores(clube.nJogadores){
        for (int i = 0; i < N_JOGADORES; i++)
            jogadores[i] = clube.jogadores[i];
    }

private:
    string nome;
    int nJogadores;
    Pessoa* jogadores[N_JOGADORES];
    Pessoa* procurajogador(int BI) const;

};


#endif //AULA9_CLUBE_H
