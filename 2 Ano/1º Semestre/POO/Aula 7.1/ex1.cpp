#include <iostream>
#include <sstream>
#include <exception>
#include <cmath>
#include "automovel.h"

using namespace std;

int main(){

        carro a(2000, "vermelho", "opel", "34-DF-58");
        //carro b = a;
        cout << a.getNcarros() << endl;
        cout << "Carro a" << a.getano() << " " << a.getmarca() <<  " " << a.getcor() << endl;
        //cout << "Carro b" << b.getano() << " " << b.getmarca() <<  " " << b.getcor() << endl;
    return 0;
}