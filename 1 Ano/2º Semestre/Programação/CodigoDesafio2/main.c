#include <stdio.h>
#include "funcao.h"


int main() {

    submissao a1 = {123, {12,12,2023}, {11,45}};
    data d1 = {12, 12, 2023};

    submissao a2 = {123, {4,5,2023}, {10,45}};
    data d2 = {3, 5, 2023};

    submissao a3 = {123, {12,12,2023}, {12,3}};
    data d3 = {12, 12, 2023};

    submissao a4 = {123, {31,1,2023}, {12,7}};
    data d4 = {31, 1, 2023};

    printf("%d\n", calculaNota(&d1, a1));           // Deve devolver 10
    printf("%d %d %d\n", d1.dia, d1.mes, d1.ano);   // A variavel d1 passa a ser {13, 12, 2023}

    printf("%d\n", calculaNota(&d2, a2));           // Deve devolver 0
    printf("%d %d %d\n", d2.dia, d2.mes, d2.ano);   // A variavel d2 passa a ser {4,5,2023}

    printf("%d\n", calculaNota(&d3, a3));           // Deve devolver 2
    printf("%d %d %d\n", d3.dia, d3.mes, d3.ano);   // A variavel d3 passa a ser {13,12,2023}

    printf("%d\n", calculaNota(&d4, a4));           // Deve devolver 0
    printf("%d %d %d\n", d4.dia, d4.mes, d4.ano);   // A variavel d4 passa a ser {1,2,2023}

    return 0;
}
