//
// Created by Gouveia on 07/11/2024.
//

#include "Pessoa.h"
#include <sstream>

string Pessoa::descricao() const {
    std::ostringstream oss;
    oss << "Nome: " << nome
        << ", BI: " << BI
        << ", NIF: " << NIF;
    return oss.str();
}