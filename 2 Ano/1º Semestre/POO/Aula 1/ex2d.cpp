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

    return 0;
}
