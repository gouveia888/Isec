#include <stdio.h>
#include <string.h>
#define TAM 30

void main(){

 char txt[TAM];
 int i;

 printf("Indique uma frase:");
 //scanf("%[^\n],txt");
 fgets(txt,TAM,stdin);

 for(i = strlen(txt)-1; i>=0; i++){

    printf("%c", txt[i]);
 }

}

