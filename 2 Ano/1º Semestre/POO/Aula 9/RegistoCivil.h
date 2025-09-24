//
// Created by Gouveia on 07/11/2024.
//

#ifndef AULA9_REGISTOCIVIL_H
#define AULA9_REGISTOCIVIL_H

#include "Pessoa.h"

class RegistoCivil {

    string pais;
    Pessoa *pessoas;  //**pessoas cada pessoa seria um ponteiro para uma pessoa que ia ser gerada de forma dinamica
    static const int TAM_INICIAL=10;
    int nPessoas, maxPessoas;

    void redimensionarPessoas();
    Pessoa* procuraPessoa(int BI) const;
public:
    RegistoCivil(string pais): pais(pais), pessoas(nullptr), nPessoas(0), maxPessoas(TAM_INICIAL){
        pessoas = new Pessoa[TAM_INICIAL];
    }
    ~RegistoCivil(){ delete[] pessoas;}
    string getPais(){ return this->pais;}
    RegistoCivil(const RegistoCivil &registo):pais(registo.pais), nPessoas(registo.nPessoas), maxPessoas(registo.maxPessoas){
        pessoas = new Pessoa[maxPessoas];
                for(int i = 0; i < maxPessoas; i++)
                    pessoas[i] = registo.pessoas[i];
    }
    Pessoa* adicionaPessooa(string nome, int BI, int NIF);
    void adicionaPessooaFicheiro(string nomeFicheiro);
    bool removePessoa(int BI);
    string getNomePessoa(int BI) const;
    string obtemListagem() const;
    bool atualizaNome(int BI, string novoNome);
    int obtemNumeroPessoas() const {return nPessoas;}

};


#endif //AULA9_REGISTOCIVIL_H
