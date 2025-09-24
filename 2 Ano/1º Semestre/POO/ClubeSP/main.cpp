#include <iostream>

#include "RegistoCivil.h"
#include "Clube.h"

int main()
{
    string listagem;
    // Testar as classes RegistoCivil e Clube
    RegistoCivil a("Portugal");
    weak_ptr<const Pessoa> p1 = a.adicionaPessoa("Joao", 123,456);
    weak_ptr<const Pessoa> p2 = a.adicionaPessoa("Tiago", 1234,4564);
    weak_ptr<const Pessoa> p3 = a.adicionaPessoa("Maria", 1235,4565);
    cout << "Nome Pessoa:" << a.getNomePessoa(123) << endl;
    cout << "Listagem\n" << a.obtemListagem() << endl;
    a.atualizaNome(123, "Roberto");

    cout << "Listagem\n" << a.obtemListagem() << endl;

    Clube c("Academica");
    c.adicionaJogador(p1);
    c.adicionaJogador(p2);
    c.adicionaJogador(p3);

    a.removePessoa(123);

    cout << a.obtemListagem() << endl;
    cout << c.obtemListagem() << endl;

    a.removePessoa(1235);

    cout << a.obtemListagem() << endl;
    cout << c.obtemListagem() << endl;


    // Existe 1 bug na class RegistoCivil
    // Existe pelo menos 1 bug na class Clube
    return 0;
}
