#include <iostream>
#include <stdio.h>

int main() {

    char name[20];
    int idade;

    printf("Insira nome ");
    scanf("%[^\n]s", &name);
    printf("\nInsira idade: ");
    scanf("%d", &idade);
    printf("Nome: %s \nIdade: %d", name, idade);



    return 0;
}
