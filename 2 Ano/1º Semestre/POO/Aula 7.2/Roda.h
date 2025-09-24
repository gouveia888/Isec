//
// Created by Gouveia on 24/10/2024.
//

#ifndef AULA2_RODA_H
#define AULA2_RODA_H


class Roda{
    double diametro;
public:
    Roda();
    Roda(double d);
    Roda(const Roda &a);
    double obtemDiametro() const;
    ~Roda();
};


#endif //AULA2_RODA_H
