//
// Created by Gouveia on 07/11/2024.
//

#include "Clube.h"
#include <sstream>

Pessoa* Clube::procurajogador(int BI) const{
    for( int i=0; i<nJogadores ; i++){
        if(jogadores[i]->getNumeroBI() == BI)
            return jogadores[i];
    }
    return nullptr;
}

bool Clube::adicionaJogador(Pessoa *novojogador){

    if(nJogadores == N_JOGADORES)
        return false;

    Pessoa *p = procurajogador(novojogador->getNumeroBI());
    if(p != nullptr)
        return false;
    jogadores[nJogadores++]=novojogador; //incrementa o numero de jogadores apos a atribuiçao

    return true;
}

bool Clube::existejogador(int BI) const{
    return procurajogador(BI) != nullptr; //se existir devolve true senao devolve false
}

bool Clube::removeJogador(int BI){
    for(int i = 0; i < nJogadores; i++){
        if(jogadores[i]->getNumeroBI()==BI){
            for(int j = i; j < nJogadores-1; j++) //nPessoas-1 para nao acederemos fora do array
                jogadores[j]=jogadores[j+1];
            --nJogadores; //a nivel de eficiencia melhor que nPessoas--
            return true;
        }
    }
    return false;
}

string Clube::obtemListagem() const{
    ostringstream oss;
    oss << "Registo do Clube" << this->nome << endl;
    for(int i = 0; i < nJogadores; i++){
        oss << this->jogadores[i]->descricao() << endl;
    }
    return oss.str();
}