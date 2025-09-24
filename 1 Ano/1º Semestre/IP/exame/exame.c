#include <stdio.h>
#include <string.h>

#define TAM 100

int main(){

int i=0,j=0,k=0, pos;
char frase[TAM],palavra[TAM],finall[TAM];

printf("Escreva um frase: ");
fgets(frase,TAM,stdin);
printf("\nEscreva uma palavra: ");
scanf("%s",palavra);
printf("\nEscreva a posicao: ");
scanf("%d",&pos);

    for(i=0;frase[i]!='\0';i++,j++){
        if(i==pos){
            finall[j++]=' ';
            for(k=0;palavra[k]!='\0';k++,j++)
                finall[j]=palavra[k];
        }
        finall[j]=frase[i];
    }

    finall[j]='\0';
    strcpy(frase,finall);
    puts(frase);

return 0;
}
