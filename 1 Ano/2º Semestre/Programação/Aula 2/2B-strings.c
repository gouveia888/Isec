// Programação 2023/24
// Aula Prática 2B - Strings

#include <stdio.h>
#include <string.h>

// Recebe string
// Mostra na consola a string escrita por ordem inversa
// A função não recebe o número de caracteres da strings, pelo que essa é a sua primeira tarefa
void printInv(char s[]){
    int i;

    for(i=0; s[i]!='\0'; i++)
        ;
    i--;

    // Possivel alternativa ao código que surge nas linhas anteriores: i = strlen(s)-1;

    while(i>=0)
        putchar(s[i--]);
}

// Recebe string
// Mostra na consola as várias palavras da string, uma em cada linha
void printPal(char s[]){
    int i;

    for(i=0; s[i]!='\0'; i++){
        if(s[i]!=' ')
            putchar(s[i]);
        else
            printf("\n");
    }

    printf("\n");
    return;
}

void ex8(char s1[],char s2[],char s3[]){
    int i;

    if(strcmp(s1,s2)== 0)
        strcpy(s3,"Conteudo igual");

    else if(strlen(s1)==strlen(s2))
        strcpy(s3,"Tamanho igual");

    else if (strcmp(s1,s2)<0){ //se a s1 for alfabeticamente menor é a s1
        strcpy(s3,s1);
        strcat(s3,s2);

    }else{
        strcpy(s3,s2);  //se a s1 for alfabeticamente menor é a s2
        strcat(s3,s1);
    }


}

int main(){
    char st1[15] = "Ola Mundo!";
    char st2[15] = "Ola Mundo2!";
    char st3[30]= "";


    printInv(st1);
    printf("\n");
    printPal(st1);

    ex8(st1,st2,st3);
    puts(st3);

    return 0;
}
