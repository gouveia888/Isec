//
// Created by Henrique Marques on 12/12/2024.
//

#ifndef AQUARIO_H
#define AQUARIO_H
#include <vector>

#include "Peixes/Peixe.h"

using namespace std;

class Aquario {
public:
    Aquario() = default;
    ~Aquario();

    void adicionaPeixe(const Peixe& peixe);
    bool eliminaPeixe(int id);
    int obtemPeixeQualquer(int id) const;
    const Peixe* obtemPeixe(int id) const;
    void alimenta(int quantidade);
    string obtemDescricao() const;
protected:
private:
    vector<Peixe*> peixes;
    // Flag para indicar se estamos a meio de um ciclo na funcao alimenta
    bool aAlimentar = false;
    vector<int> idsAEliminar; // Isto apenas e preciso porque nao usamos smart pointers
};



#endif //AQUARIO_H
