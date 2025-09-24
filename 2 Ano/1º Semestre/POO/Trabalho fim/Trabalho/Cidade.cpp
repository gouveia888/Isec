#include "Cidade.h"
#include <iostream>
#include <string>

Cidade::Cidade(const std::string &nome, int linha, int coluna)
        : nome(nome), linha(linha), coluna(coluna) {}

void Cidade::entrarCaravana(Caravana &caravana) {
    //verificar se caravana entrou na cidade
    // Adiciona a caravana ao vetor de caravanas
    caravanas.push_back(&caravana);
}

void Cidade::sairCaravana(Caravana &caravana) {
    // Remove a caravana do vetor de caravanas
    for (auto it = caravanas.begin(); it != caravanas.end(); ++it) {
        if (*it == &caravana) {
            caravanas.erase(it);
            break;
        }
    }
}

void Cidade::negociar(Caravana &caravana) {
    // Implementar lógica de negociação se necessário
}

std::string Cidade::getNome() const {
    return nome;
}

void Cidade::getPosicao(int &l, int &c) const {
    l = linha;
    c = coluna;
}


void Cidade::listarCaravanas() const {
    std::cout << "Caravanas na cidade " << nome << ":" << std::endl;
    for (const auto &caravana : caravanas) {
        caravana->exibirDetalhes(); // Chama o metodo para exibir detalhes da caravana
    }
}