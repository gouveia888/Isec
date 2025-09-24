#include <windows.h>
#include <tchar.h>
#include <io.h>
#include <stdio.h>
#include <fcntl.h> 
#include <math.h>

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
	unsigned int lim_sup, * cont_mult, lim_inf;
	CRITICAL_SECTION* cs;
	HANDLE hEv;
} TDados;

// função da(s) thread(s)
DWORD WINAPI Multiplos(LPVOID pdados) {
	int i;
	TDados* dados = (TDados*)pdados;
	WaitForSingleObject(dados->hEv, INFINITE);
	ResetEvent(dados->hEv);
	for (i = dados->lim_inf; i <= dados->lim_sup; i++)
		if (i % 3 == 0) { //identificar zona critica
			EnterCriticalSection(dados->cs);
			(*(dados->cont_mult))++;
			LeaveCriticalSection(dados->cs);
		}
	return 0;
}

// número * máximo * de threads
// podem (e devem) ser menos
#define MAX_THREADS 20


int _tmain(int argc, TCHAR* argv[]) {

	// matriz de handles das threads
	HANDLE hThreads[MAX_THREADS];

	// matriz de dados para as threads
	TDados tdados[MAX_THREADS];

	// número efectivo de threads
	int numthreads, i;

	// limite superior
	unsigned int limsup, contadorMultiplos = 0;

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
	_tprintf_s(TEXT("\nLimite sup. -> "));
	_tscanf_s(TEXT("%u"), &limsup);
	_tprintf_s(TEXT("\nNum threads -> "));
	_tscanf_s(TEXT("%u"), &numthreads);
	if (numthreads > MAX_THREADS)
		numthreads = MAX_THREADS;

	// FAZER: prepara e cria threads
	//        manda as threads começar

	CRITICAL_SECTION cs;
	InitializeCriticalSection(&cs); // BOOL ii = InitializeCriticalSectionAndSpinCount(&cs, 400);
	HANDLE hEv = CreateEvent(NULL,
							FALSE, //true - manual | false - automatico
							FALSE,
							_T("Evento")
						);

	//para ligar entre processos usar o OpenEvent(syncronize, false, "Evento")

	for (i = 0; i < numthreads; i++) {
		tdados[i].cont_mult = &contadorMultiplos;
		tdados[i].cs = &cs;
		tdados[i].hEv = hEv;
		tdados[i].lim_inf = 1 + (limsup / numthreads) * i;
		tdados[i].lim_sup = (limsup / numthreads) * (i + 1);
		hThreads[i] = CreateThread(NULL, 0, Multiplos, &tdados[i], 0, NULL);
	}

	clockticks = startClock();

	// FAZER: aguarda / controla as threads 
	//        manda as threads parar
	
	//Automatico
	int j = 0;
	do {
		char c;
		_tprintf_s(_T("Insira uma letra!\n"));
		_tscanf_s(TEXT("%s"), &c);
		SetEvent(hEv);
		j++;
	} while (j <= numthreads);

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
