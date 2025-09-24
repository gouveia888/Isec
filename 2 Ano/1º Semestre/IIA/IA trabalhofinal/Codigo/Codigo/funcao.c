#include "funcao.h"


// Leitura do ficheiro de input
// Retorna valor a atingir
// Trata dos dados iniciais do problema (num Moedas, valor a atingir, moedas disponiveis para uso)
float init_dados(char *nome, int *numMoedas, float *matCustos){
	FILE *f;
	f = fopen(nome, "r");
	if(!f)
	{
		printf("Erro no acesso ao ficheiro dos dados\n");
		exit(1);
	}
	// Num de moedas
	float valorAtingir;
	fscanf(f, "%d %f", numMoedas, &valorAtingir);
	// Valor a atingir

	//fscanf(f, "%f", &valorAtingir);
	// Preencher a matriz
	for (int i = 0; i < *numMoedas; i++)
		fscanf(f, "%f", &matCustos[i]);

	fclose(f);
	return valorAtingir;
}

// Gera uma solucao inicial
// Parametros: solucao, array custos, tamanho dos arrays e valor a atingir
// Faz uma solucao inicial com algumas moedas, nao precisa de ser solucao valida mas tenta aproximar se a uma
void gera_sol_inicial(int *sol, float matCustos[], int maxMoedas, float valorAtingir){
	int i, x;
	float custoTotal = 0.0;
	for(i = 0; i < maxMoedas; i++) //solucao comeca a zeros
		sol[i] = 0;
	while(custoTotal < valorAtingir){ //se menor adicionar cegamente uma moeda
		x = random_l_h(0, maxMoedas-1);
		int adiciona = random_l_h(1, maxMoedas-1);//gerar pelo menos uma moeda
		sol[x] += adiciona;
		custoTotal += adiciona * matCustos[x];
	}
	while(custoTotal > valorAtingir){ //se menor tirar cegamente uma moeda
		do
			x = random_l_h(0, maxMoedas-1);
		while (sol[x] == 0);
		sol[x] -= 1;
		custoTotal -= matCustos[x];
	}

	//Verifcar se neste ponto tudo foi retirado
	int tudoAZero = 1;
	for(i = 0; i < maxMoedas; i++) {
		if(sol[i] > 0) {
			tudoAZero = 0;
			break;
		}
	}
	//Se realmente tudo foi retirado entao por uma moeda num local aleatorio
	if(tudoAZero) {
		x = random_l_h(0, maxMoedas-1);
		sol[x] = 1;
	}
}

// Calcula a qualidade de uma solucao(numero de moedas usadas)
// Devolve: O custo associado a solucao
// Vai PENALIZAR solucoes que sejam invalidas. Esta penalizacao escala com a qualidade da solucao
int calcula_fit(int sol[], float matCustos[], float valorAtingir, int tam){
	int qualidade = 0;
	float custoTotal = 0.0;

	for(int i = 0; i < tam; i++){
		qualidade += sol[i];
		custoTotal += sol[i] * matCustos[i];
	}


	if(fabs(custoTotal - valorAtingir) > 0.0001) //assinalar uma ma solucao
		qualidade = INT_MAX;

	return qualidade;
}


// Calcula a qualidade de uma solucao(numero de moedas usadas)
// Devolve: O custo associado a solucao
// Vai REPARAR solucoes invalidas caso estas aparecam, devolvendo uma valida
int calcula_fit2(int sol[], float matCustos[], float valorAtingir, int tam){
	int qualidade = 0;
	float custoTotal = 0.0;

	for(int i = 0; i < tam; i++){
		custoTotal += sol[i] * matCustos[i];
	}
	while(fabs(custoTotal - valorAtingir) > 0.0001 ) {
		if (custoTotal < valorAtingir) { //se menor adicionar cegamente uma moeda
			int x = random_l_h(0, tam-1);
			sol[x] += 1;
			custoTotal += matCustos[x];
		}
		else if (custoTotal > valorAtingir) { //se menor tirar cegamente uma moeda
			int x = random_l_h(0, tam-1);
			if (sol[x] > 0) {
				sol[x] -= 1;
				custoTotal -= matCustos[x];
			}
		}
	}
	for(int i = 0; i < tam; i++)
		qualidade += sol[i];

	return qualidade;
}


// Gera a populacao inicial (array de solucoes)
// Parametros: populacao, tamanho da populacao, array de custos, tamanho dos arrays e valor a atingir
// Gera a pop inicial com recurso a chamada da func gera_sol_inicial
void gera_pop_inicial(int **pop, int tamPop, float matCustos[], int maxMoedas, float valorAtingir) {
	int i = 0;
	for (i ; i < tamPop; i++) {
		pop[i] = (int *)malloc(maxMoedas * sizeof(int));
		if (pop[i] == NULL) {
			printf("Erros na alocacao de memoria\n");
			exit(1);
		}
		gera_sol_inicial(pop[i], matCustos, maxMoedas, valorAtingir);
	}
}



void evaluate(int **pop, int tamPop, float matCustos[], float valorAtingir, int tam){
    int i;
    int qualidadePop = 0;
    for (i = 0; i < tamPop; i++) {
        //qualidadePop += calcula_fit(pop[i], matCustos, valorAtingir, tam); //penalizacao
        qualidadePop += calcula_fit2(pop[i], matCustos, valorAtingir, tam); //reparacao
    }
    qualidadePop /= tamPop;
    //printf("pop com uma qualidade media: %d", qualidadePop);
}