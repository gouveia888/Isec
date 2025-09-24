int* get_best(int **pop, int tamPop , int* best, float matCustos[], float valorAtingir, int tam);
void tournament(int **pop, int **parents, int tamPop, float matCustos[], float valorAtingir, int tam);
void tournament_geral(int **pop, int **parents, int popsize, int tsize, int tam, float *fitness);
void crossover(int **parents, int **offspring, int tamPop, int tam, float prob_recomb);
void crossover2(int **parents, int **offspring, int tamPop, int tam, float prob_recomb);
void mutation(int **offspring, int tamPop, int tam, float prob_mut);
void mutation2(int **offspring, int tamPop, int tam, float prob_mut);
void genetic_operators(int **parents,int **offspring, int tamPop, int tam, float prob_recomb, float prob_mut);