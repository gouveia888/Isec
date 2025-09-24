#include <windows.h>
#include <tchar.h>
#include <stdio.h>
#include <io.h>
#include <fcntl.h>

#define PIPE_NAME _T("\\\\.\\pipe\\teste")
#define BUF_SIZE 255
#define MAX_CLI 5

DWORD WINAPI Mensagem(LPVOID cdata);

typedef struct {
    HANDLE hPipe[MAX_CLI];
    HANDLE hmutex;
    int terminar;
    int cont; //contador de clientes
}DATA;

int _tmain(int argc, LPTSTR argv[]) {
    HANDLE hPipe, hThread;
    int i;

    DATA data;

#ifdef UNICODE
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif

    data.cont = 0;
    data.terminar = 0;

    data.hmutex = CreateMutex(NULL, FALSE, NULL);

    if (data.hmutex == NULL) {
        //ERRO AO CRIAR MUTEX
    }

    hThread = CreateThread(NULL, 0, Mensagem, &data, 0, NULL);

    if (hThread == NULL) {
        _tprintf(_T("ERROR  Criar Thread"));
        exit(-1);
    }

    while (!data.terminar) {
        _tprintf_s(_T("[ESCRITOR] Criar uma cópia do pipe '%s' ... (CreateNamedPipe)\n"),
            PIPE_NAME);
        hPipe = CreateNamedPipe(PIPE_NAME, PIPE_ACCESS_OUTBOUND, PIPE_WAIT
            | PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE, MAX_CLI, sizeof(TCHAR) * BUF_SIZE, sizeof(TCHAR) * BUF_SIZE,
            1000, NULL);
        if (hPipe == INVALID_HANDLE_VALUE) {
            _tprintf_s(_T("[ERRO] Criar Named Pipe! (CreateNamedPipe)"));
            exit(-1);
        }


        _tprintf_s(_T("[ESCRITOR] Esperar ligação de um leitor... (ConnectNamedPipe)\n"));
        if (!ConnectNamedPipe(hPipe, NULL)) {
            _tprintf_s(_T("[ERRO] Ligação ao leitor! (ConnectNamedPipe\n"));
            exit(-1);
        }

        WaitForSingleObject(data.hmutex, INFINITE);
        data.hPipe[data.cont] = hPipe;
        data.cont++;
        ReleaseMutex(data.hmutex);

    }
    WaitForSingleObject(hThread, INFINITE);

    for (i = 0; i < data.cont; i++) {
        _tprintf(_T("Desligar Pipe\n"));
        if (!DisconnectNamedPipe(data.hPipe[i])) {
            _tprintf(_T("Desligar Pipe\n"));
            exit(-1);
        }
        CloseHandle(data.hPipe[i]);
    }

    return 0;
}

DWORD WINAPI Mensagem(LPVOID cdata) {

    TCHAR buf[255];
    DATA* data = (DATA*)cdata;
    DWORD n;

    do {
        _tprintf_s(_T("[ESCRITOR] Frase: "));
        _fgetts(buf, 256, stdin);
        buf[_tcslen(buf) - 1] = '\0';

        WaitForSingleObject(data->hmutex, INFINITE); //writeFile nao devia estar dentro do mutex  variaveis auxiliares

        for (int i = 0; i < data->cont; i++) {
            if (!WriteFile(data->hPipe[i], buf, (DWORD)_tcslen(buf) * sizeof(TCHAR), &n, NULL)) {
                _tprintf_s(_T("[ERRO] Escrever no pipe! (WriteFile)\n"));
                exit(-1);
            }
            _tprintf_s(_T("[ESCRITOR] Enviei %d bytes ao leitor [%d]... (WriteFile)\n"), n, i);
        }
        WaitForSingleObject(data->hmutex, INFINITE);
    } while (_tcsicmp(buf, _T("fim")));
    data->terminar = 1;
    CreateFile(PIPE_NAME, GENERIC_READ, 0, NULL, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, NULL);
    return 0;
}