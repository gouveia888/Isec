/*=========================== Cliente basico UDP ===============================
Este cliente destina-se a enviar mensagens passadas na linha de comando, sob
a forma de um argumento, para um servidor especifico cuja locacao e' dada
pelas seguintes constantes: SERV_HOST_ADDR (endereco IP) e SERV_UDP_PORT (porto)

O protocolo usado e' o UDP.
==============================================================================*/

#include <winsock.h>
#include <stdio.h>

#pragma comment (lib, "Ws2_32.lib")
 
#define SERV_HOST_ADDR "127.0.0.1"
#define SERV_UDP_PORT  6000

#define BUFFERSIZE     4096
#define TIMEOUT 1000
void Abort(char *msg);

/*________________________________ main _______________________________________
*/

int main( int argc , char *argv[] )
{

	SOCKET sockfd;
	int msg_len, iResult, socka_ddr_size;
	struct sockaddr_in serv_addr;
	struct sockaddr_in cliente_addr;
	struct sockaddr_in outro;
	char buffer[BUFFERSIZE];
	WSADATA wsaData;

	socka_ddr_size = sizeof(struct sockaddr_in);
	/*========================= TESTA A SINTAXE =========================*/

	if(argc != 4){
		fprintf(stderr,"Sintaxe: %s frase_a_enviar\n",argv[0]);
		getchar(); //system("pause");
		exit(EXIT_FAILURE);
	}

	/*=============== INICIA OS WINSOCKS ==============*/

	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar(); //system("pause");
		exit(1);
	}

	/*=============== CRIA SOCKET PARA ENVIO/RECEPCAO DE DATAGRAMAS ==============*/

	sockfd = socket( PF_INET , SOCK_DGRAM , 0 );
	if(sockfd == INVALID_SOCKET)
		Abort("Impossibilidade de criar socket");

	/*================= PREENCHE ENDERECO DO SERVIDOR ====================*/

	memset( (char*)&serv_addr , 0, sizeof(serv_addr) ); /*Coloca a zero todos os bytes*/
	serv_addr.sin_family = AF_INET; /*Address Family: Internet*/
	serv_addr.sin_addr.s_addr = inet_addr(argv[2]); /*IP no formato "dotted decimal" => 32 bits*/
	serv_addr.sin_port = htons(atoi(argv[3])); /*Host TO Netowork Short*/

	//EX7
	int opt = 1;
	setsockopt(sockfd, SOL_SOCKET, SO_BROADCAST, (char*)&opt, sizeof(opt));

	//EX8
	//DWORD timeout = TIMEOUT;
	//setsockopt(sockfd, SOL_SOCKET, SO_RCVTIMEO, (char*)&timeout, sizeof(timeout));

	/*====================== ENVIA MENSAGEM AO SERVIDOR ==================*/

	msg_len = strlen(argv[1]);

	if(sendto( sockfd , argv[1] , msg_len+1 , 0 , (struct sockaddr*)&serv_addr , sizeof(serv_addr) ) == SOCKET_ERROR)
		Abort("O subsistema de comunicacao nao conseguiu aceitar o datagrama");

	printf("<CLI-2>Mensagem enviada ... a entrega nao e' confirmada.\n"); 

	char msg_rec[BUFFERSIZE];

	int nbytes = recvfrom(sockfd, &outro, sizeof(outro), 0, &cliente_addr, &socka_ddr_size);

	//verificar se o ip e do servidor e o porto e do servidor
	if (serv_addr.sin_addr.s_addr == cliente_addr.sin_addr.s_addr && serv_addr.sin_port == cliente_addr.sin_port){
		//e o cliente 2
		sendto(sockfd, &outro, sizeof(outro), 0, &outro, sizeof(outro));
		printf("<CLI-2> Mensagem do par oriundo: %s e do Porto %d\n", inet_ntoa(cliente_addr.sin_addr), ntohs(cliente_addr.sin_port));
	}else{
		//esta a ser contactado pelo ciente 1
		printf("<CLI-1> Mensagem do par oriundo: %s e do Porto %d\n", inet_ntoa(cliente_addr.sin_addr), ntohs(cliente_addr.sin_port));
	}

	/*========================= FECHA O SOCKET ===========================*/

	closesocket(sockfd);
	WSACleanup();

	printf("\n");
	getchar();
	exit(EXIT_SUCCESS);
}

/*________________________________ Abort________________________________________
  Mostra uma mensagem de erro e o código associado ao ultimo erro com Winsocks. 
  Termina a aplicacao com "exit status" a 1 (constante EXIT_FAILURE)
________________________________________________________________________________*/

void Abort(char *msg)
{

	fprintf(stderr,"<CLI1>Erro fatal: <%s> (%d)\n",msg, WSAGetLastError());
	exit(EXIT_FAILURE);

}
