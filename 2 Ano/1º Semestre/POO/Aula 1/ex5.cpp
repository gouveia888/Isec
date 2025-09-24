#include <iostream>
#include <string>
#include <limits>
#include <sstream>

using namespace std;

string numbersList[] = {"um", "dois", "tres",
                    "quatro", "cinco", "seis",
                    "sete", "oito", "nove", "dez"};

string to_word(int number) {
    if (number <= 0 || number > 10) {
        return "";
    }
    return numbersList[number - 1];
}

int to_digit (string word){
    for(int i=0; i < sizeof(numbersList)/sizeof(numbersList[0]); i++){
        if(word == numbersList[i]){
            return i+1;
        }
    }
}

int main(){
    while (true){
        string input;
        cout << "Insira um numero: " << endl;
        cin >> input

        if(input == "fim"){
            break;
        }

        istringstream iss(input);
        int number;

        if(iss >> number){
            //input e um digito
            cout << to_word(number) << endl;
        }else{
            //input e uma palavra
            cout << to_digit(input) << endl;
        }

        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << "Insira enter para continuar" << endl;
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }
}


