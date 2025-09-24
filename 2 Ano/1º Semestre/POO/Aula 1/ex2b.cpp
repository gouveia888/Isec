#include <iostream>

int main() {

    char name[20];
    int idade;
    std::cout << "Insira nome" << std::endl;
    std::cin >> name;
    std::cout << "Insira idade" << std::endl;
    std::cin >> idade;
    std::cout << "Nome: " << name << std::endl;
    std::cout << "Idade: " << idade << std::endl;

    return 0;
}
