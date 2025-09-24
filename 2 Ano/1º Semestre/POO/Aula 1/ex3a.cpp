#include <iostream>
#include <limits>
#include <string>

using namespace std;

void compare_fernando(string name){
    if(name == "Fernando")
        cout << "Eu conheco o Fernando" << endl;
}

void split_name(string completaName){
    int previousPos = 0;
    int pos;
    while((pos = completaName.find(' ', previousPos)) != string::npos){
        string n = completaName.substr(previousPos, pos - previousPos);
        cout << n << endl;
        previousPos = pos + 1;
        compare_fernando(n);
    }
    cout << completaName.substr(previousPos) << endl;
}

int main() {

    string name;
    cout << "Enter your name: " << endl;
    getline(cin, name);
    split_name(name);

    return 0;
}
