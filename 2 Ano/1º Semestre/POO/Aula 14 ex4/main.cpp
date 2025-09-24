#include <iostream>

#include "Aquario.h"
#include "Peixes/Carpa/Carpa.h"
#include "Peixes/Tubarao/Tubarao.h"

int main()
{
    Aquario aq;
    Peixe *carpa1 = new Carpa("cinza");
    Carpa carpa2("azul");

    Peixe *tubarao1 = new Tubarao("azul");
    Tubarao tubarao2("branco");

    aq.adicionaPeixe(*carpa1);
    aq.adicionaPeixe(*carpa1);
    aq.adicionaPeixe(carpa2);
    aq.adicionaPeixe(carpa2);
    aq.adicionaPeixe(*tubarao1);
    //aq.adicionaPeixe(tubarao2);

    cout << aq.obtemDescricao() << endl;
    for(int i = 0; i < 15; i++)
    {
        aq.alimenta(10);
        cout << aq.obtemDescricao() << endl;
    }

    delete carpa1;
    delete tubarao1;
    return 0;
}
