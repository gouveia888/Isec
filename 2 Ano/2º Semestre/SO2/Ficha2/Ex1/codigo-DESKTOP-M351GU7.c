#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <fcntl.h>
#include <stdio.h>
#pragma warning(disable: 6031)
#define TAM 200


int _tmain(int argc, TCHAR* argv[]) {
	HKEY chave = 0;
	TCHAR chave_nome[TAM], par_nome[TAM], par_valor[TAM];
	TCHAR caminho[TAM];
	DWORD op, estado;
	BOOL continua = TRUE;
	LSTATUS res;
	DWORD i, tam_nome_par;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	do {
		_tprintf_s(_T("\nOpções\n")
			_T("1 - Criar KEY (ou abrir se já existir) (\"chave\")\n")
			_T("2 - Acrescentar (criar) um par - valor\n")
			_T("3 - Consultar um par-valor da key em uso no programa\n")
			_T("4 - Apagar um par-valor da KEY etc\n")
			_T("5 - listar todos os pares-valor da KEY etc\n")
			_T("6 - apagar a KEY (e todas os pares valo e subchaves que houvesse\n")
			_T("9 - Sair\n")
			_T(" ->"));
		_tscanf_s(_T("%d"), &op);
		switch (op) {
			//  b) 1 - Criar KEY (ou abrir se já existir) 
		case 1:
			//utilizar -> RegCreateKeyEx
			// https://docs.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regcreatekeyexa	
			_tcscpy_s(caminho, TAM, _T("Software\\Aula\\"));
			_tprintf_s(_T("Nome KEY?"));
			_tscanf_s(_T("%s"), chave_nome, TAM);
			_tcscat_s(caminho, TAM, chave_nome);
			res = RegCreateKeyEx(HKEY_CURRENT_USER, caminho,
								 0, NULL,
								 REG_OPTION_NON_VOLATILE,
								 KEY_ALL_ACCESS,
								 NULL,
								 &chave,
								 &estado);
			if (res == ERROR_SUCCESS) {
				if (estado == REG_OPENED_EXISTING_KEY) {
					if (estado == REG_CREATED_NEW_KEY)
						_tprintf_s(_T("Sucesso chave criada!\n"));
					else
						_tprintf_s(_T("Chave aberta!\n"));
				}
				else
					_tprintf_s(_T("Erro!"));
			}
			break;
			// d i) Acrescentar (criar) um par - valor do tipo "string REG_SZ"
		case 2:
			//utilizar -> RegSetValueEx
			//https://learn.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regsetkeyvaluea
			if (chave != 0) {
				_tprintf_s(_T("Nome do PAR?"));
				_tscanf_s(_T("%s"), par_nome, TAM);
				_tprintf_s(_T("Valor do PAR?"));
				_tscanf_s(_T("%s"), par_valor, TAM);

				res = RegSetValueEx(chave, 
									par_nome, 
									0, REG_SZ, 
									(LPBYTE) par_valor,
									(_tcslen(par_valor) + 1 )* sizeof(TCHAR)
					  );
				if(res == ERROR_SUCCESS) {
					if (estado == REG_CREATED_NEW_KEY)
						_tprintf_s(_T("Erro!\n"));
					else
						_tprintf_s(_T("Sucesso!\n"));
				}
			}
			break;

			//d ii) Consultar um par-valor da key do tipo string = REG_SZ em uso no programa (na chave em questão)
		case 3:
			//utilizar RegQueryValueEx
			//https://learn.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regqueryvalueexa
			if (chave != 0) {
				DWORD par_tipo, tam = sizeof(par_valor);
				_tprintf_s(_T("Nome do PAR?"));
				_tscanf_s(_T("%s"), par_nome, TAM);

				res = RegQueryValueEx(chave, par_nome, 0, &par_tipo, NULL, NULL);

				if (res == ERROR_SUCCESS && par_tipo == REG_SZ) {
					res == RegQueryValueEx(chave, par_nome, 0, &par_tipo, (LPBYTE)par_valor, &tam);
					if (res == ERROR_SUCCESS) {
						_tprintf(_T("O valor do par %s é %s"), par_nome, par_valor);
					}
					_tprintf(_T("O par %s existe mas nao e do tipo correto REG_SZ"), par_nome);
				}
				_tprintf(_T("O par %s existe mas não é do tipo REG_SZ"), par_nome);
			}
			_tprintf(_T("O par %s não existe"), par_nome);
			break;

			//d iii) Apagar um par-valor da KEY etc
		case 4:
			//utilizar RegDeleteValue
			//https://learn.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regdeletevaluea
			_tprintf_s(_T("Nome do PAR?"));
			_tscanf_s(_T("%s"), par_nome, TAM);
			res = RegDeleteValue(chave, par_nome);
			if (res == ERROR_SUCCESS)
				_tprintf_s(_T("O par %s foi apagado\n"), par_nome);
			else
				_tprintf_s(_T("O par %s não existe\n"), par_nome);
			break;

			// e) listar todos os pares - valor da KEY etc
		case 5:
			//utilizar RegEnumValue

			if (chave != 0) {
				i = 0;
				tam_nome_par = TAM;

				while (RegEnumValue(chave, i, par_nome, &tam_nome_par, NULL, NULL, NULL, NULL) == ERROR_SUCCESS) {
					_tprintf(_T("O par-nome %d com o nome %s"), i, par_nome);
					i++;
					tam_nome_par = TAM;
				}
				
			}
			//https://docs.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regenumvaluea
			break;

			// f) apagar a KEY
		case 6:
			//utilizar RegDeleteTree ou RegDeleteKey
			//https://learn.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regdeletetreea
			//https://learn.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regdeletekeya
			_tcscpy(caminho, TAM, _T("Software\\Aula\\"));
			_tprintf_s(_T("Nome da KEY?"));
			_tscanf_s(_T("%s"), chave_nome, TAM);
			_tcscat_s(caminho, TAM, chave_nome);
			res = RegDeleteTree(HKEY_CURRENT_USER, caminho);

			break;
			// sair
		case 9:
			if (chave != 0)
				RegCloseKey(chave); //https://docs.microsoft.com/en-us/windows/win32/api/winreg/nf-winreg-regclosekey
			continua = FALSE;
			break;
		default:
			break;
		}
	} while (continua);
	return 0;
}