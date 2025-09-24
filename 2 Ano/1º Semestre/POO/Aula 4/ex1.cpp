#include <iostream>
#include <exception>
#include "Tabela.h"

using namespace std;

/*
int & seleciona(int &a, int &b, char c) /*noexcept* quando nao devolve exceçao {

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
}*/

void recebe (Tabela t){

}

Tabela devolve (){
    Tabela t;
    return t;
}

Tabela inicializa(){
    Tabela tmp(1);
    Tabela t2 = tmp;
    return t2;
}

int main(){
   /* int a = 5, b = 10;
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
*/
   Tabela a, a2(18), a3(0,10); // a é um objeto do tipo tabela (class)
   int b;

    a.insere(10);
/*

    try{
        cout << "Valor da posicao pedida "<< a.obtem(2) << endl;

        cout << a.elementEm(9) << endl;   // aparece um determinado valor
        a.elementEm(9) = 15;       // notar que a chamada à função fica do lado esquerdo da atribuição
        cout << a.elementEm(9) << endl;

    }catch (const invalid_argument e){
        cout << e.what() << endl;
    }*/

     //a2.imprime();
       Tabela tab3(0,10);
     // tab3.imprime();

    a.imprime();
    tab3.imprime();
    cout << a.contemValores(tab3) << endl;


    Tabela tab;
    recebe(tab);

    tab3 = a; //vai invocar o contrutor por copia

    Tabela dev = devolve();

}