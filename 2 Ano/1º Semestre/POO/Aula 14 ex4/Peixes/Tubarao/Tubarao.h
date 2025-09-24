//
// Created by Henrique Marques on 12/12/2024.
//

#ifndef TUBARAO_H
#define TUBARAO_H
#include "../Peixe.h"


class Tubarao : public Peixe {
public:
    Tubarao(string cor): Peixe("tubarao", cor, 15) {}
    Tubarao(const Tubarao& t): Peixe("tubarao", t.cor, t.peso) {}
    ~Tubarao() override {}

    Peixe* clone() const override;
    void alimenta(int quantidade, Aquario& aq) override;
protected:
private:
};



#endif //TUBARAO_H
