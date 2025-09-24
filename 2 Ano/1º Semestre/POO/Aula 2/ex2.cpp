#include <iostream>

using namespace std;

void imprime(string s){
    cout << s;
}

void imprime(string s, int n){
    cout << s << n;
}

void imprime(int n, string s){
    cout << s << n;
}

int main(){
    imprime("programação orientada a objetos");
    imprime("horas por aula teórica ", 2);
    imprime(3, " horas em cada aula prática");
    return 0;
}