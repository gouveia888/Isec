#include <string.h>
#include <stdlib.h>
#include <stdio.h>
#include <limits.h>
#include "trepa-colinas.h"
#include "funcao.h"
#include <evolutivo.h>

#define DEFAULT_RUNS 30

int main(int argc, char *argv[]) {
    char nome_fich[100];
    int k, runs, custo, best_custo = INT_MAX;
    float mediaCusto = 0.0;
    int valid_runs = 0; // Contador para soluções válidas

    // Entrada de dados do utilizador
    if (argc == 3) {
        runs = atoi(argv[2]);
        strcpy(nome_fich, argv[1]);
    } else if (argc == 2) {
        runs = DEFAULT_RUNS;
        strcpy(nome_fich, argv[1]);
    } else {
        runs = DEFAULT_RUNS;
        printf("Por favor, insira o nome do ficheiro de dados: ");
        gets(nome_fich);
    }
    if (runs <= 0)
        return 0;

    init_rand();
    int numeroMoedas;
    float valorAtingir;
    float *matrizCustos = (float *)malloc(numeroMoedas * sizeof(float));
    valorAtingir = init_dados(nome_fich, &numeroMoedas, matrizCustos);
    // Confirmação inicial dos valores
    printf("\nTipos de Moedas: %d\nValor a atingir: %.5f\n", numeroMoedas, valorAtingir);
    mostraMatriz(matrizCustos, numeroMoedas);

    char metodo[20];


    while(strcmp(metodo, "TrepaColinas") != 0 && strcmp(metodo, "Evolutivo") != 0 && strcmp(metodo, "Hibrido") != 0){
        // Escolha do algoritmo
        printf("\nEscolha o metodo a utilizar (TrepaColinas ou Evolutivo ou Hibrido): ");
        scanf("%s", metodo);

        // Algoritmo Trepa-Colinas
        if (strcmp(metodo, "TrepaColinas") == 0) {
            printf("\nO algoritmo selecionado e o Trepa-Colinas\n");
            int *sol = malloc(sizeof(int) * numeroMoedas); // Aloca a matriz de soluções
            int *best = malloc(sizeof(int) * numeroMoedas); // Aloca a matriz da melhor solução
            if (sol == NULL || best == NULL) {
                printf("Erro na alocacao de memoria\n");
                exit(1);
            }
            for (k = 0; k < runs; k++) {
                // Gerar solução inicial
                gera_sol_inicial(sol, matrizCustos, numeroMoedas, valorAtingir);
                printf("\n\nSolucao inicial da iteracao %d:\n", k + 1);
                escreve_sol(sol, numeroMoedas);
                int fit = calcula_fit2(sol, matrizCustos, valorAtingir, numeroMoedas);
                printf("Qualidade da solucao inicial: %d\n", fit);

                // Algoritmo Trepa-Colinas
                int num_vizinhos = 100;
                custo = trepa_colinas(sol, matrizCustos, numeroMoedas, valorAtingir, num_vizinhos);

                if (custo != INT_MAX) { // Se a solução não tiver custo de INT_MAX
                    mediaCusto += custo;
                    valid_runs++;
                    if (custo < best_custo) { // Minimizar
                        best_custo = custo;
                        substituir_sol(best, sol, numeroMoedas);
                    }
                }
            }
            // Escrever resultados globais
            if (valid_runs > 0) {
                printf("\n\nCusto Medio das solucoes validas: %.5f\n", mediaCusto / valid_runs);
            } else {
                printf("\n\nNenhuma solucao válida foi encontrada.\n");
            }
            printf("\nMelhor solucao encontrada:\n");
            escreve_sol(best, numeroMoedas);
            printf("Melhor custo encontrado: %2d moedas\n", best_custo);
            free(sol);
            free(best);
        }

            // Algoritmo Evolutivo
        else if (strcmp(metodo, "Evolutivo") == 0) {
            //PARAMETROS
            int tamPop = 5; //tamanho da populacao
            int numGeracoes = 50;
            float prob_recomb = 0.7;
            float prob_mut = 0.0;


            float mediaCusto = 0.0;
            int custo_best_run;
            int custo_best_ever = 10000;
            int **pop = malloc( sizeof(int *) * tamPop);
            int **parents;
            int *best_run = malloc(sizeof(int)*numeroMoedas); //aloca a matriz da melhor solucao
            int *best_ever = malloc(sizeof(int)*numeroMoedas);
            memset(best_ever, INT_MAX, sizeof(int)*numeroMoedas); //Preencher best_ever ccom valores muito altos
            int gen_atual;
            if (pop == NULL || best_run == NULL) {
                printf("Erro na alocacao de memoria");
                exit(1);
            }

            for (k = 0; k < runs; k++) {
                //printf("\n\nRepeticao %d do algoritmo evolutivo:",k);
                // Geracao da populacao inicial
                gera_pop_inicial(pop, tamPop, matrizCustos, numeroMoedas, valorAtingir);
                //mostraPop(pop, tamPop, numeroMoedas);
                evaluate(pop, tamPop, matrizCustos, valorAtingir, numeroMoedas);


                best_run = pop[0]; //assumir uma qualquer
                best_run = get_best(pop, tamPop, best_run, matrizCustos, valorAtingir, numeroMoedas); // Encontra-se a melhor solucao dentro de toda a populacao
                // Reserva espaco para os pais da populacao seguinte
                parents = malloc(sizeof(int *) * tamPop);
                if (parents == NULL)
                {
                    printf("Erro na alocacao de memoria\n");
                    exit(1);
                }
                for (int i = 0; i < tamPop; i++) {
                    parents[i] = malloc(sizeof(int) * numeroMoedas);
                    if (parents[i] == NULL) {
                        printf("Memory allocation failed for parents[%d]\n", i);
                        exit(EXIT_FAILURE);
                    }
                }
                // Ciclo da criacao de novas geracoes
                gen_atual = 1;
                while (gen_atual <= numGeracoes)
                {
                    //printf("\nGeracao: %d\n", gen_atual);

                    // Torneio binario para encontrar os progenitores (ficam armazenados no vector parents)
                    //tournament(pop, parents, tamPop, matrizCustos, valorAtingir, numeroMoedas);

                    //troneio geral
                    float *fitness = malloc(tamPop * sizeof(float));
                    for (int i = 0; i < tamPop; i++) {
                        fitness[i] = calcula_fit2(pop[i], matrizCustos, valorAtingir, numeroMoedas);
                    }
                    tournament_geral(pop, parents, tamPop, 2, numeroMoedas, fitness);
                    free(fitness);


                    // Aplica os operadores geneticos aos pais (os descendentes ficam armazenados na estrutura pop)
                    genetic_operators(parents, pop, tamPop, numeroMoedas, prob_recomb, prob_mut);

                    // Avalia a nova populacao (a dos filhos)
                    evaluate(pop, tamPop, matrizCustos, valorAtingir, numeroMoedas);
                    best_run = get_best(pop, tamPop, best_run, matrizCustos, valorAtingir, numeroMoedas);
                    gen_atual++;
                }
                printf("\nMelhor individuo desta iteracao:\t");
                escreve_sol(best_run, numeroMoedas);
                custo_best_run = calcula_fit2(best_run, matrizCustos, valorAtingir, numeroMoedas);
                printf("Com um custo %d: ", custo_best_run);
                mediaCusto += custo_best_run;
                if (custo_best_run < custo_best_ever) {
                    best_ever = best_run;
                    custo_best_ever = custo_best_run;
                }

            }
            // Escreve eresultados globais
            printf("\n\nCusto Medio: %.5f", mediaCusto/k);
            printf("\nMelhor solucao encontrada:\t");
            escreve_sol(best_ever, numeroMoedas);
            printf("Melhor custo encontrado: %2d moedas\n", custo_best_ever);

            free(pop);
            free(parents);
            free(best_run);
            free(best_ever);
        }

        else if (strcmp(metodo, "Hibrido") == 0) {
            //PARAMETROS
            int tamPop = 5; //tamanho da populacao
            int numGeracoes = 50;
            float prob_recomb = 0.7;
            float prob_mut = 0.0;

            float mediaCusto = 0.0;
            int custo_best_run;
            int custo_best_ever = 10000;
            int **pop = malloc(sizeof(int *) * tamPop);
            int **parents;
            int *best_run = malloc(sizeof(int) * numeroMoedas); //aloca a matriz da melhor solucao
            int *best_ever = malloc(sizeof(int) * numeroMoedas);
            memset(best_ever, INT_MAX, sizeof(int) * numeroMoedas); //Preencher best_ever com valores muito altos
            int gen_atual;
            if (pop == NULL || best_run == NULL) {
                printf("Erro na alocacao de memoria");
                exit(1);
            }

            for (k = 0; k < runs; k++) {
                // Geracao da populacao inicial
                gera_pop_inicial(pop, tamPop, matrizCustos, numeroMoedas, valorAtingir);
                evaluate(pop, tamPop, matrizCustos, valorAtingir, numeroMoedas);

                /*//Hibrido Alterado
                // Aplicar Trepa-Colinas na população inicial
                for (int i = 0; i < tamPop; i++) {
                    int num_vizinhos = 1000;
                    custo_best_run = trepa_colinas(pop[i], matrizCustos, numeroMoedas, valorAtingir, num_vizinhos);
                    if (custo_best_run < custo_best_ever) {
                        best_ever = pop[i]; // Atualiza a melhor solução
                        custo_best_ever = custo_best_run;
                    }
                }
                */

                best_run = pop[0]; //assumir uma qualquer
                best_run = get_best(pop, tamPop, best_run, matrizCustos, valorAtingir, numeroMoedas); // Encontra-se a melhor solucao dentro de toda a populacao

                // Reserva espaco para os pais da populacao seguinte
                parents = malloc(sizeof(int *) * tamPop);
                if (parents == NULL) {
                    printf("Erro na alocacao de memoria\n");
                    exit(1);
                }
                for (int i = 0; i < tamPop; i++) {
                    parents[i] = malloc(sizeof(int) * numeroMoedas);
                    if (parents[i] == NULL) {
                        printf("Memory allocation failed for parents[%d]\n", i);
                        exit(EXIT_FAILURE);
                    }
                }

                // Ciclo da criacao de novas geracoes
                gen_atual = 1;
                while (gen_atual <= numGeracoes) {
                    // Torneio geral
                    float *fitness = malloc(tamPop * sizeof(float));
                    for (int i = 0; i < tamPop; i++) {
                        fitness[i] = calcula_fit2(pop[i], matrizCustos, valorAtingir, numeroMoedas);
                    }
                    tournament_geral(pop, parents, tamPop, 2, numeroMoedas, fitness);
                    free(fitness);

                    // Aplica os operadores geneticos aos pais (os descendentes ficam armazenados na estrutura pop)
                    genetic_operators(parents, pop, tamPop, numeroMoedas, prob_recomb, prob_mut);

                    // Avalia a nova populacao (a dos filhos)
                    evaluate(pop, tamPop, matrizCustos, valorAtingir, numeroMoedas);
                    best_run = get_best(pop, tamPop, best_run, matrizCustos, valorAtingir, numeroMoedas);
                    gen_atual++;
                }

                // Aplicar Trepa-Colinas na melhor solução encontrada
                int num_vizinhos = 1000; // ou outro valor que você preferir
                custo_best_run = trepa_colinas(best_run, matrizCustos, numeroMoedas, valorAtingir, num_vizinhos);

                // Atualiza a média de custo
                mediaCusto += custo_best_run; // Adiciona o custo da melhor solução encontrada

                // Verifica se a melhor solução encontrada é a melhor de todas as execuções
                if (custo_best_run < custo_best_ever) {
                    best_ever = best_run; // Atualiza a melhor solução
                    custo_best_ever = custo_best_run;
                }
            }

            // Escreve resultados globais
            printf("\n\nCusto Medio: %.5f", mediaCusto / runs); // Custo médio considerando todas as execuções
            printf("\nMelhor solucao encontrada:\t");
            escreve_sol(best_ever, numeroMoedas);
            printf("Melhor custo encontrado: %2d moedas\n", custo_best_ever);

            // Libera memória
            for (int i = 0; i < tamPop; i++) {
                free(parents[i]);
            }
            free(parents);
            free(best_run);
            free(best_ever);
            free(pop);
        }

        else {
            printf("Metodo nao reconhecido. Por favor, escolha entre 'TrepaColinas' ou 'Evolutivo' ou 'Hibrido'.\n");
        }
    }


    return 0;
}
