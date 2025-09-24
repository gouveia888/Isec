#include <iostream>
#include <exception>

using namespace std;

static const int TAM = 10; //com o static apenas esta incluida neste ficheiro

struct Tabela{
    int matriz[TAM];
};

void insere (struct Tabela &tab, int valor=6){
    int i=0;
    for(i=0; i<TAM; i++)
        tab.matriz[i]=valor;
}

void imprime (const struct Tabela &tab){ //const para indicar que o valor interno da struct nao vai ser alterada
    int i=0;

    for(i=0; i<TAM; i++)
        cout << "Valor " << i <<" da matriz "<< tab.matriz[i] << "\n";
}

int obtem (const struct Tabela &tab, int pos){
    if(pos >= 0 && pos <= TAM){
        return tab.matriz[pos];
    }else{
        throw invalid_argument("Posicao invalida");
    }

}

bool atualiza (struct Tabela &tab, int i, int valor){
    if(i >= 0 && i <= TAM){
        tab.matriz[i] = valor;
        return true;
    }else{
        return false;
    }
}

int &elementEm(Tabela &tab, int i){
    if (i < 0 || i >= TAM){
        int erro = -1;
        throw invalid_argument("Posicao invalida");
        return erro;
    }
    return tab.matriz[i];
}


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
  /*  int a = 5, b = 10;
    cout <<  noexcept(seleciona(a, b, 'm')) << endl; //indica se a funçao pode devolver uma exceçao
    try{
        seleciona(a, b, 'r') = 0;
    } catch(const invalid_argument e){
        cout << e.what() << endl;
        a = 0;
        b = 0;
        //throw;// passa para a proxima funçao se estas estiverem encadeadas
    } catch (exception &e){
        cout << e.what() << endl;
    }

    cout << "a = " << a << "\n b = " << b;   // aparece 0 10
*/

   Tabela a;
   int b;

    insere(a);
    imprime(a);

    try{
        cout << "Valor da posicao pedida "<< obtem(a,2) << endl;

        cout << elementEm(a, 9) << endl;   // aparece um determinado valor
        elementEm(a,9) = 15;       // notar que a chamada à função fica do lado esquerdo da atribuição
        cout << elementEm(a, -9) << endl;

    }catch (const invalid_argument e){
        cout << e.what() << endl;
    }

}