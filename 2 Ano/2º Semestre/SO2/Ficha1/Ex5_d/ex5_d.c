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
    int vezes = -1, pos=0, i;

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
    
    _tcscpy_s(str, MAX, _T(""));

    if (argc == 1) {

        for (i = 0; i < _tcslen(argv[0]); i++) {
            if (argv[0][i] == '\\') {
                pos = i;
            }
        }
        _tcscpy_s(str, MAX, &argv[0][pos + 1]);
        str[_tcslen(str)] = '\0';
       
        vezes = 3;
        _stprintf_s(str, MAX, _T("%s %d"), str, vezes);
        _tprintf(_T("Primeira execução: %d\n"), GetCurrentProcessId());
        CreateProcess(NULL,
            str,
            NULL, NULL, FALSE, CREATE_NEW_CONSOLE, NULL, NULL,
            &si,
            &pi);

    }else {

        vezes = _tstoi(argv[1]);
        if (vezes > 0) {
            vezes = vezes - 1;
            _tprintf(_T("PID %d: %s %s\n"), GetCurrentProcessId(), argv[0], argv[1]);
            _stprintf_s(str, MAX, _T("%s %d"), argv[0], vezes);
                CreateProcess(NULL,
                    str,
                    NULL, NULL, FALSE, CREATE_NEW_CONSOLE, NULL, NULL,
                    &si,
                    &pi);
                WaitForSingleObject(pi.hProcess, INFINITE);
        }

    }

    CloseHandle(pi.hProcess);
    CloseHandle(pi.hThread);

    return 0;
}