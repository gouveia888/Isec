#include <iostream>
#include <string>
#include <limits>

using namespace std;

string invert( const string &word){
    string invert = "";
            for(int i = word.length() - 1; i >= 0; i--){
                invert += word[i];
            }
    return invert;
}

int main(){
    while (true){
        string word;
        cout << "Insira uma palavra: " << endl;

        cin >> word;

        if(word == "fim"){
            break;
        }

        string invertedstring = invert(word);
        cout << invertedstring << endl;

        if(invertedstring == word){
            cout << "E um palindromo" << endl;
        }

        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << "Insira enter para continuar" << endl;
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }
}


