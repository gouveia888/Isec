//
// Created by Henrique Marques on 12/12/2024.
//

#ifndef CARPA_H
#define CARPA_H
#include "../Peixe.h"


class Carpa : public Peixe{

public:
    Carpa(string cor): Peixe("carpa", cor, PESO_INICIAL) {}
    Carpa(const Carpa& carpa): Peixe("carpa", carpa.cor, carpa.peso) {}
    ~Carpa() override {}

    Peixe* clone() const override;
    void alimenta(int quantidade, Aquario& aq) override;
protected:
private:
    static const int PESO_INICIAL = 5;
};



#endif //CARPA_H
