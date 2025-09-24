//
// Created by Gouveia on 28/11/2024.
//

#include "Compra.h"

// includes e outras declarações omitidos
#include "Compra.h"
Compra::Compra(string n, int q) : nome(n), qty (q > 0? q : 0) {}
string Compra::getNome() const { return nome; }
int Compra::getQty() const { return qty; }
void Compra::atualizaQty(int dif) {
    qty += dif;
    if (qty < 0)
        qty = 0;
}
string Compra::descricao() const {
    return nome + ": " + to_string(qty);
}

bool Compra::operator<(const Compra &compra) const{
    return this->nome < compra.nome;
}