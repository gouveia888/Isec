#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include "utils.h"

//imprime a matriz de custos para o ecrâ
void mostraMatriz(float matCustos[], int numMoedas){
	for (int i = 0; i < numMoedas; i++)
		printf("%.3f\t", matCustos[i]);

	printf("\n");
}

// Escreve uma solucao
// Parametros: array solucao e tamanho do mesmo
void escreve_sol(int *sol, int maxMoedas){
	for (int i = 0; i < maxMoedas; i++)
	{
		printf("%d\t", sol[i]);
	}
	printf("\n");
}

// Inicializa o gerador de numeros aleatorios
void init_rand()
{
	srand((unsigned)time(NULL));
}

// Devolve valor inteiro aleatorio entre min e max
int random_l_h(int min, int max)
{
	return min + rand() % (max-min+1);
}

// Devolve um valor real aleatorio do intervalo [0, 1]
float rand_01()
{
	return ((float)rand())/RAND_MAX;
}

void substituir_sol(int *a, int *b, int maxMoedas) {
	int i;
	for (i = 0; i < maxMoedas; i++)
			a[i] = b[i];
}

void mostraPop(int **pop, int tamPop, int maxMoedas) {
	int i;
	printf("\nPopulacao:\n");
	for (i = 0; i < tamPop; i++) {
		printf("\tIndividuo %d:\t", i);
		escreve_sol(pop[i], maxMoedas);
	}
}