#include <windows.h>
#include <stdio.h>
#include <conio.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>

#define MSGTXTSZ 60

typedef struct
{
    TCHAR msg[MSGTXTSZ];
} Msg;

#define Msg_Sz sizeof(Msg)

void PrintLastError(TCHAR* part, DWORD id)
{
    LPTSTR buffer;
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
        64, // FORMAT_MESSAGE_ALLOCATE_BUFFER is set => min bytes a alocar
        NULL);
    _tprintf(TEXT("\n%s Erro %d: %s\n"), part, id, buffer);

    LocalFree(buffer);
}

void readTChars(TCHAR* p, int maxchars)
{
    size_t len;
    _fgetts(p, maxchars, stdin);
    len = _tcslen(p);
    if (p[len - 1] == TEXT('\n'))
        p[len - 1] = TEXT('\0');
}

DWORD WINAPI ThreadClienteReader(LPVOID lpvParam);

int DeveContinuar = 1;
int ReaderAlive = 0;
HANDLE ReadReady;

void pressEnter()
{
    TCHAR somekeys[25];
    _tprintf(TEXT("\nPress enter > "));
    readTChars(somekeys, 25);
}

int _tmain(int argc, TCHAR* argv[])
{
    HANDLE hPipe;
    BOOL fSuccess = FALSE;
    DWORD cbWritten, cbBytesRead, dwMode;
    LPTSTR lpszPipename = TEXT("\\\\.\\pipe\\F6E7");

    Msg MsgToSend;
    HANDLE hThread;
    DWORD dwThreadId = 0;

    Msg FromServer;
    int ret;
    HANDLE hUserToken = NULL;

#ifdef UNICODE
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
    _setmode(_fileno(stderr), _O_WTEXT);
#endif

    while (1)
    {
        hPipe = CreateFile(
            lpszPipename,  // Nome do pipe
            GENERIC_READ | // acesso read e write
            GENERIC_WRITE,
            0 | FILE_SHARE_READ | FILE_SHARE_WRITE, // sem->com partilha
            NULL,                                   // atributos de segurança = default
            OPEN_EXISTING,                          // É para abrir um pipe já existente
            0 | FILE_FLAG_OVERLAPPED,               // atributos default
            NULL);                                  // sem ficheiro template

        if (hPipe != INVALID_HANDLE_VALUE)
            break;

        if ((ret = GetLastError()) != ERROR_PIPE_BUSY)
        {
            //_tprintf(TEXT("\nCreate file deu erro e não foi BUSY. Erro = %d\n"),
            //	GetLastError());
            // PrintLastError(TEXT("CreateFile"), ret);

            pressEnter();
            return -1;
        }

        // Aguarda por instância no máximo de 30 segs.
        if (!WaitNamedPipe(lpszPipename, 30000))
        {
            _tprintf(TEXT("Esperei por instância 30 segundos. Sair"));
            pressEnter();
            return -1;
        }
    }

    dwMode = PIPE_READMODE_MESSAGE;
    fSuccess = SetNamedPipeHandleState(
        hPipe,   // handle para o pipe
        &dwMode, // Novo modo do pipe
        NULL,    // Não é para mudar max. bytes
        NULL);   // Não é para mudar max. timeout

    if (!fSuccess)
    {
        PrintLastError(TEXT("SetNamesPipeHandleState falhou - "), 0);
        CloseHandle(hPipe);
        return -1;
    }

    ReadReady = CreateEvent(
        NULL,
        TRUE,
        FALSE,
        NULL);

    hThread = CreateThread(
        NULL,
        0,
        ThreadClienteReader,
        (LPVOID)hPipe,
        0,
        &dwThreadId);

    HANDLE WriteReady;
    OVERLAPPED OverlWr = { 0 };
    WriteReady = CreateEvent(
        NULL,
        TRUE,
        FALSE,
        NULL);

    if (WriteReady == NULL)
    {
        _tprintf(_T("\nCliente: nao foi possivel criar o evento\n"));
        return 1;
    }

    while (1)
    {
        readTChars(MsgToSend.msg, MSGTXTSZ);
        if (_tcscmp(TEXT("exit"), MsgToSend.msg) == 0)
            break;
        _tprintf(TEXT("\nA enviar %d bytes: \"%s\""),
            (int)Msg_Sz,
            MsgToSend.msg);

        ZeroMemory(&OverlWr, sizeof(OverlWr));
        ResetEvent(WriteReady);
        OverlWr.hEvent = WriteReady;
        fSuccess = WriteFile(
            hPipe,      // handle para o pipe
            &MsgToSend, // message (ponteiro)
            Msg_Sz,     // comprimento da messagem
            &cbWritten, // ptr p/ guarder num. bytes escritos
            &OverlWr);  // != NULL -> é overlapped I/O

        // WaitForSingleObject(WriteReady, INFINITE);

        GetOverlappedResult(hPipe, &OverlWr, &cbWritten, FALSE); // FALSE -> sem WAIT (já esperou no wait for single ...

        _tprintf(TEXT("\nMessagem enviada"));
    }

    DeveContinuar = 0;
    SetEvent(ReadReady);

    if (ReaderAlive)
    {
        WaitForSingleObject(hThread, 3000);
        _tprintf(TEXT("\nThread reader encerrada"));
    }
    _tprintf(TEXT("\nCliente vai terminar ligação e sair"));

    CloseHandle(WriteReady);
    CloseHandle(hPipe);
    pressEnter();
    return 0;
}

// Thread Reader

DWORD WINAPI ThreadClienteReader(LPVOID lpvParam)
{
    Msg FromServer;

    DWORD cbBytesRead = 0;
    BOOL fSuccess = FALSE;
    HANDLE hPipe = (HANDLE)lpvParam;

    OVERLAPPED OverlRd = { 0 };

    if (hPipe == NULL)
    {
        _tprintf(TEXT("\nThread Reader – o handle recebido no param da thread é nulo\n"));
        return -1;
    }

    OverlRd.hEvent = ReadReady;

    ReaderAlive = 1;
    _tprintf(TEXT("Thread Reader - a receber mensagens\n"));

    while (DeveContinuar)
    {

        // prepara leitura ASYNC
        ZeroMemory(&OverlRd, sizeof(OverlRd));
        OverlRd.hEvent = ReadReady;
        ResetEvent(ReadReady);

        fSuccess = ReadFile(
            hPipe,        // handle para o pipe (recebido no param)
            &FromServer,  // buffer para os dados a ler
            Msg_Sz,       // Tamanho msg a ler
            &cbBytesRead, // número de bytes a ler
            &OverlRd);    // != NULL -> é overlapped I/O

        WaitForSingleObject(ReadReady, INFINITE);
        if (DeveContinuar == 0)
        {
            _tprintf(TEXT("\nRecebida ordem na main para terminar "));
            break;
        }
        _tprintf(TEXT("\nRead concluido"));

        GetOverlappedResult(hPipe, &OverlRd, &cbBytesRead, FALSE); // sem WAIT
    }

    ReaderAlive = 0;
    _tprintf(TEXT("Thread Reader a terminar. \n"));
    return 1;
}