#ifndef TRABALHO_BARBARA_H
#define TRABALHO_BARBARA_H

#include "Caravana.h"
#include "../Dados.h"


class Barbara : public Caravana {
private:
    int turnosativos;
    int maxTurnosAborrecidos;

public:
    // Construtor
    Barbara(const Dados &dados) : Caravana(0, 0, 0, 40, 0), turnosativos(0) {
        maxTurnosAborrecidos = dados.getduracao_barbaros();
    }

    // Destrutor
    virtual ~Barbara() override {}

    // Métodos
    void mover(Buffer &buffer); // Passa o buffer para atualizar a posição
    void Tempestade();
    void atualizar();
    void verificarConsumoAgua(); // Implementação do metodo virtual puro


};

#endif // TRABALHO_BARBARA_H