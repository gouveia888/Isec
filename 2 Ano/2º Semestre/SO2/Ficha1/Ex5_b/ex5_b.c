#include <windows.h> 
#include <tchar.h> 
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#define MAX 256
#pragma warning (disable:6031)

int _tmain(int argc, LPTSTR argv[]) { //main que iremos a utilizar

    PROCESS_INFORMATION pi;
    STARTUPINFO si;
    TCHAR str[MAX];
    int lenght;

    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory(&pi, sizeof(pi));

    //UNICODE: Por defeito, a consola Windows não processa caracteres wide. 
    //A maneira mais fácil para ter esta funcionalidade é chamar _setmode:

//obrigatório
#ifdef UNICODE 
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
#endif

    do {
        _tprintf(_T("Insira o nome:"));
        fflush(stdin);
        _fgetts(str, MAX, stdin);
        str[_tcslen(str) - 1] = '\0';

        CreateProcess(  NULL,
                        str,
                        NULL, NULL, FALSE, 0, NULL, NULL,
                        &si,
                        &pi);

    } while (_tcsicmp(str,_T("fim")));

    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

    return 0;
}