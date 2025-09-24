//
// Created by Gouveia on 07/11/2024.
//

#ifndef AULA9_PESSOA_H
#define AULA9_PESSOA_H
#include <string>


using namespace std;

class Pessoa {
public:
    Pessoa(const string & nome, int nBI, int nNIF)
            : nome(nome), BI(nBI), NIF(nNIF) {}
    string getNome() const { return nome; }
    int getNumeroBI() const { return BI; }
    int getNumeroNIF() const { return NIF; }
    void atualizarNome(const string& novoNome) {nome = novoNome;}
    string descricao() const
    Pessoa(const Pessoa &p): nome(p.nome), BI(p.BI),NIF(p.NIF)){}

private:
    string nome;
    int BI;
    int NIF;

};


#endif //AULA9_PESSOA_H
