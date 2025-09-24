#include <windows.h> 
#include <tchar.h> 
#include <fcntl.h>
#include <io.h>
#include <stdio.h>
#define MAX 256
#pragma warning (disable:6031)

#define fileName _T("texto.txt")

int _tmain(int argc, LPTSTR argv[]) { //main que iremos a utilizar

//obrigatório
#ifdef UNICODE 
    _setmode(_fileno(stdin), _O_WTEXT);
    _setmode(_fileno(stdout), _O_WTEXT);
#endif


    HANDLE pFicheiroMapeado = CreateFile(fileName,
                                        GENERIC_READ | GENERIC_WRITE, 
                                        FILE_SHARE_READ, 
                                        NULL, 
                                        OPEN_EXISTING, 
                                        FILE_ATTRIBUTE_NORMAL, 
                                        NULL);

    if (pFicheiroMapeado == NULL)
        _tprintf(_T("Error - %d\n"), GetLastError());
    else
        _tprintf(_T("Handler do Ficheiro criado com sucesso\n"));

    HANDLE pMepFicheiro = CreateFileMapping(pFicheiroMapeado,
                                            NULL, 
                                            PAGE_READWRITE, 
                                            0, 26, 
                                            NULL); //NOME DO map

    if (pMepFicheiro == NULL)
        return 0;
    else
        _tprintf(_T("Handler FileMapping criado com sucesso\n"));

    char* pVistaFicheiro = (char*)MapViewOfFile(pMepFicheiro,
                                                FILE_MAP_READ | FILE_MAP_WRITE,
                                                0, 
                                                0, 
                                                26);


    if (pFicheiroMapeado == NULL)
        return 0;
    else
        _tprintf(_T("Handler da Vista do Ficheiro criado com sucesso\n"));

    _tprintf(_T("Texto incial do ficheiro:\n"));
    

    for (DWORD i = 0; i < 26; i++) {
        _tprintf(_T("%c"), pVistaFicheiro[i]);
    }
    char aux; int j = 25;
    for (DWORD i = 0; i < 13; i++) {
        aux = pVistaFicheiro[i];
        pVistaFicheiro[i] = pVistaFicheiro[j];
        pVistaFicheiro[j] = aux;
        j--;
    }

    _tprintf(_T("\nTexto final do ficheiro:\n"));


    for (DWORD i = 0; i < 26; i++) {
        _tprintf(_T("%c"), pVistaFicheiro[i]);
    }


    UnmapViewOfFile(pVistaFicheiro);
    CloseHandle(pMepFicheiro);
    CloseHandle(pFicheiroMapeado);

    return 0;
}