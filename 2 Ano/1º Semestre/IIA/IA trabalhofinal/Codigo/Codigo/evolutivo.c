#include "evolutivo.h"
#include <string.h>
#include "funcao.h"
#include "trepa-colinas.h"

// Procura a melhor sol de uma determinada populacao
// Recebe: populacao, tam desta, melhor sol ate agora, parametros necessarios para calcula_fit2
int* get_best(int **pop, int tamPop, int* best, float matCustos[], float valorAtingir, int tam) {
    int i;
    for (i = 0; i < tamPop; i++) {
        if (calcula_fit2(best, matCustos, valorAtingir, tam) < calcula_fit2(pop[i], matCustos, valorAtingir, tam))
            best = pop[i];
    }
    return best;
}


// Preenche uma estrutura com os progenitores da próxima geração, de acordo com o resultados do torneio binário (tamanho de torneio: 2)
// Parâmetros de entrada: população actual (pop), populacao de pais a encher (parents), tamanho da população (popsize),
// tamanho do torneio (tsize), tam dos individuos e vetor de fitness
void tournament_geral(int **pop, int **parents, int popsize, int tsize, int tam, float *fitness) {
    int i, j, k, sair, best, *pos;

    pos = malloc(tsize * sizeof(int));
    // Realiza popsize torneios
    for (i = 0; i < popsize; i++) {
        // Seleciona tsize soluções diferentes para entrarem em torneio de seleção
        for (j = 0; j < tsize; j++) {
            do {
                pos[j] = random_l_h(0, popsize - 1);
                // Verifica se a nova posição escolhida é igual a alguma das outras posições escolhidas
                sair = 0;
                for (k = 0; k < j; k++) {
                    if (pos[k] == pos[j])
                        sair = 1;
                }
            } while (sair);
            // Guarda a posição da melhor solução de todas as que entraram em torneio
            if (j == 0 || fitness[pos[j]] < fitness[pos[best]]) //minimizar
                best = j;
        }
        memcpy(parents[i], pop[pos[best]], sizeof(int) * tam);
    }
    free(pos);
}




// Preenche uma estrutura com os progenitores da proxima geracao, de acordo com o resultados do torneio binario (tamanho de torneio: 2)
// Parametros de entrada: populacao actual (pop), populacao de pais a encher(parents), tam das duas populacoes
// e informacoes para a chamada a calcula_fit2
void tournament(int **pop, int **parents, int tamPop, float matCustos[], float valorAtingir, int tam) {
    int i, x1, x2;

    for (i = 0; i < tamPop; i++) {
        x1 = random_l_h(0, tamPop - 1);
        do
            x2 = random_l_h(0, tamPop - 1);
        while (x1 == x2);
        if (calcula_fit2(pop[x1], matCustos, valorAtingir, tam) < calcula_fit2(pop[x2], matCustos, valorAtingir, tam))
            memcpy(parents[i], pop[x1], sizeof(int) * tam);
        else
            memcpy(parents[i], pop[x2], sizeof(int) * tam);
    }
}

// Preenche o vector descendentes com o resultado das operacoes de recombinacao apenas de 1 ponto
// Parametros de entrada: ponteiro para os pais, ponteiro para os descendentes, tamanho das populacoes, tamanho dos individuos
// e probabilidade de recombinacao
void crossover(int **parents, int **offspring, int tamPop, int tam, float prob_recomb) {
    int i, j, point;
    int itera = tamPop - (tamPop % 2); //para garantir bom funcionamento em iteracoes impares
    for (i = 0; i < itera; i += 2) {
        if (rand_01() < prob_recomb) {
            point = random_l_h(1, tam - 1);
            for (j = 0; j < point; j++) {
                offspring[i][j] = parents[i][j];
                offspring[i + 1][j] = parents[i + 1][j];
            }
            for (j = point; j < tam - 1; j++) {
                offspring[i][j] = parents[i + 1][j];
                offspring[i + 1][j] = parents[i][j];
            }
        } else {
            for (j = 0; j < tam - 1; j++) {
                offspring[i][j] = parents[i][j];
                offspring[i + 1][j] = parents[i + 1][j];
            }
        }
    }
    if (tamPop % 2 != 0) { //cobrir o utimo elemento em caso de iteracoes impares
        for (j = 0; j < tam; j++) {
            offspring[tamPop - 1][j] = parents[tamPop - 1][j];
        }
    }
}

// Preenche o vector descendentes com o resultado das operacoes de recombinacao apenas de 2 ponto
// Parametros de entrada: ponteiro para os pais, ponteiro para os descendentes, tamanho das populacoes, tamanho dos individuos
// e probabilidade de recombinacao
void crossover2(int **parents, int **offspring, int tamPop, int tam, float prob_recomb) {
    int i, j, point, point2;

    int itera = tamPop - (tamPop % 2); //para garantir bom funcionamento em iteracoes impares
    for (i = 0; i < itera; i += 2) {
        if (rand_01() < prob_recomb) {
            point = random_l_h(1, tam - 1);
            do
                point2 = random_l_h(1, tam - 1);
            while (point == point2);
            //trocar os pontos caso o 2 seja mais pequenos
            if (point > point2) {
                int aux = point;
                point = point2;
                point2 = aux;
            }
            for (j = 0; j < point; j++) {
                offspring[i][j] = parents[i][j];
                offspring[i + 1][j] = parents[i + 1][j];
            }
            for (j = point; j < point2; j++) {
                offspring[i][j] = parents[i + 1][j];
                offspring[i + 1][j] = parents[i][j];
            }
            for (j = point2; j < tam - 1; j++) {
                offspring[i][j] = parents[i][j];
                offspring[i + 1][j] = parents[i + 1][j];
            }
        } else {
            for (j = 0; j < tam - 1; j++) {
                offspring[i][j] = parents[i][j];
                offspring[i + 1][j] = parents[i + 1][j];
            }
        }
    }
    if (tamPop % 2 != 0) { //cobrir o utimo elemento em caso de iteracoes impares
        for (j = 0; j < tam; j++) {
            offspring[tamPop - 1][j] = parents[tamPop - 1][j];
        }
    }
}

// Simple Mutation using create_neighbor
void mutation(int **offspring, int tamPop, int tam, float prob_mut) {
    int i, j;
    for (i = 0; i < tamPop; i++) {
        if (rand_01() < prob_mut) {
            int *newSol = malloc(sizeof(int) * tam);
            create_neighbor(offspring[i], newSol, tam);
            for (j = 0; j < tam; j++)
                offspring[i][j] = newSol[j];
            free(newSol);
        }
    }
}

// Advanced Mutation using create_neighbor2
void mutation2(int **offspring, int tamPop, int tam, float prob_mut) {
    int i, j;
    for (i = 0; i < tamPop; i++) {
        if (rand_01() < prob_mut) {
            int *newSol = malloc(sizeof(int) * tam);
            create_neighbor2(offspring[i], newSol, tam);
            for (j = 0; j < tam; j++)
                offspring[i][j] = newSol[j];
            free(newSol);
        }
    }
}

// Operadores geneticos a usar na geracao dos filhos
// Parametros de entrada: estrutura com os pais (parents), estrutura com parãmetros (d), estrutura que guardara os descendentes (offspring)
void genetic_operators(int **parents, int **offspring, int tamPop, int tam, float prob_recomb, float prob_mut) {
    // Recombinacao com um ponto de corte
    crossover(parents, offspring, tamPop, tam, prob_recomb);

    // Recombinacao com dois pontos de corte
    //crossover2(parents, offspring, tamPop, tam, prob_recomb);

    // Mutacao que tira ou coloca 1
    mutation(offspring, tamPop, tam, prob_mut);

    //Mutação tira ou coloca 2
    //mutation2(offspring, tamPop, tam, prob_mut);
}
