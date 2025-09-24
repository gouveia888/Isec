//
// Created by Henrique Marques on 07/11/2024.
//

#ifndef PESSOA_H
#define PESSOA_H

#include<string>

using namespace std;

class Pessoa {
public:
    Pessoa(const string & nome, int nBI, int nNIF)
    : nome(nome), BI(nBI), NIF(nNIF) {}
    Pessoa(const Pessoa& p): nome(p.nome), BI(p.BI), NIF(p.NIF){}
    string getNome() const { return nome; }
    int getNumeroBI() const { return BI; }
    int getNumeroNIF() const { return NIF; }
    void atualizarNome(const string& novoNome) { nome = novoNome; }
    string descricao() const;
private:
    string nome;
    int BI;
    int NIF;
};

#endif //PESSOA_H
