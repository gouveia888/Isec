#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <stdio.h>
#include <fcntl.h> 
#include <math.h>

// número * máximo * de threads
// podem (e devem) ser menos
#define MAX_THREADS 20
#define BLOCO 10000

// funcionalidade relacionada com temporização
static double PerfCounterFreq;   // n ticks por seg.

void initClock() {
	LARGE_INTEGER aux;
	if (!QueryPerformanceFrequency(&aux))
		_tprintf(TEXT("\nSorry - No can do em QueryPerfFreq\n"));
	PerfCounterFreq = (double)(aux.QuadPart);   // / 1000.0;
	_tprintf(TEXT("\nTicks por sec.%f\n"), PerfCounterFreq);
}

__int64 startClock() {
	LARGE_INTEGER aux;
	QueryPerformanceCounter(&aux);
	return aux.QuadPart;
}

double stopClock(__int64 from) {
	LARGE_INTEGER aux;
	QueryPerformanceCounter(&aux);
	return (double)(aux.QuadPart - from) / PerfCounterFreq;
}

// estrutura de dados para controlar as threads
typedef struct {
	int* contadorblocos;
	unsigned int * cont_mult;
	CRITICAL_SECTION* cs;
	HANDLE hEv; //sinalizar asterisco no main
} TDados;

// função da(s) thread(s)
DWORD WINAPI Multiplos(LPVOID pdados) {
	int i;
	TDados* dados = (TDados*)pdados;

	while (1) {

		if (dados->contadorblocos == -1)
			break;

		int limite;

		EnterCriticalSection(dados->cs);
			dados->contadorblocos++;
			limite = 1 + (BLOCO * (int)dados->contadorblocos);
		LeaveCriticalSection(dados->cs);

		_tprintf(_T("\nInf %d, Sup %d\n"), limite, limite + BLOCO);

		for (i = limite; i <= limite + BLOCO; i++)
			if (i % 3 == 0) { //identificar zona critica
				EnterCriticalSection(dados->cs);
				(*(dados->cont_mult))++;
				LeaveCriticalSection(dados->cs);
			}
		SetEvent(dados->hEv);
		return 0;
	}

}

// função da(s) thread(s)
DWORD WINAPI Teclado(LPVOID pdados) {
	TDados* dados = (TDados*)pdados;
	char cmd[100];

	_tprintf_s(_T("\nInsira ""fim"" para terminar"));
	
	while (1) {
		_tscanf_s(TEXT("%s"), &cmd, 100);

		if (_tcscmp(cmd, "fim") == 0) {
			EnterCriticalSection(dados->cs);
			dados->contadorblocos = -1;
			LeaveCriticalSection(dados->cs);
			SetEvent(dados->hEv);
			break;
		}

	}
	return 0;
}

int _tmain(int argc, TCHAR* argv[]) {

	// matriz de handles das threads
	HANDLE hThreads[MAX_THREADS];

	// matriz de dados para as threads
	TDados tdados[MAX_THREADS];

	// número efectivo de threads
	int numthreads, i;

	// limite superior
	unsigned int contadorBlocos = 0, contadorMultiplos = 0;

	// variáveis para cronómetro
	__int64 clockticks;
	double duracao;

	unsigned int range;
	unsigned int inter;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
#endif 

	initClock();
	_tprintf_s(TEXT("\nNum threads -> "));
	_tscanf_s(TEXT("%u"), &numthreads);
	// FAZER: prepara e cria threads
	//        manda as threads começar

	CRITICAL_SECTION cs;
	InitializeCriticalSection(&cs); // BOOL ii = InitializeCriticalSectionAndSpinCount(&cs, 400);
	HANDLE hEv = CreateEvent(NULL,
							FALSE, //true - manual | false - automatico
							FALSE,
							_T("Evento")
						);

	TDados teclado;

	teclado.contadorblocos = &contadorBlocos;
	teclado.cont_mult = &contadorMultiplos;
	teclado.cs = &cs;
	teclado.hEv = hEv;

	CreateThread(NULL, 0, Teclado, &teclado, 0, NULL);

	//para ligar entre processos usar o OpenEvent(syncronize, false, "Evento")

	for (i = 0; i < numthreads; i++) {
		tdados[i].cont_mult = &contadorMultiplos;
		tdados[i].contadorblocos = &contadorBlocos;
		tdados[i].cs = &cs;
		tdados[i].hEv = hEv;
		hThreads[i] = CreateThread(NULL, 0, Multiplos, &tdados[i], 0, NULL);
	}

	SetEvent(hEv);

	clockticks = startClock();

	// FAZER: aguarda / controla as threads 
	//        manda as threads parar

	//Automatico
	
	int contadortemp;
	do {
		WaitForSingleObject(hEv, INFINITE);
		EnterCriticalSection(&cs);
		contadortemp = &contadorBlocos;
		LeaveCriticalSection(&cs);
		_tprintf_s(_T("*"));
	} while (contadortemp > 0);

	WaitForMultipleObjects(numthreads,
		hThreads,
		TRUE,
		INFINITE);

	duracao = stopClock(clockticks);
	_tprintf(TEXT("\nSegundos=%f\n"), duracao);
	_tprintf(TEXT("\nNúmeros multiplos de 3 [%d]\n"), contadorMultiplos);

	// FAZER: apresenta resultados

	// cód. ref. para aguardar por uma tecla – caso faça falta
	// _tprintf(TEXT("\nCarregue numa tecla"));
	// _gettch();

	for (int i = 0; i < numthreads; i++)
		CloseHandle(hThreads[i]);

	CloseHandle(hEv);
	DeleteCriticalSection(&cs);
	return 0;
}
// Este código é apenas uma ajuda para o exercício. Se quiser, mude-o.
