/*============================== Servidor basico UDP ==========================
  Este servidor UDP destina-se a mostrar os conteudos dos datagramas recebidos.
  O porto de escuta encontra-se definido pela constante SERV_UDP_PORT.
  Assume-se que as mensagens recebida sao cadeias de caracteres (ou seja, 
  "strings").
===============================================================================*/

#include <stdio.h>
#include <winsock.h>

#pragma comment (lib, "Ws2_32.lib")

#define SERV_UDP_PORT 6000
#define BUFFERSIZE    4096
#define MAX_RESPOSTA  10
void Abort(char *msg);

/*________________________________ main ________________________________________
*/

int main( int argc , char *argv[] )
{
	SOCKET sockfd;
	int iResult, nbytes, nbytes2;
	struct sockaddr_in serv_addr;
	struct sockaddr_in cliente_addr;
	struct sockaddr_in cliente_addr2;
	int cliente_addr_len = sizeof(cliente_addr);
	int cliente_addr2_len = sizeof(cliente_addr2);
	char buffer[BUFFERSIZE], buffer1[BUFFERSIZE], resposta[MAX_RESPOSTA];
	WSADATA wsaData;
	/*=============== INICIA OS WINSOCKS ==============*/

	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*============ CRIA O SOCKET PARA RECEPCAO/ENVIO DE DATAGRAMAS UDP ============*/

	if((sockfd = socket( PF_INET , SOCK_DGRAM , 0)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket");

	/*=============== ASSOCIA O SOCKET AO  ENDERECO DE ESCUTA ===============*/

	/*Define que pretende receber datagramas vindos de qualquer interface de 
	rede, no porto pretendido*/

	memset( (char*)&serv_addr , 0, sizeof(serv_addr) );
	serv_addr.sin_family = AF_INET; /*Address Family: Internet*/
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /*Host TO Network Long*/
	serv_addr.sin_port = htons(SERV_UDP_PORT);  /*Host TO Network Short*/

	/*Associa o socket ao porto pretendido*/

	if(bind( sockfd , (struct sockaddr *)&serv_addr , sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de registar-se para escuta");


	/*================ PASSA A ATENDER CLIENTES INTERACTIVAMENTE =============*/

	while(1){

		fprintf(stderr,"<SER-2>Esperando datagrama...\n");

		nbytes=recvfrom(sockfd , buffer , sizeof(buffer) , 0 , (struct sockaddr*)&cliente_addr, &cliente_addr_len);

		if (nbytes == SOCKET_ERROR)
			Abort("Erro na recepcao de datagrams");
		else
			fprintf(stdout, "\n SERV-11 CLIENTE 1 REGISTADO\n");
		//dialogo com o segundo cliente
		
		buffer[nbytes] = '\0';

		fprintf(stdout, "\n Mensagem recebida do CLIENTE 1 %s\n", buffer);

		buffer[nbytes]= '\0'; /*Termina a cadeia de caracteres recebidos com '\0'*/

		nbytes2=recvfrom(sockfd, buffer, sizeof(buffer), 0, (struct sockaddr*)&cliente_addr2, &cliente_addr2_len);



		if (nbytes2 == SOCKET_ERROR)
			Abort("Erro na recepcao de datagrams");
		else
			fprintf(stdout, "\n SERV-11 CLIENTE 2 REGISTADO\n");

		printf("\n<SER-2>Mensagem recebida {%s}\n",buffer);

		//sprintf_s(resposta, sizeof(resposta), "%d", strlen(buffer));

		if (sendto(sockfd, (char*)&cliente_addr, sizeof(cliente_addr), 0, (struct sockaddr*)&cliente_addr2, sizeof(cliente_addr2)) == SOCKET_ERROR)
			Abort("O subsistema de comunicacao nao conseguiu aceitar o datagrama");


		
	}
	
}

/*________________________________ Abort________________________________________
  Mostra uma mensagem de erro e o código associado ao ultimo erro com Winsocks. 
  Termina a aplicacao com "exit status" a 1 (constante EXIT_FAILURE)
________________________________________________________________________________*/

void Abort(char *msg)
{
	WSACleanup();
	fprintf(stderr,"<SERV>Erro fatal: <%s> (%d)\n",msg, WSAGetLastError());
	exit(EXIT_FAILURE);

}