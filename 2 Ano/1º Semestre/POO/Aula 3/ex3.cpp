#include <iostream>

using namespace std;

/*int multiplica(){
    return 1;
}

int multiplica(int a){
    return a;
}

int multiplica(int a, int b){
    return a*b;
}

int multiplica(int a, int b, int c){
    return a*b*c;
}*/

int multiplica(int a = 1, int b = 1, int c = 1){
    return a*b*c;
}

int main(){
    cout << "\n" << multiplica() << "\n" << multiplica(5);
    cout << endl << multiplica(2,3) << endl << multiplica(2,3,4);
}