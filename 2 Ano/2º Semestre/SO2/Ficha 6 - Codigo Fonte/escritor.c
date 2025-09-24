#include <windows.h>
#include <tchar.h>
#include <stdio.h>
#include <io.h>
#include <fcntl.h>

#define PIPE_NAME _T("\\\\.\\pipe\\teste")

int _tmain(int argc, LPTSTR argv[]) {
    DWORD n;
    HANDLE hPipe;
    TCHAR buf[256];

#ifdef UNICODE
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif

    _tprintf_s(_T("[ESCRITOR] Criar uma cópia do pipe '%s' ... (CreateNamedPipe)\n"), 
        PIPE_NAME);
    hPipe = CreateNamedPipe(PIPE_NAME, PIPE_ACCESS_OUTBOUND, PIPE_WAIT 
        | PIPE_TYPE_MESSAGE | PIPE_READMODE_MESSAGE, 1, sizeof(buf), sizeof(buf), 
        1000, NULL);
    //PIPE_ACCESS_OUTBOUND servidor - cliene, 
    //PIPE_ACCESS_INBOUND cliente - serviddor
    // PIPE_ACCESS_DUPLEX
    //PIPE_WAIT modo bloqueante
    //pipe_nowait modo nao bloqueante
    //1 - numero de instancias maximas 
    if (hPipe == INVALID_HANDLE_VALUE) {
        _tprintf_s(_T("[ERRO] Criar Named Pipe! (CreateNamedPipe)"));
        exit(-1);
    }

    do {
        _tprintf_s(_T("[ESCRITOR] Esperar ligação de um leitor... (ConnectNamedPipe)\n"));
        if (!ConnectNamedPipe(hPipe, NULL)) {
            _tprintf_s(_T("[ERRO] Ligação ao leitor! (ConnectNamedPipe\n"));
            exit(-1);
        }

        do {
            _tprintf_s(_T("[ESCRITOR] Frase: "));
            _fgetts(buf, 256, stdin);
            buf[_tcslen(buf) - 1] = '\0';
            if (!WriteFile(hPipe, buf, (DWORD)_tcslen(buf) * sizeof(TCHAR), &n, NULL)) {
                _tprintf_s(_T("[ERRO] Escrever no pipe! (WriteFile)\n"));
                exit(-1);
            }
            _tprintf_s(_T("[ESCRITOR] Enviei %d bytes ao leitor... (WriteFile)\n"), n);
        } while (_tcsicmp(buf, _T("fim")));

        FlushFileBuffers(hPipe);

        _tprintf_s(_T("[ESCRITOR] Desligar o pipe (DisconnectNamedPipe)\n"));
        if (!DisconnectNamedPipe(hPipe)) {
            _tprintf_s(_T("[ERRO] Desligar o pipe! (DisconnectNamedPipe)"));
            exit(-1);
        }
    } while (_tcscmp(buf, _T("FIM")));
    CloseHandle(hPipe);
    return 0;
}
