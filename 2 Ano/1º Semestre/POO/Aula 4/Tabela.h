//
// Created by Gouveia on 03/10/2024.
//

#ifndef AULA2_TABELA_H
#define AULA2_TABELA_H

using namespace std;

class Tabela{

    static const int TAM = 10; //com o static apenas esta incluida neste ficheiro
    int matriz[TAM];


public:
    Tabela(int valor);

    Tabela(int a, int valor);

    Tabela(const Tabela &t);

    ~Tabela();

    void insere (int valor);

    void imprime () const;

    int obtem (int pos) const;

    bool atualiza (int i, int valor);

    int &elementEm(int i);

    bool procura(int valor) const ;

    bool contemValores(const Tabela& t) const;

};


#endif //AULA2_TABELA_H
