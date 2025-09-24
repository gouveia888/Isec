// NOTAS IMPORTANTES

// 1. Altere o nome deste ficheiro. Deve passar a ter o seu numero de aluno como nome, mantendo a extensão .c
// Por exemplo, se o número de aluno for 1234567, o ficheiro deve passar a chamar-se: 1234567.c

// 2. Complete a sua identificação nas linhas 11 e 12

// 3. Só pode escrever código dentro da função desafio1(). Não pode alterar mais nada neste ficheiro
// Esta função não deve escrever nada na consola

// Nome completo: Tiago Gouveia Filipe
// Número de aluno: 2019112767

#include <stdio.h>

// Recebe:
// Endereço inicial de uma tabela de inteiros (tab)
// Dimensão de uma tabela de inteiros (tam)
// Endereço de uma variável inteira (contaP)

// Devolve número de elementos duplicados (número de elementos que aparecem exatamente 2 vezes na tabela)
// Coloca na variável referenciada por contaP o número de picos do array
// Considera-se que um pico é um elemento do array que está rodeado por valores menores à sua esquerda e à sua direita

int desafio1(int *tab, int tam, int *contaP){

    int i,j,cont=0,pares=0;

    for(i=1;i<tam-1;i++){
        if(tab[i]>tab[i-1] && tab[i]>tab[i+1])
            (*contaP)++;
    }

    for(i=0;i<tam;i++){
        cont=0;
        for(j=i+1;j<tam;j++){
            if(tab[i]==tab[j])
                cont++;
        }
        if(cont==1)
            pares++;
        //if(cont>1)
          //  pares--;
    }
return pares;
}

int main() {
    int tab1[5] = {5, 3, 3, 2, 2};
    int tab2[10] = {-3, -2, 0, 0, 1, 4, 3, -2, 9, 1};
    int tab3[8] = {1, 1, 4, 10, 4, 8, 1, 9};
    int c1=0, c2=0, c3=0, d1, d2, d3;

    d1 = desafio1(tab1, 5, &c1);
    d2 = desafio1(tab2, 10, &c2);
    d3 = desafio1(tab3, 8, &c3);

    printf("%d %d %d %d %d %d\n", d1, c1, d2, c2, d3, c3);
    return 0;
}
