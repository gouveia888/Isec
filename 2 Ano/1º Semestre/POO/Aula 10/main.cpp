//FICHA 6

//a) Composição

#include <iostream>
#include "RegistoCivil.h"
#include "Clube.h"

using namespace std;

int main() {

    RegistoCivil rc("Portugal");
    weak_ptr<Pessoa> p= rc.adicionaPessoa("Henrique", 123, 1223);
    weak_ptr<Pessoa> p2= rc.adicionaPessoa("Afonso", 124, 121243);

    cout << p->descricao() << endl;
    cout << rc.obtemListagem() << endl;
    rc.atualizaNome(123, "Joao");
    cout << p->descricao() << endl;
    cout << rc.obtemListagem();

    Clube *aac = new Clube("Academica");
        aac->adicionaJogador(p);
        aac->adicionaJogador(p2);
        cout << aac->obtemListagem() << endl;
    delete aac;
    return 0;
}
