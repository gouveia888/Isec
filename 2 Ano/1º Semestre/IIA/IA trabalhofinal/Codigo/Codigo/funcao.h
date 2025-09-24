#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <stdbool.h>
#include "utils.h"

float init_dados(char *nome, int *numMoedas, float *matCustos);

void gera_sol_inicial(int *sol, float matCustos[], int maxMoedas, float valorAtingir);

int calcula_fit(int sol[], float matCustos[], float valorAtingir, int tam);

int calcula_fit2(int sol[], float matCustos[], float valorAtingir, int tam);

void gera_pop_inicial(int **pop, int tamPop, float matCustos[], int maxMoedas, float valorAtingir);
void evaluate(int **pop, int tamPop, float matCustos[], float valorAtingir, int tam);