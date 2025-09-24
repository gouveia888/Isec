#include <iostream>
#include <exception>

using namespace std;



class Tabela{

    static const int TAM = 10; //com o static apenas esta incluida neste ficheiro
    int matriz[TAM];


public:
    Tabela(int valor = 0){
        insere(valor);
    }

    Tabela(int a, int valor){
        matriz[0]=a;
        for(int i=1;i<TAM; i++)
            this->matriz[i]=matriz[i-1]+valor;
        //matriz[i]=a;
        //a+=b
        // ou matriz[i] = a+i*b
    }

    void insere (int valor){
        int i=0;
        for(i=0; i<TAM; i++)
            this->matriz[i]=valor;
    }

    void imprime () const{ //const para indicar que o valor interno da struct nao vai ser alterada
        int i=0;

        for(i=0; i<TAM; i++)
            cout << "Valor " << i <<" da matriz "<< this->matriz[i] << "\n";
    }

    int obtem (int pos) const{
        if(pos >= 0 && pos <= TAM){
            return this->matriz[pos];
        }else{
            throw invalid_argument("Posicao invalida");
        }

    }

    bool atualiza (int i, int valor){
        if(i >= 0 && i <= TAM){
            this->matriz[i] = valor;
            return true;
        }else{
            return false;
        }
    }

    int &elementEm(int i){
        if (i < 0 && i >= TAM){
            //int erro = -1;
            //return erro;

            throw invalid_argument("Posicao invalida");
        }
        return this->matriz[i];
    }

};



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

    /*a.insere(1);
    a.imprime();

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
        tab3.imprime();

}