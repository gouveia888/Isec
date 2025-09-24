#include <stdio.h>

// Versão A
// Recebe nome de ficheiro de texto com notas de alunos
// Apresenta na consola o número de alunos e número de provas
void printDadosA(char *nomeF){
    FILE *f;
    int a, b;

    f = fopen(nomeF, "r");
    if(f == NULL){ printf("Erro no acesso ao ficheiro\n"); return;}

    while(fgetc(f) != ':')  // Procurar o caracter relevante -> ':'
        ;
    fscanf(f, "%d", &a);    // A seguir está um inteiro

    while(fgetc(f) != ':')  // Voltar a procurar o caracter relevante -> ':'
        ;
    fscanf(f, "%d", &b);    // A seguir está o outro inteiro

    fclose(f);
    printf("Versao A: Alunos: %d\tProvas: %d\n", a, b);
}

// Versão B
// Recebe nome de ficheiro de texto com notas de alunos
// Apresenta na consola o número de alunos e número de provas
void printDadosB(char *nomeF){
    FILE *f;
    int a, b;

    f = fopen(nomeF, "r");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro\n"); return;
    }

    fscanf(f, " %*[^:] : %d", &a);
    fscanf(f, " %*[^:] : %d", &b);

    fclose(f);
    printf("Versao B: Alunos: %d\tProvas: %d\n", a, b);
}

// Versão C
// Recebe nome de ficheiro de texto com notas de alunos
// Apresenta na consola o número de alunos e número de provas
void printDadosC(char *nomeF){
    FILE *f;
    int a, b;

    f = fopen(nomeF, "r");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro\n"); return;
    }

    fscanf(f, " Numero de alunos: %d", &a);
    fscanf(f, " Numero de provas: %d", &b);

    fclose(f);
    printf("Versao C: Alunos: %d\tProvas: %d\n", a, b);
}

// Recebe nome de ficheiro de texto com notas de alunos
// Apresenta na consola as notas finais de cada aluno
void calculaNotasFinais(char *nomeF){
    FILE *f;


    f = fopen(nomeF, "r");
    if(f == NULL){
        printf("Erro no acesso ao ficheiro\n"); return;
    }


    fclose(f);
}


int main(){

    // 3 versões para mostrar o número de alunos e de provas
    printDadosA("notas_ex9.txt");
    printDadosB("notas_ex9.txt");
    printDadosC("notas_ex9.txt");

    calculaNotasFinais("notas_ex9.txt");


    return 0;
}