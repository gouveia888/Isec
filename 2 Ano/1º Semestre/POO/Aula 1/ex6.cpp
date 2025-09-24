#include <iostream>
#include <string>
#include <limits>
#include <sstream>
#include <fstream>

using namespace std;

string numberList[] = {"um", "dois", "tres",
                    "quarto", "cinco" , "seis",
                    "sete", "oito", "nove", "dez"};

string to_word(int number) {
  if (number <= 0 || number > 10) {
    return "";
  }
  return numberList[number-1];
}

int to_digit(string word) {
  for (int i = 0; i < sizeof(numberList)/size(numberList[0]); i++) {
    if (word == numberList[i]) {
      return i+1;
    }
  }
  return 0;
}

int main() {

//
    string rfileName;
    cout << "FileName: ";
    cin >> rfileName;

    string wfileName;
    cout << "FileName to write: ";
    cin >> wfileName;

//open file
//class ifstream ou ofstream criamos uma variavel rfile e passamos o contrutor rfileName
    ifstream rfile(rfileName);
    ofstream wfile(wfileName);

//verifica se ficheiro existe
    if(!rfile.is_open()){
        cout << "Error opening file" << rfileName <<  endl;
        return -1;
    }
    if(!wfile.is_open()){
        cout << "Error opening file to write" << wfileName <<  endl;
        return -1;
    }

// Enquanto le do ficheiro permanece no ciclo

    string input;
    while (rfile >> input) {

        if (input == "fim") {
            break;
        }

        istringstream iss(input);
        int number;
        if(iss >> number) {
          // Input is digit
          wfile << to_word(number) << endl;
        } else {
          // Input is a word
          wfile << to_digit(input) << endl;
        }

        /* Ignorar primeiro enter e depois enter para o proximo na impressao no cout
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
        cout << "Press enter" << endl;
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
         */
    }
    //fechar ficheiro
    rfile.close();
    wfile.close();
}