#include <iostream>
#include <limits>

using namespace std;

int main() {

    int size = 20;
    char name[size];
    int idade;
    cout << "Insira nome: " << endl;
    cin.getline(name, size);

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
