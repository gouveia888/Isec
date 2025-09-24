#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>

#define TAM 200

int _tmain(int argc, TCHAR *argv[]){
  HKEY chave;
  TCHAR chave_nome[TAM], par_nome[TAM], par_valor[TAM];

  /* ... Mais variáveis ... */

#ifdef UNICODE 
  _setmode(_fileno(stdin), _O_WTEXT);
  _setmode(_fileno(stdout), _O_WTEXT);
  _setmode(_fileno(stderr), _O_WTEXT);
#endif

  /* ... as várias alíneas ...*/

   RegCloseKey(chave);

   return 0;
}