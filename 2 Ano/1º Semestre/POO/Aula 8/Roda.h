//
// Created by Gouveia on 31/10/2024.
//

#ifndef AULA8_RODA_H
#define AULA8_RODA_H


class Roda{
    double diametro;
public:
    Roda();
    Roda(double d);
    Roda(const Roda &a);
    double obtemDiametro() const;
    ~Roda();
};


#endif //AULA8_RODA_H
