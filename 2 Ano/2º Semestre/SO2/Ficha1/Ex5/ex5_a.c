#include <windows.h> 
#include <tchar.h> 
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#define MAX 256
#pragma warning (disable:6031)

int _tmain(int argc, LPTSTR argv[]) { //main que iremos a utilizar
    TCHAR path[MAX];
    //UNICODE: Por defeito, a consola Windows não processa caracteres wide. 
    //A maneira mais fácil para ter esta funcionalidade é chamar _setmode:

//obrigatório
#ifdef UNICODE 
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
#endif

    _tprintf(TEXT("O nome do programa é %s\n"), argv[0]);
    GetModuleFileName(NULL,path,MAX); //devolve -1 em caso de erro
    _tprintf(_T("O nome do programa é %s\n"), path);

    return 0;
}