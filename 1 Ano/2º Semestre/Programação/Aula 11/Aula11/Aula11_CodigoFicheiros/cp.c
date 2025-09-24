#include <stdio.h>
#include <string.h>

struct paragem{
    char nome[50];
    int minutos;
};

int Soma_Media (char *nome,float *soma, float *media){
    int cont=0,num;
    FILE *f;

    *soma=0;
    *media=0;
    f = fopen(nome,"rb");

    if(f==NULL){
        printf("Erro ao abrir o ficheiro");
        return -1;
    }

    while(fread(&num,sizeof(int),1,f)==1){
        cont++;
        *soma=*soma+num;
    }

    *media=*soma/cont;

    fclose(f);

    return cont;
}

int Cresc (char *nome){
    int cont=1,num, num_a, sup;
    FILE *f;

    f = fopen(nome,"rb");

    if(f==NULL){
        printf("Erro ao abrir o ficheiro");
        return -1;
    }

    fread(&num_a,sizeof(int),1,f);

    while(fread(&num,sizeof(int),1,f)==1){
        cont++;
        if (num_a < num){
            num_a = num;
            sup++;
        }else{
            fclose(f);
            return 0;
        }
    }

    fclose(f);
    return 1;
}

// Recebe nome do ficheiro binário e nomes de 2 paragens
// Verifica se é possivel efetuar a ligação entre as 2 paragens
// Devolve número de minutos que demora a ligar as 2 paragens ou -1, caso seja impossivel
int liga(char *nome, char *or, char *dest){
    FILE *f;
    int soma=0;
    struct paragem p;

    f = fopen(nome, "rb");
    if(f == NULL) {
        printf("Erro no acesso ao ficheiro\n");
        return;
    }

    while(fread(&p, sizeof(struct paragem), 1, f) == 1)
        soma+= p.minutos;

    fclose(f);
        return soma;
}

// Recebe nome do ficheiro binário e nome de paragem
// Verifica se paragem faz parte do percurso ou não. Devolve 1(Sim) ou 0 (Não)
int existeParagem(char *nomeF, char *paragem){
    FILE *f;
    struct paragem p;

    f = fopen(nomeF, "rb");
    if(f == NULL) {
        printf("Erro no acesso ao ficheiro\n");
        return;
    }

    while(fread(&p, sizeof(struct paragem), 1, f) == 1)
        if(strcmp(paragem,p.nome)==0){
            fclose(f);
            return 1;
        }else
            return 0;
}

// Recebe nome do ficheiro binário
// Escreve na consola o nome da origem e do destino do percurso
void orDest(char *nomeF){
    FILE *f;
    struct paragem org, dest;

    f = fopen(nomeF, "rb");
    if(f == NULL) {
        printf("Erro no acesso ao ficheiro\n");
        return;
    }

    fread(&org, sizeof(struct paragem), 1, f);
    printf("Estacao de origem e %s\n", org);


    while(fread(&dest, sizeof(struct paragem), 1, f) == 1);
        printf("A ultima estacao e %s\n", dest.nome);

    fclose(f);
}

// Recebe nome do ficheiro binário
// Escreve na consola as varias paragens do percurso armazenado
void mostraPercurso(char *nomeF) {
    FILE *f;
    struct paragem p;

    f = fopen(nomeF, "rb");
    if(f == NULL) {
        printf("Erro no acesso ao ficheiro\n");
        return;
    }

    while(fread(&p, sizeof(struct paragem), 1, f) == 1)
        printf("%s: %d\n", p.nome, p.minutos);

    fclose(f);
}

int main() {

    int num;
    float *soma, *media;


    //num= Soma_Media("valoresEx2.bin", &soma, &media);
    //printf("Numero %d, soma=%d, media=%d,", num, soma, media);

    mostraPercurso("cp_ex3.dat");

    orDest("cp_ex3.dat");

    printf("\nParagem: %d\n", existeParagem("cp_ex3.dat", "Porto"));

    printf("\nLigacao: %d\n", liga("cp_ex3.dat", "Coimbra", "Porto"));


    return 0;
}
