#include <Windows.h>
#include <TCHAR.H>
#include <fcntl.h>

#define MAXTH 2

typedef struct {
	int pares_lim_sup, pares_lim_inf, primos_lim_sup, primos_lim_inf, sumEven, sumPrime;
}TDATA;

// função de thread para calcular o sumatório dos numero pares
// pausa, no final de 200 numeros, por 1 segundo
DWORD WINAPI SumEvenNumbers(LPVOID pdados) {
	TDATA* dados = (TDATA*)pdados;

	_tprintf(_T("\n%d-%d-%d-%d\n"), dados->pares_lim_inf, dados->pares_lim_sup, dados->primos_lim_inf, dados->primos_lim_sup);
	int i, evenCount = 0;
	for (i = dados->pares_lim_inf; i <= dados->pares_lim_sup; i++)
		if (i % 2 == 0) {
			dados->sumEven += i;
			evenCount++;

			if (evenCount % 200 == 0)
				Sleep(10);
		}
	ExitThread(0);  // <- para quê ?? ou *ptr ou return
}


// função aux para verificar se num é primo
DWORD IsPrime(DWORD num) {
	if (num <= 1)  // o numero 1 NÃO É um numero primo   <- não é ???????
		return 0;

	for (DWORD i = 2; i < num; i++) {
		if (num % i == 0) {
			return 0;
		}
	}
	return 1; // se devolver 1 é numero primo
}


// função de thread para calcular sum dos números primos
DWORD WINAPI SumPrimeNumbers(LPVOID pParam) {
	int i;
	int primeCount = 0;
	TDATA* dados = (TDATA*)pParam;

	for (i = dados->primos_lim_inf; i <= dados->primos_lim_sup; i++) { //alterar valor dos primos
		if (IsPrime(i)) {
			dados->sumPrime += i;
			primeCount++;

			if (primeCount % 15 == 0)
				Sleep(1000);
		}
	}
	ExitThread(0);
}

//main

int _tmain(int argc, LPTSTR arg[]) {
	int sumEvenRet, sumPrimeRet;
	TDATA dados;

	HANDLE hthread[2]; // array de handles para as threads
	// podiam ser duas variáveis independentes
	// mas se forem "várias" mais vale um array (como em SO)

	dados.pares_lim_inf = 0;
	dados.pares_lim_sup = 1000;
	dados.primos_lim_inf = 0;
	dados.primos_lim_sup = 3;
	dados.sumEven = 0;
	dados.sumPrime = 0;

#ifdef UNICODE
	_setmode(_fileno(stdin), _O_WTEXT);
	_setmode(_fileno(stdout), _O_WTEXT);
	_setmode(_fileno(stderr), _O_WTEXT);
#endif

	hthread[0] = CreateThread(
		NULL,           // ptr p/ security attributes. NULL = use default
		0,              // init stack size. 0 = use default 
		SumEvenNumbers, // ptr p/ função da thread
		(LPVOID)&dados,       // argumento p/ função da thread
		0,              // creation flags. 0 = use default
		NULL);          // ptr p/ DWORD para thread ID. NULL = "i don't care and don't want it"

	hthread[1] = CreateThread(
		NULL,
		0,
		SumPrimeNumbers,
		(LPVOID)&dados,
		0,
		NULL);

	DWORD resultado;
	int count = 0;
	do {
		resultado = WaitForMultipleObjects(
			MAXTH - count, //numero de threads
			hthread, //ponteiro para o array com as id´s das threads -REORGANIZAR ARRAY-
			FALSE, //true espera por todas, false só a primeira
			5000); //limite de tempo, pode ser INFINITE

		int nt = resultado - WAIT_OBJECT_0;

		if (nt == 0) {
			GetExitCodeThread(
				hthread[0],//endereço da thread
				&dados);  // apanha o resultado 
			_tprintf(_T("Soma Pares\nValor da thread por argumentos %d, dos valores compreendidos entre %d e %d\n"), dados.sumEven, dados.pares_lim_inf, dados.pares_lim_sup);
			CloseHandle(hthread[0]);

			WaitForSingleObject(hthread[1], INFINITE);
			GetExitCodeThread(
				hthread[1],//endereço da thread
				&dados);  // apanha o resultado 

			_tprintf(_T("Soma Primos\nValor da thread por argumento %d, dos valores compreendidos entre %d e %d\n"), dados.sumPrime, dados.primos_lim_inf, dados.primos_lim_sup);
			CloseHandle(hthread[1]);

		}

		if (nt == 1) {
			GetExitCodeThread(
				hthread[1],//endereço da thread
				&sumPrimeRet);  // apanha o resultado 

			_tprintf(_T("Soma Primos\nValor da thread por argumento %d, dos valores compreendidos entre %d e %d\n"), dados.sumPrime, dados.primos_lim_inf, dados.primos_lim_sup);
			CloseHandle(hthread[1]);

			WaitForSingleObject(hthread[0], INFINITE);
			GetExitCodeThread(
				hthread[0],//endereço da thread
				&sumEvenRet);  // apanha o resultado 
			_tprintf(_T("Soma Pares\nValor da thread por argumentos %d, dos valores compreendidos entre %d e %d\n"), dados.sumEven, dados.pares_lim_inf, dados.pares_lim_sup);
			CloseHandle(hthread[0]);
		}

		//REORGANIZAR O ARRAY DE THREADS
		//if (nt == 0)
			//hthread[0] == hthread[1];

		count++;
	} while (count < 2);


	return 0;

}


/*

INTEL

HANDLE CreateThread(
  LPSECURITY_ATTRIBUTES   lpThreadAttributes, -- child process can hinerit the handle? NULL = não
  SIZE_T                  dwStackSize,        -- initial stack size in bytes (arredondado para multiplo. de page size = 4k). 0=default
  LPTHREAD_START_ROUTINE  lpStartAddress,     -- ptr para a thread function
  LPVOID                  lpParameter,        -- argumento para a função = whatever caiba num sizeof de void * -> como em SO
  DWORD                   dwCreationFlags,    -- flags (o = deafault). de interesse: CREATE_SUSPENDED
  LPDWORD                 lpThreadId          -- ptr para a dword para cicar com a thread ID. NULL = don't care & don't want it
);


*/