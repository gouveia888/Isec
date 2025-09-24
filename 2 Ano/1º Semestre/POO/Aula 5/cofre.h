//
// Created by Gouveia on 10/10/2024.
//
#include <string>
#ifndef AULA2_COFRE_H
#define AULA2_COFRE_H

using namespace std;

class cofre {

private:
    int codigo;
    int confirmacodigo; //usado apenas enquanto se esta a alterar o codigo
    bool aberto;
    const int codigo_desbloqueio;
    int num_tentativas;
    static const int NUM_OBJETOS = 20; //com static vai ser partilhada entre todos os objetos da class cofre
    string objetos[NUM_OBJETOS];
    static const int NUM_MAX_TENTATIVAS = 3;
    bool alterandoCodigo;

public:
    cofre(int codigo, int codigo_desbloqueio, bool aberto):codigo(codigo), codigo_desbloqueio(codigo_desbloqueio),
    aberto(aberto), num_tentativas(0),  alterandoCodigo(false), objetos{}, confirmacodigo(0){
    }

    cofre(int codigo_desbloqueio): codigo_desbloqueio(codigo_desbloqueio), aberto(true), codigo(0), num_tentativas(0),
    objetos{}, alterandoCodigo(false), confirmacodigo(0){
    }

    bool estaAberto() const;
    int numTentativasRestantes() const;
    string obtemobjetos() const;
    bool contemobjetos(const string &objeto) const;
    bool estaBloqueado() const;

    bool fechar();
    bool abrir(int codigo);
    bool desbloqueia(int codigo_desbloqueio);
    bool mudacodigo(int novocodigo, int codigoatual);
    bool adicionaObjeto(string novoObjeto);
    bool removeObjeto(string novoObjeto);
};


#endif //AULA2_COFRE_H
