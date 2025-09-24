#include <stdio.h>
#include <stdlib.h>
#include "trepa-colinas.h"
#include "funcao.h"

void create_neighbor(int *current_solution, int *neighbor_solution, int num_coins) {
    for (int i = 0; i < num_coins; i++) {
        neighbor_solution[i] = current_solution[i];
    }

    // Escolhe aleatoriamente adicionar ou remover uma moeda
    int action = random_l_h(0, 1); // 0 para adicionar, 1 para remover
    int coin_index = random_l_h(0, num_coins - 1);

    if (action == 0) {
        neighbor_solution[coin_index]++; // Adiciona uma moeda
    } else if (neighbor_solution[coin_index] > 0) {
        neighbor_solution[coin_index]--; // Remove uma moeda
    }
}

void create_neighbor2(int *current_solution, int *neighbor_solution, int num_coins) {
    for (int i = 0; i < num_coins; i++) {
        neighbor_solution[i] = current_solution[i];
    }

    // Escolhe aleatoriamente adicionar ou remover duas moeda
    for (int i = 0; i < 2; i++) {
        int action = random_l_h(0, 1); // 0 para adicionar, 1 para remover
        int coin_index = random_l_h(0, num_coins - 1);
        if (action == 0) {
            neighbor_solution[coin_index]++; // Adiciona uma moeda
        } else if (neighbor_solution[coin_index] > 0) {
            neighbor_solution[coin_index]--; // Remove uma moeda
        }
    }
}

// Trepa colinas
int trepa_colinas(int sol[], float matCustos[], int tam, float valorAtingir, int num_iter) {
    int *new_sol = malloc(sizeof(int) * tam);
    if (new_sol == NULL) {
        printf("Erro na alocacao de memoria");
        exit(1);
    }

    int custo = calcula_fit(sol, matCustos, valorAtingir, tam);
    for (int i = 0; i < num_iter; i++) {
        create_neighbor(sol, new_sol, tam); // vizinhanca 1
        //create_neighbor2(sol, new_sol, tam); // vizinhanca 2

        //penalizar
        //int custo_viz = calcula_fit(new_sol, matCustos, valorAtingir, tam); // penalizacao

         //recompensar
         int custo_viz = calcula_fit2(new_sol, matCustos, valorAtingir, tam); // reparacao

        if (custo_viz <= custo) { // (<= ou <) aceitar ou nao vizinhos iguais
            custo = custo_viz;
            substituir_sol(sol, new_sol, tam);
        }
    }
    printf("\nMelhor sol desta iter do trepa-colinas:\t");
    escreve_sol(sol, tam);
    printf("De custo: %d", custo);
    free(new_sol);
    return custo;
}
