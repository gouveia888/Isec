#include <iostream>
#include "Automovel.h"

using namespace std;

int main() {

    Automovel a;
    cout << a.Imprime();
    cout << a.getmatricula();
    cout << a.getcombustivel();

    return 0;
}
