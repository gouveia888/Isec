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
    LPTSTR lpszPipename = TEXT("\\\\.\\pipe\\F6E6");

    Msg MsgToSend, FromServer;
    int ret;

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

    while (1)
    {

        _tprintf(TEXT("\nEscreve (exit para sair)\n"));
        readTChars(MsgToSend.msg, MSGTXTSZ);
        if (_tcscmp(TEXT("exit"), MsgToSend.msg) == 0)
            break;

        _tprintf(TEXT("\nA enviar %d bytes: \"%s\""),
            (int)Msg_Sz,
            MsgToSend.msg);

        fSuccess = WriteFile(
            hPipe,      // handle para o pipe
            &MsgToSend, // message (ponteiro)
            Msg_Sz,     // comprimento da messagem
            &cbWritten, // ptr p/ guarder num. bytes escritos
            NULL);      // NULL -> NÂO É overlapped I/O

        _tprintf(TEXT("\nWrite concluido"));
        if (!fSuccess)
        {
            PrintLastError(TEXT("\nWriteFile deu fSuccess FALSE - "), GetLastError());
            break;
        }
        if (cbWritten < Msg_Sz)
        {
            PrintLastError(TEXT("\nWriteFile escreveu menos bytes que esperado - "), GetLastError());
            break;
        }

        _tprintf(TEXT("\nAguardar resposta com ReadFile"));
        fSuccess = ReadFile(
            hPipe,
            &FromServer,
            Msg_Sz,
            &cbBytesRead,
            NULL);

        _tprintf(TEXT("\nRead concluido"));
        if (!fSuccess)
        {
            PrintLastError(TEXT("\nReadFile deu fSuccess FALSE - "), GetLastError());
            break;
        }
        if (cbBytesRead < Msg_Sz)
        {
            PrintLastError(TEXT("\nReadFile leu menos bytes que esperado - "), GetLastError());
            break;
        }

        _tprintf(TEXT("\nServidor disse: [%s]"), FromServer.msg);
    }

    _tprintf(TEXT("\nCliente vai terminar ligação e sair"));

    CloseHandle(hPipe);
    return 0;
}