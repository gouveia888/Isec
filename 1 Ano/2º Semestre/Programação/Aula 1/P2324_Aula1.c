// Programação 2023/24
// Aula Prática 1

#include <stdio.h>

// Recebe: Tabela de inteiros a com tamanho tam
// Mostra na consola os valores armazenados na tabela
void mostraTab(int a[], int tam){
    int i;

    for(i=0; i<tam; i++)
        printf("%d\t", a[i]);
    putchar('\n');
}

// Exercicio 1
// Recebe: Tabela de inteiros a com tamanho tam
// Devolve maior valor armazenado na tabela
int maior(int a[], int tam){
    int i, m = a[0];
    for(i=1; i<tam; i++)
        if(a[i] > m)
            m = a[i];
    return m;
}

// Exercicio 2
// Recebe: Tabela de inteiros a com tamanho tam
// Devolve posição do maior valor armazenado na tabela
int posMaior(int a[], int tam){
    int i, pos=0;
    for(i=1; i<tam; i++)
        if(a[i] > a[pos]){
            pos = i;
        }
    return pos;
}

// Exercicio 3
// Recebe: Tabela de inteiros a com tamanho tam
// Devolve número de ocorrências do maior valor na tabela
int contaMaior(int a[], int tam){
    int i, max = 0, cont=0;
    for(i=1; i<tam; i++)
        if(a[i] > a[max]){
            max=i;
            cont=1;
        }else if(a[max]==a[i])
            cont++;

    return cont;
}

// Exercicio 4
int Maisvezes(int a[], int tam){

    int i,j cont=1, num_vezes=0, maior;

    for(i=0; i<tam; i++){
        for(j=i+1;j<i;j++)
            if(a[j]==a[i])
                cont++;
        if(cont > num_vezes){
            num_vezes=conta;
            maior=v[i];
        }else if{
            if//conta==numvezes e vi maior que mais frequente
        }

    }

    return mais frequente;
}

int main(){

    int tab1[8] = {3, 6, 8, 8, 10, 1, 4, 2};
    int tab2[6] = {5, 5, 5, 9, 1, 9};

    printf("Tabela 1:\n");
    mostraTab(tab1, 8);
    printf("Maior: %d\n", maior(tab1, 8));
    printf("Pos Maior: %d\n", posMaior(tab1, 8));
    printf("Conta Maior: %d\n", contaMaior(tab1, 8));
    printf("Mais Comum: %d\n\n", Maisvezes(tab1, 8));

    printf("Tabela 2:\n");
    mostraTab(tab2, 6);
    printf("Maior: %d\n", maior(tab2, 6));
    printf("Pos Maior: %d\n", posMaior(tab2, 6));
    printf("Conta Maior: %d\n", contaMaior(tab2, 6));
    printf("Mais Comum: %d\n\n", Maisvezes(tab2, 6));

    return 0;
}
