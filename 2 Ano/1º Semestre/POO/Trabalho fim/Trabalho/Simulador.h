#ifndef TRABALHO_SIMULADOR_H
#define TRABALHO_SIMULADOR_H

#include <string>
#include <vector>
#include <memory>
#include "Dados.h"
#include "Buffer.h"


class Simulador {
    Dados dados;
    Buffer buffer;

public:
    void iniciar();
    void processarComando(const std::string &cmd, std::istringstream &iss);
    void executarComandosDeArquivo(const std::string &nome_fich);
};

#endif //TRABALHO_SIMULADOR_H