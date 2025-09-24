#include <windows.h>
#include <stdio.h> 
#include <tchar.h>
#include <io.h>        
#include <fcntl.h>     

#define BUFSIZE 2048   

#define MSGTXTSZ 60

typedef struct {
	TCHAR msg[MSGTXTSZ];
} Msg;

#define Msg_Sz sizeof(Msg)

void readTChars(TCHAR* p, int maxchars) {
	size_t len;
	_fgetts(p, maxchars, stdin);
	len = _tcslen(p);
	if (p[len - 1] == TEXT('\n'))
		p[len - 1] = TEXT('\0');
}

void pressEnter() {
	TCHAR somekeys[25];
	_tprintf(TEXT("\nPress enter > "));
	readTChars(somekeys, 25);
}

void PrintLastError(TCHAR* part, DWORD id) {
	LPTSTR buffer;  // auto alocado
	if (part == NULL)
		part = TEXT("*");
	if (id == 0)
		id = GetLastError();
	FormatMessage(
		FORMAT_MESSAGE_ALLOCATE_BUFFER |
		FORMAT_MESSAGE_FROM_SYSTEM |
		FORMAT_MESSAGE_IGNORE_INSERTS,
		NULL,
		id,
		MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
		(LPTSTR)&buffer,
		64,
		NULL);
	_tprintf(TEXT("\n%s Erro %d: %s\n"), part, id, buffer);
	LocalFree(buffer);
}

DWORD WINAPI InstanceThread(LPVOID lpvParam);

#define MAX_CLIENTES 10
HANDLE clientes[MAX_CLIENTES];

void iniciaClientes() {
	int i;
	for (i = 0; i < MAX_CLIENTES; i++) {
		clientes[i] = NULL;
	}
}

//ADICIONAR MUTEX
void adicionaClientes(HANDLE cli) {
	int i;
	for (i = 0; i < MAX_CLIENTES; i++) {
		if (clientes[i] == NULL) {
			clientes[i] = cli;
		}
	}
	return;
}

void removeClientes(HANDLE cli) {
	int i;
	for (i = 0; i < MAX_CLIENTES; i++) {
		if (clientes[i] == cli) {
			clientes[i] = NULL;
		}
	}
	return;
}

HANDLE WriteReady;

int writeClienteASINC(HANDLE hPipe, Msg msg) {
	DWORD cbWritten = 0;
	BOOL fSuccess = FALSE;
	OVERLAPPED OverlWr = { 0 };
	_tprintf(TEXT("\nA enviar %d bytes com \"%s\""),
		(int)Msg_Sz, msg.msg);
	ZeroMemory(&OverlWr, sizeof(OverlWr));  // não necessário porque { 0 }
	ResetEvent(WriteReady);  // não assinalado
	OverlWr.hEvent = WriteReady;
	fSuccess = WriteFile(
		hPipe,               // handle para o pipe 
		&msg,                // message (ponteiro) 
		Msg_Sz,              // comprimento da messagem 
		&cbWritten,          // ptr p/ guarder num. bytes escritos 
		&OverlWr);           // != NULL -> É mesmo overlapped I/O
	WaitForSingleObject(WriteReady, INFINITE); // <<-- colocar aqui um valor máximo
	GetOverlappedResult(hPipe, &OverlWr, &cbWritten, FALSE);  // sem WAIT
	if (cbWritten == Msg_Sz) {
		_tprintf(TEXT("\nWrite para 1 cliente concluido"));
		return 1;
	}
	else {
		PrintLastError(TEXT("\nOcorreu algo na escrita para 1 cliente"), GetLastError());
		return 0;
	}

}

int broadcastClientes(Msg msg) {
	int i, numwrites = 0;
	for (i = 0; i < MAX_CLIENTES; i++)
		if (clientes[i] != NULL)
			if (writeClienteASINC(clientes[i], msg))
				++numwrites;
			else {
				removeCliente(clientes[i]);
				_tprintf(TEXT("\nCliente suspeito removido"));
			}
	return numwrites;
}

// ----------------------------------------------------
// ----------------------------------------------------

int _tmain(VOID) {   // aplicação genérica quanto a char/Unicode
	BOOL   fConnected = FALSE;
	DWORD  dwThreadId = 0;
	HANDLE hPipe = INVALID_HANDLE_VALUE, hThread = NULL;
	LPTSTR lpszPipename = TEXT("\\\\.\\pipe\\F6E6");

	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);

	WriteReady = CreateEvent(NULL, TRUE, FALSE, NULL);

	iniciaClientes();

	while (1) {
		_tprintf(TEXT("\nServidor – ciclo principal – CreateNamedPipe = %s"),
			lpszPipename);
		hPipe = CreateNamedPipe(
			lpszPipename,             // nome do pipe 
			PIPE_ACCESS_DUPLEX | FILE_FLAG_OVERLAPPED,       // acesso read/write 
			PIPE_TYPE_MESSAGE |       // tipo de pipe = message
			PIPE_READMODE_MESSAGE |   // com modo message-read e
			PIPE_WAIT,                // bloqueante 
			MAX_CLIENTES, // max. instancias (255)
			BUFSIZE,                  // tam buffer output
			BUFSIZE,                  // tam buffer input 
			5000,                     // time-out p/ cliente 5k milisegundos (0->default=50)
			NULL);                    // atributos segurança default --> sa com NULL DACL = Happy hour

		if (hPipe == INVALID_HANDLE_VALUE) {
			PrintLastError(TEXT("\nCreateNamedPipe falhou, erro = %d"), GetLastError());
			//_tprintf(TEXT("\nCreateNamedPipe falhou, erro = %d"), GetLastError());
			return -1;                // Nada a fazer
		}

		_tprintf(TEXT("\nServidor a aguardar que um cliente se ligue"));


		fConnected = ConnectNamedPipe(hPipe, NULL) ?
			TRUE : (GetLastError() == ERROR_PIPE_CONNECTED);

		if (fConnected) {
			_tprintf(TEXT("\nCliente ligado"));

			hThread = CreateThread(
				NULL,              // Sem atributos de segurança
				0,                 // Tam. de pilha default 
				InstanceThread,    // Função da thread
				(LPVOID)hPipe,     // Parâmetro para a thread = handle
				0,                 // inicialmente não suspensa 
				&dwThreadId);      // Ptr p/ onde colocar ID da thread

			if (hThread == NULL) {
				_tprintf(TEXT("\nErro na criação da thread. Erro = %d"),
					GetLastError());
				return -1;
			}
			else
				CloseHandle(hThread);
		}
		else
			// O cliente não conseguiu ligar – fecha esta instância do pipe 
			CloseHandle(hPipe);
	}


	return 0;
}

DWORD WINAPI InstanceThread(LPVOID lpvParam) {
	Msg Pedido, Resposta;
	DWORD cbBytesRead = 0, cbReplyBytes = 0;
	DWORD cbWritten = 0;
	int numresp = 0;
	BOOL fSuccess = FALSE;
	HANDLE hPipe = (HANDLE)lpvParam;

	HANDLE ReadReady;
	OVERLAPPED OverlRd = { 0 };

	if (hPipe == NULL) {
		_tprintf(TEXT("\nErro – o handle enviado no param da thread é nulo"));
		return -1;
	}

	_tprintf(TEXT("\nThread dedicada do servidor - a receber mensagens"));

	ReadReady = CreateEvent(NULL, TRUE, FALSE, NULL);
	adicionaClientes(hPipe);

	// Ciclo de diálogo com o cliente: 
	while (1) {

		ZeroMemory(&OverlRd, sizeof(OverlRd));
		ResetEvent(ReadReady);
		OverlRd.hEvent = ReadReady;


		fSuccess = ReadFile(
			hPipe,         // handle para o pipe (recebido no param)
			&Pedido,       // buffer para os dados a ler 
			Msg_Sz,        // Tamanho msg a ler
			&cbBytesRead,  // número de bytes lidos 
			&OverlRd);         // NULL -> não é overlapped I/O 

		_tprintf(TEXT("\nRead de Cliente concluido"));
		WaitForSingleObject(ReadReady, INFINITE); //vai ficar sempre aqui presso

		GetOverlappedResult(hPipe, &OverlRd, &cbBytesRead, FALSE);

		if (!fSuccess) {
			PrintLastError(TEXT("\nReadFile deu fSuccess FALSE - "), GetLastError());
			break;
		}
		if (cbBytesRead < Msg_Sz) {
			PrintLastError(TEXT("\nReadFile leu menos bytes que o esperado - "), GetLastError());
			break;
		}

		_tprintf(TEXT("\nServidor: Recebi msg: [%s]"), Pedido.msg);
		_tcscpy_s(Resposta.msg, MSGTXTSZ, Pedido.msg);
		for (int i = 0; i < _tcslen(Resposta.msg); ++i)
			Resposta.msg[i] = _totupper(Resposta.msg[i]);

		numresp = broadcastClientes(Resposta);
	}

	removeClientes(hPipe);

	FlushFileBuffers(hPipe);
	DisconnectNamedPipe(hPipe); // Desliga servidor da instância
	CloseHandle(hPipe); // Fecha este lado desta instância

	_tprintf(TEXT("\nThread dedicada Cliente a terminar"));
	return 1;
}

