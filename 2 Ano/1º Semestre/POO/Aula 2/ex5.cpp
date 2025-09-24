#include <iostream>
#include <exception>

using namespace std;

int & seleciona(int &a, int &b, char c) /*noexcept* quando nao devolve exceçao*/ {

    switch(c){
        case 'm':
             return a < b ? a : b;
        case 'M':
            return a > b ? a : b;
        case 'p':
            return a;
        case 'u':
            return b;
        default:
            throw invalid_argument("Caracter invalido");
    }
}

int main(){
    int a = 5, b = 10;
    cout <<  noexcept(seleciona(a, b, 'm')) << endl; //indica se a funçao pode devolver uma exceçao
    try{
        seleciona(a, b, 'm') = 0;
    } catch(const invalid_argument e){
        cout << e.what() << endl;
        a = 0;
        b = 0;
        // throw; passa para a proxima funçao se estas estiverem encadeadas
    } catch (exception &e){
        cout << e.what() << endl;
    }

    cout << "a = " << a << "\n b = " << b;   // aparece 0 10
}