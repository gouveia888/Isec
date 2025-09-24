#include <stdio.h>
#include <string.h>

// Recebe um mês escrito em português
// Escreve na consola o nome do mês traduzido para ingles
void traduz(char *mes){
    char *pt[12] = {"Janeiro", "Fevereiro", "Marco", "Abril", "Maio", "Junho",
                    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
    char *eng[12] = {"January", "February", "March", "April", "May", "June",
                     "July", "August", "September", "October", "November", "December"};
    int i;
    for(i=0;i<12;i++)
        if(strcmp(mes, pt[i]) == 0)
            puts(eng[i]);

}

int planeta(char *p){
    char *planetas[8] = {"Mercurio", "Venus", "Terra", "Marte", "Jupiter", "Saturno", "Urano", "Neptuno"};
    int i,ver=0;

    for(i=0;i<12;i++)
        if(strcmp(p, planetas[i]) == 0)
            return 1;

    return 0;

}

// Recebe a matriz de sinonimos e a indicacao do numero de linhas (sabe-se que são 2 colunas)
// Escreve na consola todos os pares de sinonimos
void escreve_sin(char *sin[][2], int tot_lin){

    int i,j;
        for(i=0;i<tot_lin;i++){
            for(j=0;j<2;j++)
                printf("%s\t",sin[i][j]);
            puts("\n");
        }


}

// Recebe a matriz de sinonimos, a indicacao do numero de linhas e a palavra a pesquisar
// Devolve ponteiro para sinonimo da palavra recebida por parametro (NULL se não existir sinonimo)
char *pesquisa_sinonimo(char *sin[][2], int tot_lin, char *p){

    int i;

    for(i=0;i<tot_lin;i++){
       if (strcmp(sin[i][0], p) == 0) {
            return sin[i][1];
        }
        if (strcmp(sin[i][1], p) == 0) {
            return sin[i][0];
        }


    }printf("%s",p);
    return NULL;
}

// Recebe a matriz de sinonimos e a indicacao do numero de linhas
// Devolve ponteiro para a palavra alfabeticamente mais pequena que se encontra na matriz (NULL se não existirem palavras)
char* alfaMin(char *sin[][2], int tot_lin){

    int i,j;
    char *q;

        for(i=0;i<tot_lin;i++){
            if(strcmp(sin[i][0],sin[i][1])>0)
                q=sin[i][0];
            if(strcmp(sin[i][0],sin[i][1])<0)
                q=sin[i][1];
        }

    return q;
}

int main(){
    char palavra[50], *p, *q;

    char *s[5][2] = {{"estranho", "bizarro"},
                     {"desconfiar", "suspeitar"},
                     {"vermelho", "encarnado"},
                     {"duvidar", "desconfiar"},
                     {"carro", "automovel"}};

    // Exercicio 9

    char st[20], pl[20];;

    //printf("Mes: "); scanf("%s", st);
    //traduz(&st);


    //printf("Planeta: "); scanf("%s", pl);
    //printf("%d",planeta(pl));

    //return 0;
    // Exercicio 10 a partir daqui. Retirar a instrução da linha anterior para testar o código

    escreve_sin(s, 5);

    printf("Palavra a pesquisar: ");
    scanf(" %s", palavra);

    p = pesquisa_sinonimo(s, 5, palavra);

    if(p == NULL)
        printf("A palavra %s nao tem sinonimo conhecido\n", palavra);
    else
        printf("A palavra %s e sinonimo de %s\n", p, palavra);

    q = alfaMin(s, 5);

    if(p == NULL)
        printf("Nao existem palavras na tabela\n");
    else
        printf("A palavra alfabeticamente mais pequena e %s\n", q);

    // Escrever e testar as restantes funções


    return 0;
}
