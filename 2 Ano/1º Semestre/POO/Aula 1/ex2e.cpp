#include <iostream>
#include <limits>

using namespace std;

int main() {

    string name;
    int idade;
    cout << "Insira nome: " << endl;
    getline(cin, name);

    while (true) {
        cout << "Insira idade" << endl;
        cin >> idade;
        if (cin.fail()) {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            continue;
        }
        if (idade <= 0) {
            cerr << "Invalid Input";
        } else {
            break;
        }

    }

    cout << "Nome: " << name << endl;
    cout << "Idade: " << idade << endl;
    cout << "Name size: " << name.length() << endl;

    /*for(int i=0; i < name.length(); i++){
        cout << name.at(i) << endl;
        cout << name[i] << endl;
    }
    */

    for(char c : name){
        cout << c << endl;
    }

    return 0;
}
