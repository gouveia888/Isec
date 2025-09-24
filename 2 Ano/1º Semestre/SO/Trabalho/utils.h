#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <signal.h>
#include <time.h>
#include <string.h>
#include <ctype.h>
#include <fcntl.h>
#include <unistd.h>
#include <time.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/select.h>
#include <pthread.h>

#define FIFO_SRV "tubo"
#define FIFO_CLI "f_%d"

#define MAX_USERS 10
#define MAX_TOPICS 20
#define MAX_MSG_PRESISTENTES 5
#define MAX_NAME_TOPICS 20
#define MAX_MSG_SIZE 300

typedef struct{
    char username[50], topico[MAX_NAME_TOPICS];
    int duracao;
    char conteudo[MAX_MSG_SIZE];
}Mensagem;

typedef struct{
    char username[50];
    int pid;
}Login;

typedef struct{
    char topico[50];
    int i;
}Topics;

typedef struct{
    int tipo, pid;
    char topico[50];
}Topics_env;

typedef struct{
    int tipo, pid;
    Mensagem m;
}msg_env;

typedef struct{
    int tipo, pid;
    Login u;
}login_env;

typedef struct{
    int tipo, pid;
    Login u;
}exit_env;

typedef struct{
    int tipo, pid;
    Topics t;
}sub_env;

typedef struct{
    int tipo, pid;
    Topics t;
}unsub_env;

typedef struct{
    int tipo, pid;
}ask_topics;

typedef struct {
    char topicosub[MAX_TOPICS][MAX_NAME_TOPICS]; // Array para armazenar os tópicos inscritos
    int bloqueio[]; // Contador de tópicos inscritos
} Subscriptions;

