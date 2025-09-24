#include <iostream>
#include <sstream>
#include <exception>
#include <cmath>
#include "Roda.h"
#include "Automovel.h"

using namespace std;

void mostra(Automovel o){
    cout << o.tostring() <<endl;
}

int main(){

    //Roda rodas[2] Se existisse um construtor por deefault
    //Roda rodas[2] = {Roda(10), Roda(20)};

    //Automovel a("Porche",500,20);
    //Automovel b("Citroen",200,18);

    //b=a;

    //cout << a.tostring() << endl;
    //cout << b.tostring() << endl;

    cout << "Hello world!" <<endl;
    //mostra(a);

    //Roda *roda = new Roda(20);
    //delete *roda;
    //roda = nullptr;

    int tam = 3;
    Roda *rodas = new Roda[tam];
    delete []rodas;

    return 0;
}
