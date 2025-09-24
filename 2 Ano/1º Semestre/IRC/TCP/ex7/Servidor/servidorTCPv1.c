/*________________________________servidorTCPv1.c___________________________________*/
/*======================= Servidor interactivo TCP ============================
Este servidor destina-se mostrar mensagens recebidas via TCP, no porto
definido pela constante SERV_TCP_PORT.
Trata-se de um servidor que envia confirmacao (o comprimento, em bytes, da
mensagem recebida).
===============================================================================
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <winsock.h>

#define SERV_TCP_PORT  6000
#define BUFFERSIZE     4096

#pragma comment (lib, "Ws2_32.lib")

void Abort(char* msg, SOCKET s);
void AtendeCliente(LPVOID param);

/*________________________________ main ________________________________________
*/
int main(int argc, char* argv[]) {

	SOCKET sock = INVALID_SOCKET, newSock = INVALID_SOCKET;
	int iResult, termina = 0;
	int cliaddr_len;
	struct sockaddr_in cli_addr, serv_addr;
	WSADATA wsaData;
	SECURITY_ATTRIBUTES sa;
	DWORD thread_id;

	/*=============== INICIA OS WINSOCKS ==============*/
	iResult = WSAStartup(MAKEWORD(2, 2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*================== ABRE SOCKET PARA ESCUTA DE CLIENTES ================*/
	if ((sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) == INVALID_SOCKET)
		Abort("Impossibilidade de abrir socket", sock);

	/*=================== PREENCHE ENDERECO DE ESCUTA =======================*/
	memset((char*)&serv_addr, 0, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);  /*Recebe de qq interface*/
	serv_addr.sin_port = htons(SERV_TCP_PORT);  /*Escuta no porto Well-Known*/

	/*====================== REGISTA-SE PARA ESCUTA =========================*/
	if (bind(sock, (struct sockaddr*)&serv_addr, sizeof(serv_addr)) == SOCKET_ERROR)
		Abort("Impossibilidade de registar-se para escuta", sock);

	/*============ AVISA QUE ESTA PRONTO A ACEITAR PEDIDOS ==================*/
	if (listen(sock, 5) == SOCKET_ERROR)	//numero de pedidos em espera que o servidor pode ter
		Abort("Impossibilidade de escutar pedidos", sock);

	//CreateThread(&sa, 0, (LPTHREAD_START_ROUTINE)AtendeTeclado, (PVOID)newSock, (DWORD)0, &thread_id);

	/*================ PASSA A ATENDER CLIENTES INTERACTIVAMENTE =============*/
	cliaddr_len = sizeof(cli_addr);

	while (1) {
		//if (terminado) {
		//	break;
		//}
		/*====================== ATENDE PEDIDO ========================*/
		if ((newSock = accept(sock, (struct sockaddr*)&cli_addr, &cliaddr_len)) == SOCKET_ERROR)
			fprintf(stderr, "<SERV> Impossibilidade de aceitar cliente...\n");
		else {
			sa.nLength = sizeof(sa);
			sa.lpSecurityDescriptor = NULL;
			if (CreateThread(&sa, 0, (LPTHREAD_START_ROUTINE) AtendeCliente, (PVOID)newSock, (DWORD) 0, &thread_id) == NULL) {
				close(newSock);
			}
		}
	}
	close (sock);
	ExitThread(0); //se nao meter 0, tudo é terminado e nao é suposto
}

/*___________________________ AtendeCliente ____________________________________
Atende cliente.
______________________________________________________________________________*/

void AtendeCliente(LPVOID param) {
	static char buffer[BUFFERSIZE];
	static unsigned int cont = 0;
	int nbytes, nBytesSent;
	struct sockaddr_in cli_addr;
	
	SOCKET sock;
	sock = (SOCKET)param;
	nbytes = sizeof(cli_addr);
	if (getpeername(sock, (struct sockaddr*)&cli_addr, &nbytes) != SOCKET_ERROR)
		printf("Sou a thread com o id %d e vou atender o cliente <%s : %d>", GetCurrentThread(), inet_ntoa(cli_addr.sin_addr), ntohs(cli_addr.sin_port));

	/*==================== PROCESSA PEDIDO ==========================*/
	while (1) {
		switch ((nbytes = readLine(param, buffer, BUFFERSIZE-1))) {

		case SOCKET_ERROR:
			fprintf(stderr, "\n<SER> Erro na recepcao de dados...\n");
			break;

		case  0:
			fprintf(stderr, "\n<SER> O cliente nao enviou dados...\n");

			break;

		default:
			buffer[nbytes] = '\0';
			printf("\n<SER %d> Mensagem n. %d recebida {%s}\n", GetCurrentThreadId(),++cont, buffer);

			/*============ ENVIA CONFIRMACAO =============*/
			printf("<SER> Confirma recepcao de mensagem.\n");
			sprintf_s(buffer, BUFFERSIZE, "%d", nbytes);
			nbytes = strlen(buffer);

			if ((nBytesSent = writeN(param, buffer, nbytes, 0)) == SOCKET_ERROR)
				fprintf(stderr, "<SER> Impossibilidade de Confirmar.\n");
			else if (nBytesSent < nbytes)
				fprintf(stderr, "<SER> Mensagem confirmada, mas truncada.\n");
			else
				printf("<SER> Mensagem confirmada.\n");

			if (!strcmp(buffer, "sair")) {
				printf("Comando de sair");
				return;
			}
		}
		
	}

}
/*
void AtendeTeclado(LPVOID param) {
	static char buffer[BUFFERSIZE];
	static unsigned int cont = 0;
	int nbytes, nBytesSent;
	struct sockaddr_in cli_addr;

	SOCKET sock;
	sock = (SOCKET)param;
	nbytes = sizeof(cli_addr);
	if (getpeername(sock, (struct sockaddr*)&cli_addr, &nbytes) != SOCKET_ERROR)
		printf("Sou a thread com o id %d e vou atender o teclado>", GetCurrentThread(), inet_ntoa(cli_addr.sin_addr));

}*/

/*________________________________ Abort________________________________________
Mostra a mensagem de erro associada ao ultimo erro no SO e abando com
"exit status" a 1
_______________________________________________________________________________
*/
void Abort(char* msg, SOCKET s)
{
	fprintf(stderr, "\a<SER >Erro fatal: <%d>\n", WSAGetLastError());

	if (s != INVALID_SOCKET)
		closesocket(s);

	WSACleanup();

	exit(EXIT_FAILURE);
}

int writeN(SOCKET sock, char* buffer, int nbytes) {
	int nLeft, nWritten;
	nLeft = nbytes;
	while (nLeft > 0) {
		nWritten = send(sock, buffer, nLeft, 0);
		if (nWritten == 0 || nWritten == SOCKET_ERROR)
			return nWritten; //Erro ou EOF
		nLeft -= nWritten;
		buffer += nWritten;
	}
	return nWritten;
}

int readLine(SOCKET sock, char* buffer, int nbytes) {
	int nread, i;
	char c;

	i = 0;
	while (i < nbytes - 1) { //-1 para deixar espaço para o '\0'
		nread = recv(sock, &c, sizeof(c), 0);
		if (nread == SOCKET_ERROR)
			return nread;
		if (nread == 0)
			break;
		if (c == '\r')
			continue;
		if (c == '\n')
			break;
		buffer[i++] = c;
	}
	buffer[i] = '\0';
	return i;

}
