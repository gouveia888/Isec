//
// Created by Henrique Marques on 07/11/2024.
//

#ifndef REGISTOCIVIL_H
#define REGISTOCIVIL_H
#include <memory>
#include "Pessoa.h"

class RegistoCivil {
public:
    RegistoCivil(string pais): pais(pais), pessoas(nullptr),
    nPessoas(0), maxPessoas(TAM_INICIAL)
    {
        pessoas = new shared_ptr<Pessoa>[TAM_INICIAL];
    }
    RegistoCivil(const RegistoCivil& registo):
    pais(registo.pais), nPessoas(registo.nPessoas),
    maxPessoas(registo.maxPessoas)
    {
        pessoas = new shared_ptr<Pessoa>[maxPessoas];
        for (int i = 0; i < maxPessoas; i++)
            pessoas[i] = make_shared<Pessoa>(*registo.pessoas[i]);
    }
    ~RegistoCivil(){ delete[] pessoas; }
    string getPais() { return pais; }

    weak_ptr<const Pessoa> adicionaPessoa(string nome, int BI, int NIF);
    void adicionaPessoasFicheiro(string nomeFicheiro);
    bool removePessoa(int BI);
    string getNomePessoa(int BI) const;
    string obtemListagem() const;
    bool atualizaNome(int BI, string novoNome);
    int obtemNumeroPessoas() const { return nPessoas; }

private:
    string pais;
    shared_ptr<Pessoa>* pessoas;
    int nPessoas;
    int maxPessoas;
    static const int TAM_INICIAL = 10;

    void redimensionarPessoas(); // Esta funcao e privada
    shared_ptr<Pessoa> procuraPessoa(int BI) const;
};



#endif //REGISTOCIVIL_H
