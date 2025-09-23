#include <stdio.h>

#define IVAA 0.06
#define IVANA 0.23

void main(){

int numA=0, numNA=0, npro=0;
char tipo;
float precototal=0,precototaliva=0, preco=0;

do{
    printf("Introduza o valor do preco (0 para sair):");
    scanf("%f", &preco);

        if(preco>0){
            do{
                printf("Introduza o tipo de produto Alimentar-(A) ou Nao Alimentar-(N):");
                scanf(" %c", &tipo);
            }while(tipo!='A' && tipo!='a' && tipo!='n' && tipo!='N');

         precototal=precototal+preco;
            if(tipo == 'A'|| tipo == 'a'){
                precototaliva=precototaliva + (preco * (1 + IVAA));
                numA=numA+1;
                }else{
                    precototaliva=precototaliva + (preco * (1 + IVANA));
                    numNA=numNA+1;
                }
        }
}while(preco>0);

npro=numA+numNA;

printf("\nTotal produtos alimentares: %d\n", numA);
printf("Total produtos nao alimentares: %d\n", numNA);
printf("Total produtos: %d\n", npro);
printf("Preco total sem iva: %.2f\n", precototal);
printf("Preco total com iva: %.2f\n", precototaliva);

}
