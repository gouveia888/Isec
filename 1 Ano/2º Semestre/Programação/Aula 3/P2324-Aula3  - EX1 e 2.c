// Programação 2023/24
// Aula Prática 3 - Ponteiros e Endereços: Comunicação entre funções e manipulação de tabelas

#include <stdio.h>

void f1(){
 int a=12, *p;
 float x=3.5, *q;
 p = &a;
 q = &x;
 printf("\n\n\n\nValores: a=%d\tx=%.2f\n", a, x);
 printf("Valores: a=%d\tx=%.2f\n", *p, *q);
 printf("Endereco/Localizacao: a -> %p\tx -> %p\n", &a, &x);
 printf("Endereco/Localizacao: a -> %p\tx -> %p\n", p, q);
}

void f2(int x, int *p){
 printf("\n\nF2: Endereco/Localizacao: x=%p\tb=%p\n", &x, p);
 x++;
 (*p)++;
 printf("Valores em F2: a=%d\tb=%d\n", x, *p);
}
int main(){

 int a=1, b=2;
 printf("Valores Iniciais: a=%d\tb=%d\n", a, b);
 printf("Main: Endereco/Localizacao: a=%p\tb=%p\n", &a, &b);
 f2(a, &b);
 printf("Valores Finais: a=%d\tb=%d\n", a, b);
}
