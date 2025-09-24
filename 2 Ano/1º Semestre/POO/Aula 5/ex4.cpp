#include <iostream>
#include <exception>
#include "cofre.h"

using namespace std;

int main(){

    cofre c(12345678); //esta aberto
    cofre a(9999, 87654321, false);

    cout << "Cofre c esta aberto? " << c.estaAberto() << endl;
    cout << "Cofre a esta aberto? " << a.estaAberto() << endl;

    cout << "Cofre c esta bloqueado? " << c.estaBloqueado() << endl;
    cout << "Cofre a esta bloqueado? " << a.estaBloqueado() << endl;

    cout << "Cofre c esta Numero tentativas restantes? " << c.numTentativasRestantes() << endl;
    cout << "Cofre a esta Numero tentativas restantes? " << a.numTentativasRestantes() << endl;

    cout << "Cofre c tem os objetos " << endl << c.obtemobjetos() << endl;
    cout << "Cofre a tem os objetos " << endl << a.obtemobjetos() << endl;

    c.mudacodigo(0,1234);
    c.mudacodigo(0,1234);

    c.fechar();
    cout << "Cofre c esta aberto? " << c.estaAberto() << endl;

    cout << "Introduza um codigo para abir o cofre c" << endl;
    int codigo = 0;
    cin >> codigo;
    c.abrir(codigo);
    cout << "Cofre c esta aberto? " << c.estaAberto() << endl;

    cout << "Cofre a esta aberto? " << a.estaAberto() << endl;
    cout << "Introduza um codigo para abir o cofre a" << endl;
    cin >> codigo;
    a.abrir(9999);
    cout << "Cofre c esta aberto? " << a.estaAberto() << endl;

    a.adicionaObjeto("Relogio");
    a.adicionaObjeto("Carteira");
    a.adicionaObjeto("Telemovel");

    a.abrir(9999);
    cout << a.obtemobjetos();

    a.removeObjeto("Relogio");
    cout << a.obtemobjetos();

    a.abrir(0);
    a.abrir(0);
    a.abrir(0);
    cout << "Cofre a esta bloqueado "  << a.estaBloqueado();
    a.desbloqueia(87654321);

    cout << "Cofre a esta bloqueado " << a.estaBloqueado();
    return 0;
}