Z// FIcheiro feed.c

#include "utils.h"

#define MAX_BUFFER 360

typedef struct
{
    char cmd[50], topico[MAX_NAME_TOPICS];
    int duracao;
    char conteudo[MAX_MSG_SIZE];
} Auxiliar;

Auxiliar separa_buffer(char buffer[]);
int verifica_struct_cli(const char *cmd);

int main(int argc, char *argv[]){

    int fd_cli, fd, n, tipo;
    char buffer[MAX_BUFFER], fifo_cli[20];
    Auxiliar comp;
    fd_set fds;

    if (argc != 2)
    {
        printf("SINTAXE INCORRETA!\n");
        exit(2);
    }

    // definir nome do fifo do cliente (sprintf)
    sprintf(fifo_cli, FIFO_CLI, getpid());
    mkfifo(fifo_cli, 0600);
    fd_cli=open(fifo_cli, O_RDWR);
    // open
    fd = open(FIFO_SRV, O_WRONLY);
    // Adiciona um sleep antes de enviar a inscrição

    // verifica user
    login_env log;
    log.tipo = 5;
    log.pid = getpid();
    log.u.pid = getpid();
    strcpy(log.u.username, argv[1]);

    // enviar tipo para manager
    write(fd, &log, sizeof(login_env));

    printf("INICIO...\n");

    if (access(FIFO_SRV, F_OK) != 0)
    {
        printf("[ERROR] O SERVIDOR NAO ESTA A CORRER!\n");
        exit(3);
    }
        
    //vaariavel para encerrar o feed
    Login SAIR;
    
    printf("CMD>\n");

    do{
        
        FD_ZERO(&fds);
        FD_SET(0, &fds);
        FD_SET(fd_cli, &fds);
        
        int n = select(fd_cli + 1, &fds, NULL, NULL, NULL);

        if (n == -1)
        {
            printf("ERRO no Select\n");
            break;
        }
        // stdin
        if (FD_ISSET(0, &fds))
        {
            printf("CMD>\n");
            fgets(buffer, MAX_BUFFER, stdin);
            comp = separa_buffer(buffer);
            switch (verifica_struct_cli(comp.cmd)){

                case 0: // cmd = topics
                    ask_topics topicos;
                    topicos.tipo = 0;
                    topicos.pid = getpid();
                    write(fd, &topicos, sizeof(ask_topics));
                    break;

                case 1: // cmd = msg
                    msg_env T1;
                    T1.tipo = 1;
                    T1.pid = getpid();
                    strcpy(T1.m.username, argv[1]);
                    strcpy(T1.m.topico, comp.topico);
                    strcpy(T1.m.conteudo, comp.conteudo);
                    if(comp.duracao < 0)
                        T1.m.duracao = 0;
                    else
                        T1.m.duracao = comp.duracao;            
                    write(fd, &T1, sizeof(T1));
                    break;

                case 2: // cmd = subscribe
                    sub_env T2;
                    T2.tipo = 2;
                    T2.pid = getpid();
                    strcpy(T2.t.topico, comp.topico); // comp.topico deve ser "futebol"
                    // Enviar a solicitação de inscrição ao manager
                    write(fd, &T2, sizeof(T2));
                    break;

                case 3: // cmd = unsubcribe
                    unsub_env T3;
                    T3.tipo = 3;
                    T3.pid = getpid();
                    strcpy(T3.t.topico, comp.topico);
                    write(fd, &T3, sizeof(T3));
                    break;
                case 4: // cmd = exit
                    exit_env T4;
                    T4.tipo = 4;
                    T4.pid = getpid();
                    strcpy(T4.u.username, argv[1]); 
                    write(fd, &T4, sizeof(T4));
                    break;
                case 5: // cmd = login
                    login_env T5;
                    T5.tipo = 5;
                    T5.pid = getpid();
                    strcpy(T5.u.username, comp.topico);
                    write(fd, &T5, sizeof(T5));
                    break;

                default:
                    break;
            }
        
        }
        // manager
        if (FD_ISSET(fd_cli, &fds)){
            n = read(fd_cli, &tipo, sizeof(int)); 
            //printf("Tipo recebido: %d\n", tipo);
            if(n == sizeof(int)){ 
                switch (tipo){

                    case 0: // cmd = topics
                        char topicos[MAX_TOPICS][MAX_NAME_TOPICS];
                        n = read(fd_cli, topicos, sizeof(topicos));
                        int i=0;
                        while(strcmp(topicos[i], "") != 0){
                            printf("Topico %d: %s\n",i+1,topicos[i]);
                            i++;
                        }
                        break;

                    case 1: // cmd = msg
                        Mensagem recebe_msg;
                        n = read(fd_cli, &tipo, sizeof(int));
                        n = read(fd_cli, &recebe_msg, sizeof(Mensagem));
                        printf("%s: %s\n",recebe_msg.username, recebe_msg.conteudo);
                        break;

                    case 4: // cmd = exit
                        n = read(fd_cli, &tipo, sizeof(int));  
                        n = read(fd_cli, &SAIR, sizeof(SAIR));
                        exit_env T4;
                        T4.tipo = 4;
                        T4.pid = getpid();
                        strcpy(T4.u.username, argv[1]); 
                        write(fd, &T4, sizeof(T4));
                        printf("Sessao encerrada!\n");
                        break;

                    default:
                        break;
                }
            }
        }

    }while (strcmp(SAIR.username, "exit") != 0);

    close(fd);
    close(fd_cli);
    unlink(fifo_cli);
    exit(0);

    return 1;
}

Auxiliar separa_buffer(char buffer[])
{
    Auxiliar s1 = {0};

    sscanf(buffer, "%s %s %d %[^\n]", s1.cmd, s1.topico, &s1.duracao, s1.conteudo);
    return s1;
}

int verifica_struct_cli(const char *cmd){

    if(strcmp(cmd,"topics")==0) {
        return 0;
    }
    else if(strcmp(cmd,"msg")==0){
        return 1;
    }
    else if(strcmp(cmd,"subcribe")==0){
        return 2;
    }
    else if(strcmp(cmd,"unsubcribe")==0){
        return 3;
    }
    else if(strcmp(cmd,"exit")==0){
        return 4;
    }
    else if(strcmp(cmd,"login")==0){
        return 5;
    }
    else
        return -1; //comando errado

}