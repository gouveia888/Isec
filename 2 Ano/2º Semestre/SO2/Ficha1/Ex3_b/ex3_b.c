#include <windows.h> //windows api
#include <tchar.h> //
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#define MAX 256
#pragma warning (disable:6031)

int _tmain(int argc, LPTSTR argv[]) { //main que iremos a utilizar
    TCHAR str[MAX], result[MAX] = TEXT("Olá! Este programa é para aceitar UNICODE. Insira \'fim\' para sair\n");
    unsigned int i;
    //UNICODE: Por defeito, a consola Windows não processa caracteres wide. 
    //A maneira mais fácil para ter esta funcionalidade é chamar _setmode:

//obrigatório
#ifdef UNICODE 
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
#endif
    _tprintf(TEXT("SO - II \n"));
    do {
        _tprintf(result);
        fflush(stdin);
        _fgetts(str, MAX, stdin);
        //Retirar \n
        str[_tcslen(str) - 1] = '\0';
        //Maiúsculas
        for (i = 0; i < _tcslen(str); i++)
            str[i] = _totupper(str[i]);
        _stprintf_s(result, MAX, TEXT("Frase:%s, Tamanho:%d\n"), str, (int)_tcslen(str));
    } while (_tcsicmp(TEXT("FIM"), str));
    return 0;
}