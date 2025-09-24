/*=========================== Cliente basico UDP ===============================
Este cliente destina-se a enviar mensagens passadas na linha de comando, sob
a forma de um argumento, para um servidor especifico cuja locacao e' dada
pelas seguintes constantes: SERV_HOST_ADDR (endereco IP) e SERV_UDP_PORT (porto)

O protocolo usado e' o UDP.
==============================================================================*/

#include <winsock.h>
#include <stdio.h>

//ex1
#pragma comment (lib, "Ws2_32.lib")
 
#define SERV_HOST_ADDR "127.0.0.1"
#define SERV_UDP_PORT  6000
#define TIMEOUT 1000 //ms

#define BUFFERSIZE     4096

void Abort(char *msg);

/*________________________________ main _______________________________________
*/

int main( int argc , char *argv[] )
{

	SOCKET sockfd;
	int msg_len, iResult;
	struct sockaddr_in serv_addr;
	char buffer[BUFFERSIZE];
	WSADATA wsaData;

	int serv_addr_size = sizeof(serv_addr);

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
		Abort("Impossibilidade de criar socket\n");

	/*================= PREENCHE ENDERECO DO SERVIDOR ====================*/

	//EX7

	int opt = 1;
	setsockopt(sockfd,SOL_SOCKET, SO_BROADCAST, (char*)&opt, sizeof(opt));

	//EX8
	DWORD timeout = TIMEOUT;
	setsockopt(sockfd, SOL_SOCKET, SO_RCVTIMEO, (char*)&timeout, sizeof(timeout));
	//se nao tiver servidor ativo apos timeout encerra a comunicaçao

	memset( (char*)&serv_addr , 0, sizeof(serv_addr) ); /*Coloca a zero todos os bytes*/
	serv_addr.sin_family = AF_INET; /*Address Family: Internet*/
	serv_addr.sin_addr.s_addr = inet_addr(argv[2]); /*IP no formato "dotted decimal" => 32 bits*/ //SERV_HOST_ADDR no lugar de argv
	serv_addr.sin_port = htons(atoi(argv[3])); /*Host TO Netowork Short*/ //SERV_UDP_PORT no lugara de argv converter para numeric a string de entrada

	/*====================== ENVIA MENSAGEM AO SERVIDOR ==================*/

	msg_len = strlen(argv[1]);

	if(sendto( sockfd , argv[1] , msg_len+1 , 0 , (struct sockaddr*)&serv_addr , sizeof(serv_addr) ) == SOCKET_ERROR)
		Abort("O subsistema de comunicacao nao conseguiu aceitar o datagrama");

	printf("%s ---- %s", argv[2], argv[3]);
	printf("\n<CLI1>Mensagem enviada ... a entrega nao e' confirmada.\n"); 

	//uso da funçao getsockname
	
	struct sockaddr_in client_sock_name; //cria estrutura do tipo sockaddr_in com nome x
	int client_sock_name_size = sizeof(client_sock_name);

	getsockname(sockfd, &client_sock_name, &client_sock_name_size); //usando a funçao ele vai ao socket verificar o ip e 
																	//a porta ustilizada e escrever na estrutura	

	//printf("<CLI1>Porto de cliente %d\n", ntohs(client_sock_name.sin_port.s_addr)); //usando a funçao ntons convertemos de u_short para decimal
	

	//---------------------------- Recebe mensagem-------------------

	struct sockaddr_in confirm;
	int confrim_size = sizeof(confirm);

	//ate ao ex10
	//int nbytes = recvfrom(
	//	sockfd,								//Descritor do socket
	//	buffer,								//Buffer para guardar
	//	sizeof(buffer),						//limite maximo que pode receber (tamanho buffer)
	//	0,									//Flags
	//	&serv_addr,							//Quem esta a enviar a mensagem //serv_addr          //confirm
	//	&serv_addr_size						//Size of quem envia mensagem   //serv_addr_size	//confirm_size
	//);

	int resposta;

	int nbytes = recvfrom(
		sockfd,								
		(char *)&resposta,								
		sizeof(resposta),						
		0,									
		(struct sockaddr*)&serv_addr, 
		sizeof(serv_addr)
	);

	//if(confirm.sin_addr.s_addr == serv_addr.sin_addr.s_addr && confirm.sin_port == serv_addr.sin_port)
		//printf("\n<CLI1>A resposta é recebida e foi efetiamente enviada pelo servidor\n");


	if (nbytes == SOCKET_ERROR)
		Abort("Erro na recepcao de datagrams");

	//buffer[nbytes]='\0'; /*Termina a cadeia de caracteres recebidos com '\0'*/

	//ex10

	//printf("\n<CLI1>Mensagem recebida {%s}\n", buffer);
	printf("\n<CLI1>Numero de bytes da mensagem {%d}\n", ntohl(resposta));
	
	//EX 6
	//serv_addr.sin_addr

	

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
