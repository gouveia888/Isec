#include "utils.h"
// Criar a varivel de ambiente <export MSG_FICH="dados.txt">
// Mostrar variavel de estado <echo $MSG_FICH>
// Apagar <unset MSG_FICH>

typedef struct
{
    char nome[MAX_NAME_TOPICS], usersname[MAX_USERS][50];
    int bloqueio, count, pid[MAX_USERS];
    Mensagem msgs[MAX_MSG_PRESISTENTES];
} TOPICOS;


typedef struct
{   
    char (*users)[50], (*topics)[MAX_NAME_TOPICS];
    int *user_atual, *topics_atual, *pid, exit; 
    pthread_mutex_t *ptrinco;             
    TOPICOS *pdados;
} CDATA;

int Verifica_login(char users[MAX_USERS][50], char *new_user, int new_pid, int *users_atual, int *pid);
int Logout(char old_user, int pid);
void Mostra_Topicos(char topicos[MAX_TOPICS][MAX_NAME_TOPICS], int *topicos_atual);
int subscribe(CDATA *cdata, const char *topico, const char *nome, int pid);
int unsubscribe(CDATA *cdata, const char *topico, const char *nome);
void Mostra_Users(char users[MAX_USERS][50], int const *users_atual);
int Remove_User(char users[MAX_USERS][50], char *old_user, int *users_atual, int *pid);
void listar_subscricoes(CDATA *cdata);
void Lock_Topico(CDATA *psrv, char *arg2);
void Unlock_Topico(CDATA *psrv, char *arg2);
void show_topic(CDATA *cdata, const char *topico);
int adiciona_messagem(CDATA *cdata, Mensagem T1, int pid);
int Encontra_pos(int pid[MAX_USERS], int *users_atual, int user_pid);
void LogoutAll(CDATA *dados);
int verifica_struct_srv(const char *cmd);
void envia_msg_p(CDATA *pcli, char *topico, int pid );
void envia_msg(CDATA *pcli, Mensagem new_msg, int pid);
void *Servidor(void *pdata);
void *Cliente(void *pdata);
void *Persistentes(void *pdata);
Mensagem separa_buffer(char buffer[]);

int main(int argc, char *[])
{

    int canal[2], n, fd, fd_cli, tipo, fd_pid;
    char cmd[40], str[10], buffer[MAX_MSG_SIZE], fd_serv[20], fifo_cli[20];
    char users[MAX_USERS][50] = {0}, topicos[MAX_TOPICS][MAX_NAME_TOPICS] = {};
    int i = 0, j = 0, users_atual = 0, topicos_atual = 0, pid[MAX_USERS] = {0};
    Mensagem msg_arq;
    int sucesso;
    pthread_t tid[10]; // substituir 10 pelo numero maximo de threads
    CDATA cli;         // dados a enviar para as threads
    TOPICOS dados[MAX_TOPICS];
    pthread_mutex_t trinco;
    pthread_mutex_init(&trinco, NULL);

    for (int i = 0; i < MAX_TOPICS; i++) {
        memset(&dados[i], 0, sizeof(TOPICOS));
    }

    cli.topics = topicos;
    cli.user_atual = &users_atual;
    cli.users = users;
    cli.topics_atual = &topicos_atual;
    cli.pid = pid;
    cli.pdados = dados;
    cli.exit = 1;  
    cli.ptrinco = &trinco;

    if (access(FIFO_SRV, F_OK) == 0)
    {
        printf("[ERRO] Ja existe um servidor!\n");
        exit(3);
    }

    FILE *f = fopen(getenv("MSG_FICH"), "r");

    if (f == NULL)
    {
        printf("Problemas na CRIACAO do arquivo\n");
        return -1;
    }

     while (fgets(buffer, sizeof(buffer), f) != NULL) {
    msg_arq = separa_buffer(buffer);

    int existe_u = 0, indice_topico = -1;

    // Verifica se o usuário já existe
    for (int i = 0; i < users_atual; i++) {
        if (strcmp(cli.users[i], msg_arq.username) == 0) {
            existe_u = 1;
            break;
        }
    }

    // Verifica se o tópico já existe
    for (int i = 0; i < topicos_atual; i++) {
        if (strcmp(cli.topics[i], msg_arq.topico) == 0) {
            indice_topico = i;
            break;
        }
    }

    // Adiciona novo usuário, se necessário
    if (existe_u == 0 && users_atual < MAX_USERS) {
        strcpy(cli.users[users_atual], msg_arq.username);
        pid[users_atual] = -1;
        users_atual++;
    }

    // Se o tópico não existe, cria um novo
    if (indice_topico == -1 && topicos_atual < MAX_TOPICS) {
        indice_topico = topicos_atual;
        strcpy(cli.topics[topicos_atual], msg_arq.topico);
        strcpy(cli.pdados[topicos_atual].nome, msg_arq.topico);
        cli.pdados[topicos_atual].count = 0;
        topicos_atual++;
    }

    // Adiciona a mensagem ao tópico existente
    if (indice_topico != -1 && cli.pdados[indice_topico].count < MAX_MSG_PRESISTENTES) {
        int msg_count = cli.pdados[indice_topico].count;
        strcpy(cli.pdados[indice_topico].msgs[msg_count].conteudo, msg_arq.conteudo);
        strcpy(cli.pdados[indice_topico].msgs[msg_count].username, msg_arq.username);
        cli.pdados[indice_topico].msgs[msg_count].duracao = msg_arq.duracao;
        cli.pdados[indice_topico].pid[msg_count] = -1;
        strcpy(cli.pdados[indice_topico].usersname[msg_count], msg_arq.username);
        cli.pdados[indice_topico].count++;
    }
}

    mkfifo(FIFO_SRV, 0600);
    fd = open(FIFO_SRV, O_RDWR);

    pthread_create(&tid[0], NULL, Servidor, (void *)&cli);    //trata do stdin
    pthread_create(&tid[2], NULL, Persistentes, (void *)&cli);
    pthread_create(&tid[1], NULL, Cliente, (void *)&cli); //mensagens recebidas no FIFO_SRV

    pthread_join(tid[0], NULL);
    pthread_mutex_lock(cli.ptrinco);
        cli.exit = 0;
    pthread_mutex_unlock(cli.ptrinco);
    pthread_join(tid[1], NULL);
    pthread_join(tid[2], NULL);

    f = fopen(getenv("MSG_FICH"), "w");

    if (f == NULL) {
        perror("Erro ao abrir o arquivo para escrita");
        return -1;
    }

    for (int i = 0; i < topicos_atual; i++){
       for (int j = 0; j <= cli.pdados[i].count; j++){
            if(strlen(cli.pdados[i].msgs[j].conteudo)>0){
            fprintf(f,"%s %s %d %s\n", cli.pdados[i].nome, cli.pdados[i].msgs[j].username, cli.pdados[i].msgs[j].duracao, cli.pdados[i].msgs[j].conteudo);
            }
        }   
    }
 
    pthread_mutex_destroy(&trinco);
    close(fd);
    close(canal[0]);
    unlink(FIFO_SRV);
    exit(0);
}

void *Cliente(void *cli)
{
    int fd, fd_cli, n, tipo;
    char fifo_cli[20];
    CDATA *pcli = (CDATA *)cli;

    mkfifo(FIFO_SRV, 0600);
    fd = open(FIFO_SRV, O_RDWR);

    if (fd < 0)
    {
        perror("Erro ao abrir FIFO do servidor");
        pthread_exit(NULL);
    }

    pthread_mutex_lock(pcli->ptrinco);
        int exit = pcli->exit;
    pthread_mutex_unlock(pcli->ptrinco);

    while(exit){
        int aux = 0;

        n = read(fd, &tipo, sizeof(int));
        //printf("\nTipo: %d -- %d --- %d\n", tipo, n, sizeof(int));
        if(tipo == -1){
           break;
        }
        if (n <= 0)
            break;
        n = read(fd, &aux, sizeof(int));
        //printf("PID: %d -- %d --- %d\n", aux, n, sizeof(int));
        if (n <= 0)
            break;
        sprintf(fifo_cli, FIFO_CLI, aux);
        fd_cli = open(fifo_cli, O_WRONLY);
        if (n == sizeof(int))
        {

            switch (tipo)
            {

            case 0: // cmd = topics
                write(fd_cli, &tipo, sizeof(int));
                Mostra_Topicos(pcli->topics, pcli->topics_atual);
                write(fd_cli, pcli->topics, sizeof(char) * MAX_TOPICS * MAX_NAME_TOPICS);
                close(fd_cli);
                break;

            case 1: // cmd = msg
                Mensagem T1;
                int result;
                n = read(fd, &T1, sizeof(T1));
                if (n == sizeof(T1))
                {
                    result = subscribe(pcli, T1.topico, pcli->users[Encontra_pos(pcli->pid, pcli->user_atual, aux)], aux);
                    printf("Resultado: %d\n", result);
                    if(result == 0)
                        envia_msg_p(pcli, T1.topico, aux);
                    if(T1.duracao > 0)
                        adiciona_messagem(pcli, T1, aux);
                    envia_msg(pcli, T1, aux);
                }
                close(fd_cli);
                break;

            case 2: // cmd = subscribe
                Topics T2; 
                n = read(fd, &T2, sizeof(T2));
                if (n == sizeof(T2)){
                    result = subscribe(pcli, T2.topico, pcli->users[Encontra_pos(pcli->pid, pcli->user_atual, aux)], aux); // aux é o pid do user atua
                    envia_msg_p(pcli, T2.topico, aux);
                    listar_subscricoes(pcli);
                }
                close(fd_cli);
                break;

            case 3: // cmd = unsubcribe
                Topics T3;
                n = read(fd, &T3, sizeof(T3));
                if (n == sizeof(T3))
                {
                    int result = unsubscribe(pcli, T2.topico, pcli->users[Encontra_pos(pcli->pid, pcli->user_atual, aux)]); // aux é o pid do user atual
                    listar_subscricoes(pcli);
                }
                close(fd_cli);
                break;

            case 4: // cmd = exit
                Login T4;
                exit_env sair;
                n = read(fd, &T4, sizeof(T4));
                if (n == sizeof(T4)){
                    strcpy(sair.u.username, "exit");
                    sair.tipo = 4;
                    write(fd_cli, &sair, sizeof(sair));
                    printf("%s\n", T4.username);
                    Remove_User(pcli->users, T4.username, pcli->user_atual, pcli->pid);
                    close(fd_cli);
                }
                break;
            case 5: // cmd = login
                Login T5;
                exit_env sair_forcado;
                n = read(fd, &T5, sizeof(Login));
                if (n == sizeof(Login))
                {
                    int sucesso = Verifica_login(pcli->users, T5.username, aux, pcli->user_atual, pcli->pid);
                    if (sucesso == 0)
                    {
                        sair_forcado.tipo = 4;
                        strcpy(sair_forcado.u.username, "exit");
                        write(fd_cli, &sair_forcado, sizeof(sair_forcado));
                    }
                }
                close(fd_cli);
                break;
            }
            close(fd_cli);
        }
    }

    printf("Thread do cliente encerrada\n");
    pthread_exit(NULL);
}

void *Servidor(void *srv){

    char buffer[150], cmd[6], arg2[50];
    CDATA *psrv = (CDATA *)srv;

    do{
        printf("CMD>");
        fgets(buffer, 150, stdin);
        sscanf(buffer, "%s %s", cmd, arg2);

        switch (verifica_struct_srv(cmd))
        {
        case 0: // users
            Mostra_Users(psrv->users, psrv->user_atual);
            break;
        case 1: // REMOVE
            int n = Remove_User(psrv->users, arg2, psrv->user_atual, psrv->pid);
            Logout(arg2[50], n);
            break;
        case 2: // topics
            Mostra_Topicos(psrv->topics, psrv->topics_atual);
            break;
        case 3: // show
            show_topic(psrv, arg2);
            break;
        case 4: // lock
            Lock_Topico(psrv, arg2);
            break;
        case 5: // unlock
            Unlock_Topico(psrv, arg2);
            break;
        case 6: // close
            LogoutAll(psrv);
            break;
        }

    } while (strcmp(cmd, "close") != 0);

    //encerramento normal
    pthread_mutex_lock(psrv->ptrinco); 
        psrv->exit = 0;
    pthread_mutex_unlock(psrv->ptrinco); 
    //encerramento se nunca tiver tido nenhum cliente
    int fd = open(FIFO_SRV, O_WRONLY);
    if (fd == -1) {
        perror("Erro ao abrir FIFO no servidor");
        return NULL;
    }
    int sair = -1;
    write(fd, &sair, sizeof(int));
    close(fd);

    printf("Thread servidor terminar!\n");

    pthread_exit(NULL);
}

void *Persistentes(void *data) {
    
    CDATA *cdata = (CDATA *)data;

    while (cdata->exit) { 
        pthread_mutex_lock(cdata->ptrinco); 

        for (int i = 0; i < *(cdata->topics_atual); i++) {
            // Decrementa duração das mensagens
            for (int j = 0; j < cdata->pdados[i].count; j++) {
                if (cdata->pdados[i].msgs[j].duracao > 0) {
                    cdata->pdados[i].msgs[j].duracao--;
                }
            }

            for (int j = 0; j < cdata->pdados[i].count; ) {
                if (cdata->pdados[i].msgs[j].duracao == 0) {
                  
                    for (int k = j; k < cdata->pdados[i].count - 1; k++) {
                        cdata->pdados[i].msgs[k] = cdata->pdados[i].msgs[k + 1]; 
                    }
                    memset(&cdata->pdados[i].msgs[cdata->pdados[i].count - 1], 0, sizeof(Mensagem));
                    cdata->pdados[i].count--;
                } else {
                    j++;
                }
            }

            if (cdata->pdados[i].count == 0) {
                for (int k = i; k < *(cdata->topics_atual) - 1; k++) {
                    cdata->pdados[k] = cdata->pdados[k + 1]; 
                    strcpy(cdata->topics[k], cdata->topics[k + 1]); 
                }
                
                memset(&cdata->pdados[*(cdata->topics_atual) - 1], 0, sizeof(TOPICOS));
                memset(cdata->topics[*(cdata->topics_atual) - 1], 0, MAX_NAME_TOPICS);
                (*(cdata->topics_atual))--; 
                i--; 
            }
        }

        pthread_mutex_unlock(cdata->ptrinco);
        sleep(1);
    }

    printf("Thread de temporização encerrada.\n");
    pthread_exit(NULL);
}



void envia_msg_p(CDATA *pcli, char *topico, int pid) {
    int fd_cli;
    char fifo_cli[20];
    msg_env msg_send;

    pthread_mutex_lock(pcli->ptrinco); 

    TOPICOS *subs = pcli->pdados; // Estrutura de tópicos
    int *topics_atual = pcli->topics_atual;

    for (int i = 0; i < *topics_atual; i++) {
        if (strcmp(subs[i].nome, topico) == 0) {
           
            sprintf(fifo_cli, FIFO_CLI, pid);
            //printf("MSG-----%s-----\n", fifo_cli);
            fd_cli = open(fifo_cli, O_WRONLY);
            
            if (fd_cli == -1) {
                perror("Erro ao abrir FIFO");
                pthread_mutex_unlock(pcli->ptrinco);
                return; 
            }

            for (int k = 0; k < subs[i].count - 1; k++) {
                msg_send.tipo = 1;
                msg_send.pid = 1;
                msg_send.m.duracao = 1;
                strcpy(msg_send.m.conteudo, subs[i].msgs[k].conteudo);
                strcpy(msg_send.m.username, subs[i].msgs[k].username);
                strcpy(msg_send.m.topico, subs[i].nome);
                write(fd_cli, &msg_send, sizeof(msg_send));
                pthread_mutex_unlock(pcli->ptrinco);

            }

            close(fd_cli);
            break;
        }
    }

    pthread_mutex_unlock(pcli->ptrinco); // Desbloqueia o mutex
}

void envia_msg(CDATA *pcli, Mensagem new_msg, int pid) {
    int fd_cli;
    char fifo_cli[20];
    msg_env msg_send; 
    pthread_mutex_lock(pcli->ptrinco); 

    TOPICOS *subs = pcli->pdados; 
    int *topics_atual = pcli->topics_atual;

    for (int i = 0; i < *topics_atual; i++) {
        if (strcmp(subs[i].nome, new_msg.topico) == 0) {
            //printf("Tópico encontrado: %s\n", subs[i].nome);

            for (int k = 0; k < subs[i].count; k++) {
                if (subs[i].pid[k] != pid) {
                    msg_send.tipo = 1; 
                    msg_send.pid = subs[i].pid[k]; 
                    strcpy(msg_send.m.conteudo, new_msg.conteudo);
                    strcpy(msg_send.m.username, new_msg.username); 
                    strcpy(msg_send.m.topico, new_msg.topico);
                    printf("%s: %s\n", msg_send.m.username, msg_send.m.conteudo);
                    sprintf(fifo_cli, FIFO_CLI, subs[i].pid[k]);

                    printf("Enviando mensagem para FIFO: %s\n", fifo_cli);

                    fd_cli = open(fifo_cli, O_WRONLY);
                    write(fd_cli, &msg_send, sizeof(msg_send));
                    close(fd_cli); 
                }else 
                    printf("Ninguem subscrito ao topico\n");
            }
            break; 
        }
    }

    pthread_mutex_unlock(pcli->ptrinco);
}


int Verifica_login(char users[MAX_USERS][50], char *new_user, int new_pid, int *users_atual, int *pid)
{
    int existe = 0;

    for (int i = 0; i < *users_atual; i++)
    {
        if (strcmp(users[i], new_user) == 0)
            existe = 1;
    }

    if (existe == 0)
    {
        strcpy(users[*users_atual], new_user);
        pid[*users_atual] = new_pid;
        (*users_atual)++;
        return 1;
    }
    return 0;
}

int Remove_User(char users[MAX_USERS][50], char *old_user, int *users_atual, int *pid)
{
    int old_pid = -1;
    for (int i = 0; i < *users_atual; i++)
    {
        if (strcmp(users[i], old_user) == 0)
        {
            old_pid = pid[i];
            for (int j = i; j < *users_atual - 1; j++)
            {
                strcpy(users[j], users[j + 1]);
                pid[j] = pid[j + 1];
            }
            (*users_atual)--;
            return old_pid;
        }
    }
    return old_pid; // user nao existe
}

void Mostra_Topicos(char topicos[MAX_TOPICS][MAX_NAME_TOPICS], int *topicos_atual)
{
    for (int i = 0; i < *topicos_atual; i++)
    {
        printf("Topico %d: %s\n", i + 1, topicos[i]);
    }
}

void Mostra_Users(char users[MAX_USERS][50], int const *users_atual)
{
    for (int i = 0; i < *users_atual; i++)
    {
        printf("User %d: %s\n", i + 1, users[i]);
    }
}

Mensagem separa_buffer(char buffer[])
{
    Mensagem s1 = {0};
    sscanf(buffer, "%s %s %d %[^\n]", s1.topico, s1.username, &s1.duracao, s1.conteudo);
    s1.conteudo[strlen(s1.conteudo)] = '\0';
    return s1;
}

int Logout(char old_user, int pid)
{
    char fifo[20];
    sprintf(fifo, "f_%d", pid);

    int fd = open(fifo, O_WRONLY);
    if (fd < 0 && pid > 0)
    {
        perror("Erro ao abrir o FIFO destino");
        return -1;
    }
    exit_env T1;
    T1.tipo = 4;
    strcpy(T1.u.username, "exit");
    write(fd, &T1, sizeof(T1));
    close(fd);
    return 1;
}

int subscribe(CDATA *cdata, const char *topico, const char *nome, int pid) {
    
    pthread_mutex_lock(cdata->ptrinco);  

    TOPICOS *subs = cdata->pdados; 
    int *topics_atual = cdata->topics_atual;  

    // Verifica se o tópico já existe
    for (int i = 0; i < *topics_atual; i++) {
        printf("USERS %d\n", cdata->pdados[i].count);//------------------------------PODES VER AQUI O ERRO TENTANDO SUBSCREVER O MESMO TOPICO COM 2 USERS DIFERENTES
        if (strcmp(cdata->topics[i], topico) == 0) {
            // Verifica se o usuário já está inscrito
            for (int j = 0; j < subs[i].count; j++) {
                if (strcmp(subs[i].usersname[j], nome) == 0) {
                    //printf("Usuário '%s' já está inscrito no tópico '%s'.\n", nome, topico);
                    pthread_mutex_unlock(cdata->ptrinco);
                    return 2;  // Já inscrito
                }
            }
            // Adiciona o usuário ao tópico existente
            if (subs[i].count < MAX_USERS) {
                int index = subs[i].count;  
                strcpy(subs[i].usersname[index], nome);  
                subs[i].pid[index] = pid;  
                subs[i].count+=1; 

                //printf("Usuário '%s' inscrito no tópico '%s' com pid %d. Total de inscritos: %d.\n", subs[i].usersname[index], cdata->topics[i], subs[i].pid[index], subs[i].count);

                pthread_mutex_unlock(cdata->ptrinco);
                return 0;  // Inscrição bem-sucedida
            } else {
                pthread_mutex_unlock(cdata->ptrinco);
                printf("Limite de usuários atingido para o tópico '%s'.\n", topico);
                return -1;  // Limite de usuários atingido
            }
        }
    }

    // Caso o tópico não exista, cria um novo
    if (*topics_atual < MAX_TOPICS) {
        strcpy(cdata->topics[*topics_atual], topico);  
        strcpy(subs[*topics_atual].usersname[0], nome);  
        subs[*topics_atual].pid[0] = pid; 
        subs[*topics_atual].count = 1;  
        subs[*topics_atual].bloqueio = 0; 
        strcpy(subs[*topics_atual].nome, topico);  
        (*topics_atual)++; 

        //printf("Novo tópico '%s' criado. Usuário '%s' inscrito com pid %d.\n", subs[new_index].nome, subs[new_index].usersname[0], subs[new_index].pid[0]);

        pthread_mutex_unlock(cdata->ptrinco);
        return 1;  // Novo topico criado e usuario inscrito
    }

    //printf("Limite de tópicos atingido. Não foi possível criar o tópico '%s'.\n", topico);
    pthread_mutex_unlock(cdata->ptrinco);
    return -2;  // Limite de topicos atingido
}

int unsubscribe(CDATA *cdata, const char *topico, const char *nome)
{
    pthread_mutex_lock(cdata->ptrinco); 

    TOPICOS *subs = cdata->pdados; 
    int *topicos_atual = cdata->topics_atual;

    // Verifica se o tópico existe
    for (int i = 0; i < *topicos_atual; i++)
    {
        if (strcmp(cdata->topics[i], topico) == 0)
        {
            // Tópico encontrado
            for (int j = 0; j < subs[i].count; j++)
            {
                if (strcmp(subs[i].usersname[j], nome) == 0)
                {
                    // Remove o usuário da lista de inscritos
                    for (int k = j; k < subs[i].count - 1; k++)
                    {
                        strcpy(subs[i].usersname[k], subs[i].usersname[k + 1]);
                        subs[i].pid[k] = subs[i].pid[k+1];
                    }
                    subs[i].count--; // Reduz o número de inscritos

                    // Verifica se o tópico ficou vazio se sim remove
                    if (subs[i].count == 0)
                    {
                        for (int k = i; k < *topicos_atual - 1; k++)
                        {
                            subs[k] = subs[k + 1];
                            strcpy(cdata->topics[k], cdata->topics[k + 1]);
                        }
                        // Limpa a última posição dos tópicos e da estrutura de dados
                        memset(&subs[*topicos_atual - 1], 0, sizeof(TOPICOS));
                        memset(cdata->topics[*topicos_atual - 1], 0, MAX_NAME_TOPICS);
                        (*topicos_atual)--;
                    }

                    pthread_mutex_unlock(cdata->ptrinco);
                    return 0; // unsubcribe bem-sucedida
                }
            }
            pthread_mutex_unlock(cdata->ptrinco);
            return 2; // Usuário não inscrito
        }
    }

    pthread_mutex_unlock(cdata->ptrinco);
    return 1; // Tópico não encontrado
}

int verifica_struct_srv(const char *cmd)
{

    if (strcmp(cmd, "users") == 0)
    {
        return 0;
    }
    else if (strcmp(cmd, "remove") == 0)
    {
        return 1;
    }
    else if (strcmp(cmd, "topics") == 0)
    {
        return 2;
    }
    else if (strcmp(cmd, "show") == 0)
    {
        return 3;
    }
    else if (strcmp(cmd, "lock") == 0)
    {
        return 4;
    }
    else if (strcmp(cmd, "unlock") == 0)
    {
        return 5;
    }
    else if (strcmp(cmd, "close") == 0)
    {
        return 6;
    }
    else
        return -1; // comando errado
}

int adiciona_messagem(CDATA *cdata, Mensagem new_msg, int pid)
{
    pthread_mutex_lock(cdata->ptrinco); 

    TOPICOS *topics = cdata->pdados; 
    int *topicos_atual = cdata->topics_atual;

    for (int i = 0; i < *topicos_atual; i++)
    {
        if (strcmp(cdata->topics[i], new_msg.topico) == 0)
        {       
            // Verifica se o tópico está bloqueado
            if (topics[i].bloqueio)
            {
                printf("Tópico '%s' está bloqueado. Mensagem não adicionada.\n", new_msg.topico);
                pthread_mutex_unlock(cdata->ptrinco);
                return -1; 
            }

            if (topics[i].count >= MAX_MSG_PRESISTENTES)
            {
                printf("Tópico '%s' atingiu o limite de mensagens. Mensagem não adicionada.\n", new_msg.topico);
                pthread_mutex_unlock(cdata->ptrinco);
                return -2; 
            }

            // Adiciona a nova mensagem
            strcpy(topics[i].msgs[topics[i].count].conteudo, new_msg.conteudo);
            topics[i].msgs[topics[i].count].duracao = new_msg.duracao;
            strcpy(topics[i].msgs[topics[i].count].topico, new_msg.topico);
            strcpy(topics[i].msgs[topics[i].count].username, new_msg.username);
            
            printf("[DEBUG] Mensagem adicionada ao tópico '%s' na posição %d.\n", new_msg.topico, topics[i].count);
            topics[i].count++; // Incrementa o contador de mensagens

            pthread_mutex_unlock(cdata->ptrinco);
            return 0; // Sucesso
        }
    }

    printf("[DEBUG] Tópico '%s' não encontrado.\n", new_msg.topico);
    pthread_mutex_unlock(cdata->ptrinco);
    return 1;
}


int Encontra_pos(int pid[MAX_USERS], int *users_atual, int user_pid){
    //encontra no array do pid a posicao do user_pid
    for (int i = 0; i < *users_atual; i++)
    {
        if (pid[i] == user_pid)
        {
            return i; 
        }
    }
    return -1; 
}

void listar_subscricoes(CDATA *cdata)
{
    pthread_mutex_lock(cdata->ptrinco); // Inicia a seção crítica

    int *topicos_atual = cdata->topics_atual;

    printf("Lista de Tópicos e Usuários Subscritos:\n");
    for (int i = 0; i < *topicos_atual; i++)
    {
        printf("Tópico: %s | Estado %d Inscritos %d\n", cdata->topics[i], cdata->pdados[i].bloqueio, cdata->pdados[i].count);
        for (int j = 0; j < cdata->pdados[i].count; j++){
                printf("  Usuário %d: %s com pid %d\n", j, cdata->pdados[i].usersname[j], cdata->pdados[i].pid[j]);
        }
    }

    pthread_mutex_unlock(cdata->ptrinco); 
}

void LogoutAll(CDATA *dados)
{
    // se nao tiver mensagens presistentes logout
    for (int i = 2; i < *dados->user_atual; i++)
    {
        Logout(*dados->users[i], dados->pid[i]);
    }
}

void Lock_Topico(CDATA *psrv, char *arg2)
{
    pthread_mutex_lock(psrv->ptrinco); // Inicia a seção crítica

    int topicos_atual = *psrv->topics_atual; 

    for (int i = 0; i < topicos_atual; i++)
    {
        if (strcmp(psrv->topics[i], arg2) == 0)
        { // Encontra o tópico correspondente
            psrv->pdados[i].bloqueio = 1; // Define o bloqueio para 1
            printf("[DEBUG] Tópico '%s' bloqueado.\n", arg2);
            pthread_mutex_unlock(psrv->ptrinco); 
            return;
        }
    }

    printf("[DEBUG] Tópico '%s' não encontrado.\n", arg2);
    pthread_mutex_unlock(psrv->ptrinco); 
}

void show_topic(CDATA *cdata, const char *topico)
{
    pthread_mutex_lock(cdata->ptrinco); 

    TOPICOS *topics = cdata->pdados; 
    int *topicos_atual = cdata->topics_atual;

    // Procura o tópico pelo nome
    for (int i = 0; i < *topicos_atual; i++)
    {
        if (strcmp(cdata->topics[i], topico) == 0)
        {
            printf("Mensagens no tópico '%s': %d\n", topico, cdata->pdados[i].count);

            for (int j = 0, p = 1; j <= topics[i].count; j++){
                if(strlen(topics[i].msgs[j].conteudo)>0){
                    printf("  [%d] %s: %s\n", p++, topics[i].msgs[j].username, topics[i].msgs[j].conteudo);
                }
            }

            pthread_mutex_unlock(cdata->ptrinco);
            return;
        }
    }
    pthread_mutex_unlock(cdata->ptrinco);
}

void Unlock_Topico(CDATA *psrv, char *arg2)
{
    pthread_mutex_lock(psrv->ptrinco); 

    int topicos_atual = *psrv->topics_atual; // Número atual de tópicos

    for (int i = 0; i < topicos_atual; i++)
    {
        if (strcmp(psrv->topics[i], arg2) == 0)
        { // Encontra o tópico correspondente
            psrv->pdados[i].bloqueio = 0;
            pthread_mutex_unlock(psrv->ptrinco);
            return;
        }
    }

    printf("[DEBUG] Tópico '%s' não encontrado.\n", arg2);
    pthread_mutex_unlock(psrv->ptrinco); 
}