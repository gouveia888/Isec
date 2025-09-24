#include <iostream>
#include <exception>
#include <cmath>
#include "Roda.h"
#include "Automovel.h"
#include "Camiao.h"

using namespace std;

void mostra(Automovel o){
    cout << o.tostring() <<endl;
}

Roda* cria(int n){
    Roda *rodas = new Roda[n];
    return rodas;
}

string cria(){
    Camiao a("SCANIA",350,4,22);
    return "feito";
}

void faz(){
    Camiao a("SCANIA",350,4,22);
    Camiao b("Renoult",550,6,25);

    //atribuir b a a
    a.setMarca(b.getMarca());
    a.setPotencia(b.getPotencia());
    a.criarRodas(b.getNum_rodas(),b.getDiametro());

    cout << a.tostring() << endl;
    cout << b.tostring() << endl;
}

void func(Camiao &a){
    cout << a.tostring() << endl;
}

int main(){

    //Roda rodas[2] Se existisse um construtor por deefault
    //Roda rodas[2] = {Roda(10), Roda(20)};
/*
    Automovel a("Porche",500,20);
    Automovel b("Citroen",200,18);

    b=a;

    cout << a.tostring() << endl;
    cout << b.tostring() << endl;
*/
    cout << "Hello world!" <<endl;
    //mostra(a);

    //Roda *roda = new Roda(20);
    //delete *roda;
    //roda = nullptr;

    /*
    int tam = 3;
    Roda *rodas = cria(tam);

    for(int i = 0; i < tam ; i++){
        cout << rodas->obtemDiametro() <<endl;
    }

    delete []rodas;
*/
    Camiao a("MAN", 300,6,24);

    //cout << cria() << endl;

    //faz();
    func(a);

    cout << a.tostring() << endl;

    return 0;
}
