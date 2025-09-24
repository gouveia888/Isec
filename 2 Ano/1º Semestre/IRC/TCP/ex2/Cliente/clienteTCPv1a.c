/*=========================== Cliente basico TCP ===============================
Este cliente destina-se a enviar mensagens passadas na linha de comando, sob
a forma de um argumento, para um servidor especifico cujo socket e' fornecido através
da linha de comando. Tambem e' aguarda confirmacao (trata-se do comprimento da mensagem).

O protocolo usado e' o TCP.
==============================================================================*/

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <winsock.h>

#pragma comment (lib, "Ws2_32.lib")

#define BUFFERSIZE     4096

void Abort(char *msg, SOCKET s); //temos que mandar socket para poder fechar ligação

int writeN(SOCKET sock, char* buffer, int nbytes);
int readLine(SOCKET sock, char* buffer, int nbytes);

/*________________________________ main ________________________________________
*/

int main(int argc,char *argv[]){

	SOCKET sock = INVALID_SOCKET;
	int msg_len, nbytes, iResult;
	struct sockaddr_in serv_addr;
	char buffer[BUFFERSIZE];
	WSADATA wsaData;

	if(argc!=4){ /*Testa sintaxe*/
		fprintf(stderr,"<CLI> Sintaxe: %s \"frase_a_enviar\" ip_destino porto_destino\n",argv[0]);
		exit(EXIT_FAILURE);
	}

	/*=============== INICIA OS WINSOCKS ==============*/
	iResult = WSAStartup(MAKEWORD(2,2), &wsaData);
	if (iResult != 0) {
		printf("WSAStartup failed: %d\n", iResult);
		getchar();
		exit(1);
	}

	/*=============== ABRE SOCKET PARA CONTACTAR O SERVIDOR ==============*/
	if((sock=socket(PF_INET,SOCK_STREAM,IPPROTO_TCP)) == INVALID_SOCKET) // apenas muda os parametros em comparação ao UDP
		Abort("Impossibilidade de abrir socket", sock);

	/*================= PREENCHE ENDERECO DO SERVIDOR ====================*/
	memset((char*)&serv_addr, 0, sizeof(serv_addr));	/*a zero todos os bytes*/
	serv_addr.sin_family=AF_INET;				/*Address Family - Internet*/
	serv_addr.sin_addr.s_addr=inet_addr(argv[2]);   
	serv_addr.sin_port=htons(atoi(argv[3]));          

	/*========================== ESTABELECE LIGACAO ======================*/
	if(connect(sock,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) == SOCKET_ERROR) // novo não havia em UDP
		Abort("Impossibilidade de estabelecer ligacao", sock);

	/*====================== ENVIA MENSAGEM AO SERVIDOR ==================*/
	msg_len=strlen(argv[1]);

	if(nbytes= writeN(sock, argv[1], msg_len) == SOCKET_ERROR)
		Abort("Impossibilidade de transmitir mensagem...", sock);
	else 
		fprintf(stderr, "<CLI> Mensagem \"%s\" enviada\n", argv[1]);

	msg_len = strlen("\n");
	if (writeN(sock, "\n", msg_len) == SOCKET_ERROR)
		Abort("Impossibilidade de transmitir mensagem...", sock);
	
	/*========================== ESPERA CONFIRMACAO =======================*/
	nbytes=readLine(sock, buffer, sizeof(buffer));

	if(nbytes == SOCKET_ERROR) 
		Abort("Impossibilidade de receber confirmacao", sock);

	buffer[nbytes] = '\0';

	printf("<CLI> Confirmacao recebida {%s}.\n",buffer);

	/*=========================== FECHA SOCKET ============================*/
	closesocket(sock);

	WSACleanup();

	exit(EXIT_SUCCESS);
}


/*________________________________ Abort________________________________________
Mostra a mensagem de erro associada ao ultimo erro dos Winsock e abandona com 
"exit status" a 1
_______________________________________________________________________________
*/
void Abort(char *msg, SOCKET s)
{
	fprintf(stderr,"<CLI> Erro fatal: <%d>\n",WSAGetLastError());

	if(s != INVALID_SOCKET)
		closesocket(s);

	WSACleanup();

	exit(EXIT_FAILURE);
}

int writeN(SOCKET sock, char* buffer, int nbytes) {
	int nLeft, nWritten;
	nLeft = nbytes;
	while (nLeft > 0) {
		nWritten = send(sock, buffer, nLeft,0);
		if (nWritten == 0 || nWritten == SOCKET_ERROR)
			return nWritten; //Erro ou EOF
		nLeft -= nWritten;
		buffer += nWritten;
	}
	return nWritten;
}

int readLine(SOCKET sock, char* buffer, int nbytes) {
	int nRead, i;
	char c;
	i = 0;
	while (i < nbytes - 1) {
		if (nRead == SOCKET_ERROR)
			return nRead; //erro
		if (nRead == 0)
			break; //EOF
		if (c == '\n')
			break; //fim da linha
		buffer[i++] = c;
	}
	buffer[i] = '\0';
	return i;
}