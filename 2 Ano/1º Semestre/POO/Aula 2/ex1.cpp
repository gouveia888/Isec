#include <iostream>

using namespace std;

namespace DataStore{

    bool dadosSaoValidos(string dados){
        if(dados.length() >= 5 && dados.length() <= 10)
            return true;
        return false;
    }

}


namespace UserInterface{
    bool dadosSaoValidos(string dados){
        /*if( dados.empty() && isupper(dados[0]))
            return true;
        return false;*/
        return !dados.empty() && isupper(dados[0]);
    }
}

//using DataStore::dadosSaoValidos;
using namespace UserInterface;

int main() {

    cout << DataStore::dadosSaoValidos("HelloWorld") << endl;
    cout << dadosSaoValidos("Hello") << endl;

    return 0;
}
