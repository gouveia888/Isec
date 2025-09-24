#ifndef TRABALHO_CIDADE_H
#define TRABALHO_CIDADE_H

#include <vector>
#include <string>
#include "Caravana/Caravana.h"
class Caravana; // Declaração antecipada da classe Caravana

class Cidade {
private:
    std::string nome; // Nome da cidade
    int linha; // Posição da cidade na linha do mapa
    int coluna; // Posição da cidade na coluna do mapa
    std::vector<Caravana*> caravanas; // Vetor para armazenar as caravanas na cidade

public:
    // Construtor
    Cidade(const std::string &nome, int linha, int coluna);

    // Métodos para gerenciar caravanas
    void entrarCaravana(Caravana& caravana);
    void sairCaravana(Caravana& caravana);
    void negociar(Caravana& caravana);
    void listarCaravanas() const; // Lista todas as caravanas na cidade

    // Métodos de acesso
    std::string getNome() const;
    void getPosicao(int &l, int &c) const;
    void adicionarCaravana(Caravana *caravana);
    void removerCaravana(Caravana *caravana);
    int comprarTripulantes(int numTripulantes, int &moedas);
    int venderMercadorias(int quantidade, int &moedas);
};

#endif //TRABALHO_CIDADE_H